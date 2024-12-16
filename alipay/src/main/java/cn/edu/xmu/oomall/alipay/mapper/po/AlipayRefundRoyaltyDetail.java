package cn.edu.xmu.oomall.alipay.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.service.bo.RefundRoyaltyDetail;

import java.time.LocalDateTime;

@CopyFrom({RefundRoyaltyDetail.class})
public class AlipayRefundRoyaltyDetail {
    private Long id;

    private String outRequestNo;

    private String outTradeNo;

    private String tradeNo;

    private String transIn;

    private String transOut;

    private Integer refundAmount;

    private String royaltyType;

    private String resultCode;

    private LocalDateTime gmtRefundPay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo == null ? null : outRequestNo.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getTransIn() {
        return transIn;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn == null ? null : transIn.trim();
    }

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut == null ? null : transOut.trim();
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRoyaltyType() {
        return royaltyType;
    }

    public void setRoyaltyType(String royaltyType) {
        this.royaltyType = royaltyType == null ? null : royaltyType.trim();
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode == null ? null : resultCode.trim();
    }

    public LocalDateTime getGmtRefundPay() {
        return gmtRefundPay;
    }

    public void setGmtRefundPay(LocalDateTime gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
    }
}