//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 请求退款返回值
 * */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRefundRetObj {
    /*必选*/
    private String refund_id; //微信支付退款单号
    private String out_refund_no; //商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母
    private String transaction_id; //微信支付交易订单号
    private String out_trade_no; //原支付交易对应的商户订单号
    private String channel; // ORIGINAL：原路退款;BALANCE：退回到余额;OTHER_BALANCE：原账户异常退到其他余额账户;OTHER_BANKCARD：原银行卡异常退到其他银行卡
    /**
     * 取当前退款单的退款入账方，有以下几种情况：
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号} 招商银行信用卡0403
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     */
    private String user_received_account;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime create_time; //退款受理时间
    private String status; // SUCCESS：退款成功;CLOSED：退款关闭;PROCESSING：退款处理中;ABNORMAL：退款异常
    private Amount amount;

    /*可选*/
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime success_time; //退款成功时间，当退款状态为退款成功时有返回。


    @Data
    @NoArgsConstructor
    public class Amount {
        /*必选*/
        private Long total; //订单总金额，单位为分
        private Long refund; //退款标价金额，单位为分，可以做部分退款
        private Long payer_total; //现金支付金额，单位为分，只能为整数
        private Long payer_refund; //退款给用户的金额，不包含所有优惠券金额
        /**
         * 去掉非充值代金券退款金额后的退款金额，单位为分，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
         */
        private Long settlement_refund;
        /**
         * 应结订单金额=订单金额-免充值代金券金额，应结订单金额<=订单金额，单位为分
         */
        private Long settlement_total;
        /**
         * 优惠退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠，单位为分
         */
        private Long discount_refund;
        private String currency="CNY";
        /*可选*/
        private Long refund_fee; //手续费退款金额，单位为分。
    }
}
