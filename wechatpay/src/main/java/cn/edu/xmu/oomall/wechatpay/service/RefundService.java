package cn.edu.xmu.oomall.wechatpay.service;

import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.wechatpay.dao.PaymentDao;
import cn.edu.xmu.oomall.wechatpay.dao.RefundDao;
import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import cn.edu.xmu.oomall.wechatpay.dao.bo.RefundTrans;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RefundService {
    private static final String TRADE_STATE_FAIL = "NOTPAY";
    private static final String TRADE_STATE_CLOSED = "CLOSED";
    private static final String TRADE_STATE_REFUND = "REFUND";
    private static final String REFUND_STATUS_SUCCESS = "SUCCESS";
    private static final String REFUND_STATUS_FAIL = "ABNORMAL";

    private final PaymentDao paymentDao;
    private final RefundDao refundDao;
    private final SnowFlakeIdWorker snowFlakeIdWorker;

    @Autowired
    public RefundService(PaymentDao paymentDao, RefundDao refundDao, SnowFlakeIdWorker snowFlakeIdWorker) {
        this.paymentDao = paymentDao;
        this.refundDao = refundDao;
        this.snowFlakeIdWorker = snowFlakeIdWorker;
    }

    public RefundTrans createRefund(RefundTrans refundTrans) {
        if (refundTrans.getAmount().getRefund() <= 0) {
            throw new WeChatPayException(WeChatPayReturnNo.REFUND_AMOUNT_ERROR);
        }

        RefundTrans old = refundDao.getRefundByOutRefundNo(refundTrans.getOutRefundNo());
        if (old != null) {
            throw new WeChatPayException(WeChatPayReturnNo.OUT_REFUND_NO_USED);
        }

        String transId = refundTrans.getTransactionId();
        PayTrans payTrans = paymentDao.getPayTransByTransId(transId);
        if (payTrans == null) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        } else if (payTrans.getTradeState().equals(TRADE_STATE_CLOSED)) {
            throw new WeChatPayException(WeChatPayReturnNo.ORDER_CLOSED);
        } else if (payTrans.getTradeState().equals(TRADE_STATE_FAIL)){
            throw new WeChatPayException(WeChatPayReturnNo.ORDER_FAIL);
        } else if (!payTrans.getAmount().getTotal().equals(refundTrans.getAmount().getTotal())) {
            throw new WeChatPayException(WeChatPayReturnNo.INCONSISTENT_AMOUNT);
        }

        String outTradeNo = payTrans.getOutTradeNo();
        List<RefundTrans> refundList = refundDao.getRefundsByOutTradeNo(outTradeNo);

        int totalRefund = (refundList == null) ? 0 : refundList.stream().mapToInt(bo -> bo.getAmount().getRefund()).sum();
        if (totalRefund + refundTrans.getAmount().getRefund() > payTrans.getAmount().getTotal()) {
            throw new WeChatPayException(WeChatPayReturnNo.REFUND_AMOUNT_ERROR);
        }

        refundTrans.setOutTradeNo(outTradeNo);
        refundTrans.setUserReceivedAccount(payTrans.getPayer().getSpOpenid());  // 模拟系统不支持查询银行账户，因此以spOpenid替之
        refundTrans.setCreateTime(LocalDateTime.now());

        boolean fail = (refundTrans.getAmount().getRefund().equals(901));  // API测试
        return fail ? refundFail(refundTrans) : refundSuccess(refundTrans);
    }

    public RefundTrans getRefund(String subMchid, String outRefundNo) {
        RefundTrans refundTrans = refundDao.getRefundByOutRefundNo(outRefundNo);
        if (refundTrans == null) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        } else if (!refundTrans.getSubMchid().equals(subMchid)) {
            throw new WeChatPayException(WeChatPayReturnNo.NO_AUTH);
        }
        return refundTrans;
    }

    private RefundTrans refundSuccess(RefundTrans refundTrans) {
        PayTrans payTrans = paymentDao.getPayTransByOutNo(refundTrans.getOutTradeNo());
        if (payTrans == null) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        }
        payTrans.setTradeState(TRADE_STATE_REFUND);
        paymentDao.updatePayTrans(payTrans);

        refundTrans.setStatus(REFUND_STATUS_SUCCESS);
        refundTrans.setRefundId(String.format("RF%d", snowFlakeIdWorker.nextId()));
        return refundDao.createRefund(refundTrans);
    }

    private RefundTrans refundFail(RefundTrans refundTrans) {
        refundTrans.setStatus(REFUND_STATUS_FAIL);
        refundTrans.setRefundId(String.format("RF%d", snowFlakeIdWorker.nextId()));
        return refundDao.createRefund(refundTrans);
    }
}
