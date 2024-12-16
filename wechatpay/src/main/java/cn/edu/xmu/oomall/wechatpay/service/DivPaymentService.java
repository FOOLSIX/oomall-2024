package cn.edu.xmu.oomall.wechatpay.service;

import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.wechatpay.dao.DivPaymentDao;
import cn.edu.xmu.oomall.wechatpay.dao.DivReceiverDao;
import cn.edu.xmu.oomall.wechatpay.dao.PaymentDao;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivPayTrans;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivReceiver;
import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DivPaymentService {

    private static final String DIVPAY_STATE_FINISHED = "FINISHED";
    private static final String DIVPAY_STATE_PROCESSING = "PROCESSING";
    private static final String TRADE_STATE_FAIL = "NOTPAY";
    private static final String TRADE_STATE_CLOSED = "CLOSED";
    private static final String RECEIVER_RESULT_SUCCESS = "SUCCESS";
    private static final String RECEIVER_RESULT_PENDING = "PENDING";

    private final DivPaymentDao divPaymentDao;

    private final PaymentDao paymentDao;

    private final DivReceiverDao divReceiverDao;

    private final SnowFlakeIdWorker snowFlakeIdWorker;

    @Autowired
    public DivPaymentService(DivPaymentDao divPaymentDao, PaymentDao paymentDao, DivReceiverDao divReceiverDao, SnowFlakeIdWorker snowFlakeIdWorker) {
        this.divPaymentDao = divPaymentDao;
        this.paymentDao = paymentDao;
        this.divReceiverDao = divReceiverDao;
        this.snowFlakeIdWorker = snowFlakeIdWorker;
    }

    private DivReceiver buildReceiverDto(DivReceiver divReceiver, boolean fail) {
        DivReceiver newDivReceiver = new DivReceiver(divReceiver);
        newDivReceiver.setCreateTime(LocalDateTime.now());
        newDivReceiver.setDetailId(String.format("D%d", snowFlakeIdWorker.nextId()));
        newDivReceiver.setResult(fail ? RECEIVER_RESULT_PENDING : RECEIVER_RESULT_SUCCESS);
        newDivReceiver.setFailReason(null);
        newDivReceiver.setFinishTime(LocalDateTime.now());
        return newDivReceiver;
    }

    public DivPayTrans createDivPayTrans(DivPayTrans divPayTrans) {

        // 查找outOrderNo是否重复
        DivPayTrans old = divPaymentDao.getDivPayTransByOutOrderNo(divPayTrans.getOutOrderNo());
        if (old != null) {
            throw new WeChatPayException(WeChatPayReturnNo.OUT_DIVPAY_NO_USED);
        }

        String transId = divPayTrans.getTransactionId();
        PayTrans payTrans = paymentDao.getPayTransByTransId(transId);
        if (payTrans == null) {
            throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
        } else if (payTrans.getTradeState().equals(TRADE_STATE_CLOSED)) {
            throw new WeChatPayException(WeChatPayReturnNo.ORDER_CLOSED);
        } else if (payTrans.getTradeState().equals(TRADE_STATE_FAIL)){
            throw new WeChatPayException(WeChatPayReturnNo.ORDER_FAIL);
        }

        List<DivPayTrans> oldDivPayTransList = divPaymentDao.getDivPayTransByTransId(transId);
        int totalAmount;
        if (oldDivPayTransList != null) {
            List<DivReceiver> oldReceivers = oldDivPayTransList.stream().flatMap(bo -> divReceiverDao.getDivReceiversByOrderId(bo.getOrderId()).stream()).toList();
            Collection<DivReceiver> newReceivers = divPayTrans.getReceivers();

            int minAmount = newReceivers.stream().mapToInt(DivReceiver::getAmount).min().orElse(0);
            if (minAmount <= 0) {
                throw new WeChatPayException(WeChatPayReturnNo.DIV_AMOUNT_ERROR);
            }

            totalAmount = oldReceivers.stream().mapToInt(DivReceiver::getAmount).sum()
                          + newReceivers.stream().mapToInt(DivReceiver::getAmount).sum();

        } else { // 以前没有分账过
            Collection<DivReceiver> newReceivers = divPayTrans.getReceivers();
            totalAmount = newReceivers.stream().mapToInt(DivReceiver::getAmount).sum();
        }

        if (totalAmount > payTrans.getAmount().getTotal()) {
            throw new WeChatPayException(WeChatPayReturnNo.NOT_ENOUGH);
        }

        return divPaySuccess(divPayTrans);
    }

    private DivPayTrans divPaySuccess(DivPayTrans divPayTrans) {
        divPayTrans.setReceivers(divPayTrans.getReceivers().stream().map(r -> buildReceiverDto(r, false)).collect(Collectors.toCollection(ArrayList::new)));
        divPayTrans.setState(DIVPAY_STATE_FINISHED);
        divPayTrans.setSuccessTime(LocalDateTime.now());
        divPayTrans.setOrderId(String.format("O%d", snowFlakeIdWorker.nextId()));
        return divPaymentDao.createDivPayTrans(divPayTrans);
    }

}
