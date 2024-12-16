package cn.edu.xmu.oomall.wechatpay.service;

import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.wechatpay.dao.PaymentDao;
import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PaymentService {

    private static final String TRADE_STATE_SUCCESS = "SUCCESS";
    private static final String TRADE_STATE_REFUND = "REFUND";
    private static final String TRADE_STATE_FAIL = "NOTPAY";
    private static final String TRADE_STATE_CLOSED = "CLOSED";

    private static final HashMap<String, String> stateDesc = new HashMap<>() {{
        put(TRADE_STATE_SUCCESS, "支付成功");
        put(TRADE_STATE_FAIL, "支付失败");
        put(TRADE_STATE_CLOSED, "支付取消");
        put(TRADE_STATE_REFUND, "转入退款");
    }};

    private final PaymentDao paymentDao;

    private final SnowFlakeIdWorker snowFlakeIdWorker;

    @Autowired
    public PaymentService(PaymentDao paymentDao, SnowFlakeIdWorker snowFlakeIdWorker) {
        this.paymentDao = paymentDao;
        this.snowFlakeIdWorker = snowFlakeIdWorker;
    }

    public PayTrans createPayTrans(PayTrans payTrans) {
        PayTrans oldPayTrans = paymentDao.getPayTransByOutNo(payTrans.getOutTradeNo());
        if (oldPayTrans != null) {
            if (oldPayTrans.getTradeState().equals(TRADE_STATE_SUCCESS)) {
                throw new WeChatPayException(WeChatPayReturnNo.ORDER_PAID);
            }
            if (oldPayTrans.getTradeState().equals(TRADE_STATE_CLOSED)) {
                throw new WeChatPayException(WeChatPayReturnNo.ORDER_CLOSED);
            }
            throw new WeChatPayException(WeChatPayReturnNo.OUT_TRADE_NO_USED);
        }

        boolean fail = payTrans.getAmount().getTotal().equals(9901);  // API测试

        return fail ? payFail(payTrans) : paySuccess(payTrans);
    }

    public void closePayTrans(String spMchid, String subMchid, String outTradeNo) {
        PayTrans payTrans = paymentDao.getPayTransByOutNo(outTradeNo);
        if (payTrans == null) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        } else if (payTrans.getTradeState().equals(TRADE_STATE_CLOSED)) {
            throw new WeChatPayException(WeChatPayReturnNo.ORDER_CLOSED);
        } else if (payTrans.getTradeState().equals(TRADE_STATE_SUCCESS) || payTrans.getTradeState().equals(TRADE_STATE_REFUND)) {
            throw new WeChatPayException(WeChatPayReturnNo.ORDER_PAID);
        } else if (!payTrans.getSpMchid().equals(spMchid) || !payTrans.getSubMchid().equals(subMchid)) {
            throw new WeChatPayException(WeChatPayReturnNo.NO_AUTH);
        } // 只有支付失败才能取消

        payTrans.setTradeState(TRADE_STATE_CLOSED);
        payTrans.setTradeStateDesc(stateDesc.get(TRADE_STATE_CLOSED));

        paymentDao.updatePayTrans(payTrans);
    }

    public PayTrans getPayTransByOutNo(String spMchid, String subMchid, String outTradeNo) {
        PayTrans payTrans = paymentDao.getPayTransByOutNo(outTradeNo);
        if (payTrans == null) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        } else if (!payTrans.getSpMchid().equals(spMchid) || !payTrans.getSubMchid().equals(subMchid)) {
            throw new WeChatPayException(WeChatPayReturnNo.NO_AUTH);
        }
        return payTrans;
    }

    public PayTrans getPayTransByTransId(String spMchid, String subMchid, String transactionId) {
        PayTrans payTrans = paymentDao.getPayTransByTransId(transactionId);
        if (payTrans == null) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        } else if (!payTrans.getSpMchid().equals(spMchid) || !payTrans.getSubMchid().equals(subMchid)) {
            throw new WeChatPayException(WeChatPayReturnNo.NO_AUTH);
        }
        return payTrans;
    }

    private PayTrans paySuccess(PayTrans payTrans) {
        payTrans.setTransactionId(String.format("T%d", snowFlakeIdWorker.nextId()));
        payTrans.setTradeState(TRADE_STATE_SUCCESS);
        payTrans.setTradeStateDesc(stateDesc.get(TRADE_STATE_SUCCESS));
        payTrans.setSuccessTime(LocalDateTime.now());
        payTrans.setPrepayId(String.format("P%d", snowFlakeIdWorker.nextId()));
        if (!payTrans.getAmount().getTotal().equals(9902)) {  // API测试
            paymentDao.notifyPayment(payTrans);
        }
        return paymentDao.createPayTrans(payTrans);
    }

    private PayTrans payFail(PayTrans payTrans) {
        payTrans.setTransactionId(String.format("T%d", snowFlakeIdWorker.nextId()));
        payTrans.setTradeState(TRADE_STATE_FAIL);
        payTrans.setTradeState(stateDesc.get(TRADE_STATE_FAIL));
        paymentDao.notifyPayment(payTrans);
        return paymentDao.createPayTrans(payTrans);
    }
}
