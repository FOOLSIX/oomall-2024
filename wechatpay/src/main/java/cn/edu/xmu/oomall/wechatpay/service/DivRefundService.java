package cn.edu.xmu.oomall.wechatpay.service;

import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.wechatpay.dao.DivPaymentDao;
import cn.edu.xmu.oomall.wechatpay.dao.DivReceiverDao;
import cn.edu.xmu.oomall.wechatpay.dao.DivRefundDao;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivPayTrans;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivReceiver;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivRefundTrans;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DivRefundService {

    private static final String DIVREFUND_STATUS_SUCCESS = "SUCCESS";
    private static final String DIVREFUND_STATUS_PROCESSING = "PROCESSING";
    private static final String DIVREFUND_STATUS_FAILED = "FAILED";
    private static final String RECEIVER_RESULT_SUCCESS = "SUCCESS";

    private final DivPaymentDao divPaymentDao;
    private final DivRefundDao divRefundDao;
    private final DivReceiverDao divReceiverDao;
    private final SnowFlakeIdWorker snowFlakeIdWorker;

    @Autowired
    public DivRefundService(DivPaymentDao divPaymentDao, DivRefundDao divRefundDao, DivReceiverDao divReceiverDao, SnowFlakeIdWorker snowFlakeIdWorker) {
        this.divPaymentDao = divPaymentDao;
        this.divRefundDao = divRefundDao;
        this.divReceiverDao = divReceiverDao;
        this.snowFlakeIdWorker = snowFlakeIdWorker;
    }

    public DivRefundTrans createDivRefund(DivRefundTrans divRefundTrans) {
        DivRefundTrans old = divRefundDao.getDivRefundTransByOutReturnNo(divRefundTrans.getOutReturnNo());
        if (old != null) {
            throw  new WeChatPayException(WeChatPayReturnNo.OUT_DIVREFUND_NO_USED);
        }

        String outOrderNo = divRefundTrans.getOutOrderNo();
        DivPayTrans divPayTrans = divPaymentDao.getDivPayTransByOutOrderNo(outOrderNo);
        if (divPayTrans == null) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        }

        // 确保回退过程的收账者存在
        String orderId = divPayTrans.getOrderId();
        Collection<DivReceiver> receivers = divReceiverDao.getDivReceiversByOrderId(orderId);
        Optional<DivReceiver> receiver = receivers.stream().filter(bo ->  bo.getResult().equals(RECEIVER_RESULT_SUCCESS)).findFirst();
        if (receiver.isEmpty()) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        }

        divRefundTrans.setOrderId(divPayTrans.getOrderId());

        return divRefundSuccess(divRefundTrans);
    }

    private DivRefundTrans divRefundSuccess(DivRefundTrans divRefundTrans) {
        divRefundTrans.setResult(DIVREFUND_STATUS_SUCCESS);
        divRefundTrans.setReturnId(String.format("RT%d", snowFlakeIdWorker.nextId()));
        divRefundTrans.setCreateTime(LocalDateTime.now());
        divRefundTrans.setFinishTime(LocalDateTime.now());
        return divRefundDao.createDivRefundTrans(divRefundTrans);
    }

}
