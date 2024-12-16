package cn.edu.xmu.oomall.wechatpay.dao.bo;

import cn.edu.xmu.oomall.wechatpay.controller.vo.RefundAmountVo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author maguoqi
 * @date 2023/12/4
 * @check payerTotal, payerRefund, settlementRefund, discountRefund
 */
@NoArgsConstructor
@AllArgsConstructor
public class RefundAmount {
    private Integer total;
    private Integer refund;
    private Integer payerTotal;
    private Integer payerRefund;
    private Integer settlementTotal;
    private Integer settlementRefund;
    private Integer discountRefund;
    private final String currency = "CNY";

    public RefundAmount(RefundAmount refundAmount) {
        this.total = refundAmount.getTotal();
        this.refund = refundAmount.getRefund();
        this.payerTotal = refundAmount.getPayerTotal();
        this.payerRefund = refundAmount.getRefund();
        this.settlementRefund = refundAmount.getSettlementRefund();
        this.discountRefund = refundAmount.getDiscountRefund();
    }

    RefundAmount(RefundAmountVo refundAmountVo) {
        this.total = refundAmountVo.getTotal();
        this.payerTotal = refundAmountVo.getTotal();
        this.settlementTotal = refundAmountVo.getTotal();
        this.refund = refundAmountVo.getRefund();
        this.payerRefund = refundAmountVo.getRefund();
        this.settlementRefund = refundAmountVo.getRefund();
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRefund() {
        return refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    public Integer getPayerTotal() {
        return payerTotal;
    }

    public void setPayerTotal(Integer payerTotal) {
        this.payerTotal = payerTotal;
    }

    public Integer getPayerRefund() {
        return payerRefund;
    }

    public void setPayerRefund(Integer payerRefund) {
        this.payerRefund = payerRefund;
    }

    public Integer getSettlementTotal() {
        return settlementTotal;
    }

    public void setSettlementTotal(Integer settlementTotal) {
        this.settlementTotal = settlementTotal;
    }

    public Integer getSettlementRefund() {
        return settlementRefund;
    }

    public void setSettlementRefund(Integer settlementRefund) {
        this.settlementRefund = settlementRefund;
    }

    public Integer getDiscountRefund() {
        return discountRefund;
    }

    public void setDiscountRefund(Integer discountRefund) {
        this.discountRefund = discountRefund;
    }

    public String getCurrency() {
        return currency;
    }
}
