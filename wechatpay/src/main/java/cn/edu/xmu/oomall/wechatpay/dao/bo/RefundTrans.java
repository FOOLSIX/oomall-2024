package cn.edu.xmu.oomall.wechatpay.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.wechatpay.controller.vo.RefundTransVo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.RefundTransPo;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author maguoqi
 * @date 2023/12/4
 * @check refundId, outTradeNo, userReceivedAccount, createTime, status
 */
@NoArgsConstructor
@CopyFrom({RefundTransPo.class})
public class RefundTrans {

    private String subMchid;
    private String transactionId;
    private String outRefundNo;
    private RefundAmount amount;
    private String refundId;
    private String outTradeNo;
    private final String channel = "ORIGINAL";
    private String userReceivedAccount;
    private LocalDateTime createTime;
    private String status;

    public RefundTrans(RefundTransVo refundTransVo) {
        this.subMchid = refundTransVo.getSubMchid();
        this.outRefundNo = refundTransVo.getOutRefundNo();
        this.transactionId = refundTransVo.getTransactionId();
        this.amount = new RefundAmount(refundTransVo.getAmount());
    }

    public RefundTrans buildBo(RefundTransPo refundTransPo) {
        this.amount = new RefundAmount(
                refundTransPo.getAmountTotal(),
                refundTransPo.getAmountRefund(),
                refundTransPo.getAmountPayerTotal(),
                refundTransPo.getAmountPayerRefund(),
                refundTransPo.getAmountSettlementTotal(),
                refundTransPo.getAmountSettlementRefund(),
                refundTransPo.getAmountDiscountRefund()
        );
        return this;
    }

    public RefundTransPo buildPo(RefundTransPo refundTransPo) {
        refundTransPo.setAmountTotal(this.amount.getTotal());
        refundTransPo.setAmountRefund(this.amount.getRefund());
        refundTransPo.setAmountPayerTotal(this.amount.getPayerTotal());
        refundTransPo.setAmountPayerRefund(this.amount.getPayerRefund());
        refundTransPo.setAmountSettlementRefund(this.amount.getSettlementRefund());
        refundTransPo.setAmountSettlementTotal(this.amount.getSettlementTotal());
        refundTransPo.setAmountDiscountRefund(this.amount.getDiscountRefund());
        return refundTransPo;
    }

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public RefundAmount getAmount() {
        return amount;
    }

    public void setAmount(RefundAmount amount) {
        this.amount = amount;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getChannel() {
        return channel;
    }

    public String getUserReceivedAccount() {
        return userReceivedAccount;
    }

    public void setUserReceivedAccount(String userReceivedAccount) {
        this.userReceivedAccount = userReceivedAccount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
