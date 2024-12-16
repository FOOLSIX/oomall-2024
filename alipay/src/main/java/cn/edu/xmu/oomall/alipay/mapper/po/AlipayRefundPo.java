package cn.edu.xmu.oomall.alipay.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.service.bo.Refund;

import java.time.LocalDateTime;

@CopyFrom({Refund.class})
public class AlipayRefundPo {
    private Long id;

    private String outRequestNo;

    private String outTradeNo;

    private String tradeNo;

    private Integer totalAmount;

    private Integer refundAmount;

    private LocalDateTime gmtRefundPay;

    private Integer refundStatus;

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

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }

    public LocalDateTime getGmtRefundPay() {
        return gmtRefundPay;
    }

    public void setGmtRefundPay(LocalDateTime gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }
}