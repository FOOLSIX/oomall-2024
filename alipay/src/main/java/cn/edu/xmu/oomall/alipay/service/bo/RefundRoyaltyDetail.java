package cn.edu.xmu.oomall.alipay.service.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.controller.vodto.RoyaltyDetailInfoPojo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRefundRoyaltyDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@CopyFrom({AlipayRefundRoyaltyDetail.class, RoyaltyDetailInfoPojo.class})
public class RefundRoyaltyDetail {
    private String tradeNo;

    private String outTradeNo;

    /**
     * 退款单号
     */
    private String outRequestNo;
    /*必选*/
    /**
     * 退分账金额
     */
    private Integer refundAmount; //退分账金额。单位：分。
    private String resultCode = "SUCCESS"; // 退分账结果码

    /*可选*/
    private String royaltyType = "transfer";
    private String transOut; //转出人支付宝账号对应用户ID
    private String transIn; //转入人支付宝账号对应用户ID

    private LocalDateTime gmtRefundPay;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }
    @CopyFrom.Exclude({RoyaltyDetailInfoPojo.class})
    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getRoyaltyType() {
        return royaltyType;
    }

    public void setRoyaltyType(String royaltyType) {
        this.royaltyType = royaltyType;
    }

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut;
    }

    public String getTransIn() {
        return transIn;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn;
    }

    public LocalDateTime getGmtRefundPay() {
        return gmtRefundPay;
    }

    public void setGmtRefundPay(LocalDateTime gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
    }
}