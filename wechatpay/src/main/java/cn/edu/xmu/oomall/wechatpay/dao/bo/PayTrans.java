package cn.edu.xmu.oomall.wechatpay.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.wechatpay.controller.vo.PayTransVo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.PayTransPo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author maguoqi
 * @date 2023/12/4
 * @check transactionId, tradeState, tradeStateDesc, successTime, prepayId
 */
@NoArgsConstructor
@CopyFrom({PayTransPo.class})
public class PayTrans {

    @Getter
    @AllArgsConstructor
    public static class Payer {
        private String spOpenid;
    }

    @Getter
    @AllArgsConstructor
    public static class Amount {
        private Integer total;
    }

    private String spAppid;
    private String spMchid;
    private String subMchid;
    private String description;
    private String outTradeNo;
    private LocalDateTime timeExpire;
    private String notifyUrl;
    private Amount amount;
    private Payer payer;
    private String transactionId; // start with "T"
    private String tradeState;
    private String tradeStateDesc;
    private LocalDateTime successTime;
    private String prepayId;  // start with "P"

    public PayTrans(PayTransVo payTransVo) {
        this.spAppid = payTransVo.getSpAppid();
        this.spMchid = payTransVo.getSpMchid();
        this.subMchid = payTransVo.getSubMchid();
        this.description = payTransVo.getDescription();
        this.timeExpire = payTransVo.getTimeExpire();
        this.outTradeNo = payTransVo.getOutTradeNo();
        this.amount = new Amount(payTransVo.getAmount().getTotal());
        this.payer = new Payer(payTransVo.getPayer().getSpOpenid());
        this.notifyUrl = payTransVo.getNotifyUrl();
    }

    public PayTrans buildBo(PayTransPo payTransPo) {
        this.amount = new Amount(payTransPo.getAmountTotal());
        this.payer = new Payer(payTransPo.getPayerSpOpenid());
        return this;
    }

    public PayTransPo buildPo(PayTransPo payTransPo) {
        payTransPo.setAmountTotal(this.amount.total);
        payTransPo.setPayerSpOpenid(this.payer.spOpenid);
        return payTransPo;
    }

    public String getSpAppid() {
        return spAppid;
    }

    public void setSpAppid(String spAppid) {
        this.spAppid = spAppid;
    }

    public String getSpMchid() {
        return spMchid;
    }

    public void setSpMchid(String spMchid) {
        this.spMchid = spMchid;
    }

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public LocalDateTime getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(LocalDateTime timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc;
    }

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }
}
