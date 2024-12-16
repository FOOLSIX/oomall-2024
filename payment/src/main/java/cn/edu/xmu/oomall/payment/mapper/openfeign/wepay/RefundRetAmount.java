package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rui Li
 * task 2023-dgn1-005
 */
@Data
@NoArgsConstructor
public class RefundRetAmount {
    Long total; //订单总金额，单位为分
    Long refund; //退款标价金额，单位为分，可以做部分退款
    Long payer_total; //现金支付金额，单位为分，只能为整数
    Long payer_refund; //退款给用户的金额，不包含所有优惠券金额
    Long settlement_refund; //去掉非充值代金券退款金额后的退款金额，单位为分，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
    Long settlement_total; //应结订单金额=订单金额-免充值代金券金额，应结订单金额<=订单金额，单位为分
    Long discount_refund; //优惠退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠，单位为分
    String currency = "CNY";
    RefundRetFrom refundRetFrom; //退款出资的账户类型及金额信息
}
