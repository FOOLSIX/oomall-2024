package cn.edu.xmu.oomall.wechatpay.mapper.generator.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
@Repository
@CopyFrom({PayTrans.class})
public class PayTransPo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.sp_appid
     *
     * @mbg.generated
     */
    private String spAppid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.sp_mchid
     *
     * @mbg.generated
     */
    private String spMchid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.sub_mchid
     *
     * @mbg.generated
     */
    private String subMchid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.out_trade_no
     *
     * @mbg.generated
     */
    private String outTradeNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.time_expire
     *
     * @mbg.generated
     */
    private LocalDateTime timeExpire;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.payer_sp_openid
     *
     * @mbg.generated
     */
    private String payerSpOpenid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.amount_total
     *
     * @mbg.generated
     */
    private Integer amountTotal;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.transaction_id
     *
     * @mbg.generated
     */
    private String transactionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.trade_state
     *
     * @mbg.generated
     */
    private String tradeState;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.trade_state_desc
     *
     * @mbg.generated
     */
    private String tradeStateDesc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechatpay_pay_trans.success_time
     *
     * @mbg.generated
     */
    private LocalDateTime successTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.id
     *
     * @return the value of wechatpay_pay_trans.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.id
     *
     * @param id the value for wechatpay_pay_trans.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.sp_appid
     *
     * @return the value of wechatpay_pay_trans.sp_appid
     *
     * @mbg.generated
     */
    public String getSpAppid() {
        return spAppid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.sp_appid
     *
     * @param spAppid the value for wechatpay_pay_trans.sp_appid
     *
     * @mbg.generated
     */
    public void setSpAppid(String spAppid) {
        this.spAppid = spAppid == null ? null : spAppid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.sp_mchid
     *
     * @return the value of wechatpay_pay_trans.sp_mchid
     *
     * @mbg.generated
     */
    public String getSpMchid() {
        return spMchid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.sp_mchid
     *
     * @param spMchid the value for wechatpay_pay_trans.sp_mchid
     *
     * @mbg.generated
     */
    public void setSpMchid(String spMchid) {
        this.spMchid = spMchid == null ? null : spMchid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.sub_mchid
     *
     * @return the value of wechatpay_pay_trans.sub_mchid
     *
     * @mbg.generated
     */
    public String getSubMchid() {
        return subMchid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.sub_mchid
     *
     * @param subMchid the value for wechatpay_pay_trans.sub_mchid
     *
     * @mbg.generated
     */
    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid == null ? null : subMchid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.description
     *
     * @return the value of wechatpay_pay_trans.description
     *
     * @mbg.generated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.description
     *
     * @param description the value for wechatpay_pay_trans.description
     *
     * @mbg.generated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.out_trade_no
     *
     * @return the value of wechatpay_pay_trans.out_trade_no
     *
     * @mbg.generated
     */
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.out_trade_no
     *
     * @param outTradeNo the value for wechatpay_pay_trans.out_trade_no
     *
     * @mbg.generated
     */
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.time_expire
     *
     * @return the value of wechatpay_pay_trans.time_expire
     *
     * @mbg.generated
     */
    public LocalDateTime getTimeExpire() {
        return timeExpire;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.time_expire
     *
     * @param timeExpire the value for wechatpay_pay_trans.time_expire
     *
     * @mbg.generated
     */
    public void setTimeExpire(LocalDateTime timeExpire) {
        this.timeExpire = timeExpire;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.payer_sp_openid
     *
     * @return the value of wechatpay_pay_trans.payer_sp_openid
     *
     * @mbg.generated
     */
    public String getPayerSpOpenid() {
        return payerSpOpenid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.payer_sp_openid
     *
     * @param payerSpOpenid the value for wechatpay_pay_trans.payer_sp_openid
     *
     * @mbg.generated
     */
    public void setPayerSpOpenid(String payerSpOpenid) {
        this.payerSpOpenid = payerSpOpenid == null ? null : payerSpOpenid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.amount_total
     *
     * @return the value of wechatpay_pay_trans.amount_total
     *
     * @mbg.generated
     */
    public Integer getAmountTotal() {
        return amountTotal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.amount_total
     *
     * @param amountTotal the value for wechatpay_pay_trans.amount_total
     *
     * @mbg.generated
     */
    public void setAmountTotal(Integer amountTotal) {
        this.amountTotal = amountTotal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.transaction_id
     *
     * @return the value of wechatpay_pay_trans.transaction_id
     *
     * @mbg.generated
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.transaction_id
     *
     * @param transactionId the value for wechatpay_pay_trans.transaction_id
     *
     * @mbg.generated
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.trade_state
     *
     * @return the value of wechatpay_pay_trans.trade_state
     *
     * @mbg.generated
     */
    public String getTradeState() {
        return tradeState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.trade_state
     *
     * @param tradeState the value for wechatpay_pay_trans.trade_state
     *
     * @mbg.generated
     */
    public void setTradeState(String tradeState) {
        this.tradeState = tradeState == null ? null : tradeState.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.trade_state_desc
     *
     * @return the value of wechatpay_pay_trans.trade_state_desc
     *
     * @mbg.generated
     */
    public String getTradeStateDesc() {
        return tradeStateDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.trade_state_desc
     *
     * @param tradeStateDesc the value for wechatpay_pay_trans.trade_state_desc
     *
     * @mbg.generated
     */
    public void setTradeStateDesc(String tradeStateDesc) {
        this.tradeStateDesc = tradeStateDesc == null ? null : tradeStateDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechatpay_pay_trans.success_time
     *
     * @return the value of wechatpay_pay_trans.success_time
     *
     * @mbg.generated
     */
    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechatpay_pay_trans.success_time
     *
     * @param successTime the value for wechatpay_pay_trans.success_time
     *
     * @mbg.generated
     */
    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }
}