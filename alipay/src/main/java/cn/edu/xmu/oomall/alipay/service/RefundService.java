package cn.edu.xmu.oomall.alipay.service;


import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.alipay.controller.vodto.PostRefundVo;
import cn.edu.xmu.oomall.alipay.dao.*;
import cn.edu.xmu.oomall.alipay.exception.AlipayBusinessException;
import cn.edu.xmu.oomall.alipay.service.bo.Payment;
import cn.edu.xmu.oomall.alipay.service.bo.Refund;
import cn.edu.xmu.oomall.alipay.service.bo.RefundRoyaltyDetail;
import cn.edu.xmu.oomall.alipay.util.AlipayReturnNo;
import cn.edu.xmu.oomall.alipay.util.ParseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * @author lianshuquan
 * @date 2023/12/13
 */
@Service
public class RefundService {
    private Logger logger = LoggerFactory.getLogger(AlipayService.class);

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private RefundDao refundDao;


    private void refundWithoutDivFail(Refund refund) {
        throw new AlipayBusinessException(AlipayReturnNo.SYSTEM_ERROR);
    }

    private void refundWithoutDivSuccess(Refund refund) {
        refund.setRefundStatus(0);
        refund.setGmtRefundPay(LocalDateTime.now());
        refundDao.insertRefund(refund);
    }

    private Payment setRefundByPayment(Refund refund){
        Payment payment = paymentDao.selectPaymentByTradeNoOrOutTradeNo(refund.getTradeNo(),refund.getOutTradeNo());
        if(payment==null)
            throw new AlipayBusinessException(AlipayReturnNo.TRADE_NOT_EXIST);
        refund.setTotalAmount(payment.getTotalAmount());
        refund.setTradeNo(payment.getTradeNo());
        logger.info("payment in refundWithoutDiv:"+ JacksonUtil.toJson(payment));
        logger.info("refund in refundWithoutDiv:"+JacksonUtil.toJson(refund));
        return payment;
    }

    public void refundWithoutDiv(String shopAccount, Refund refund) {
        Payment payment = setRefundByPayment(refund);
        if(refund.getRefundAmount()==901){
            //退款失败
            refundWithoutDivFail(refund);
        }else{
            if(refund.getRefundAmount()>payment.getTotalAmount())
                throw new AlipayBusinessException(AlipayReturnNo.REFUND_AMT_NOT_EQUAL_TOTAL);
            refundWithoutDivSuccess(refund);
        }

    }



    public void refundDiv(String shopAccount, Refund refund) {
        Payment payment = setRefundByPayment(refund);
        if(refund.getRefundRoyaltyDetails()!=null){
            for(RefundRoyaltyDetail refundRoyaltyDetail:refund.getRefundRoyaltyDetails()){
                refundRoyaltyDetail.setOutRequestNo(refund.getOutRequestNo());
                refundRoyaltyDetail.setTradeNo(refund.getTradeNo());
                refundRoyaltyDetail.setOutTradeNo(refund.getOutTradeNo());
                refundRoyaltyDetail.setResultCode("SUCCESS");
                refundRoyaltyDetail.setGmtRefundPay(LocalDateTime.now());
                refundDao.insertRefundRoyaltyDetail(refundRoyaltyDetail);
            }
        }
        Refund existedRefund = refundDao.retrieveByOutTradeNo(refund.getOutTradeNo());
        if(existedRefund!=null){
            refund = existedRefund;
        }
    }

    public Refund retrieveByOutTradeNo(String outTradeNo) {
        Refund refund = refundDao.retrieveByOutTradeNo(outTradeNo);
        if(refund==null) throw new AlipayBusinessException(AlipayReturnNo.TRADE_NOT_EXIST);
        return refund;
    }
}
