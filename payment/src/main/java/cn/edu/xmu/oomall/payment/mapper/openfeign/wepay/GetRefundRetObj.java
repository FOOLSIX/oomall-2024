package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询退款返回值
 * @author Wenbo Li
 * modified By Rui Li
 * task 2023-dgn1-005
 * */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRefundRetObj {
    private String refund_id; //微信支付退款单号
    private String out_refund_no;  //商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
    private String transaction_id; //微信支付交易订单号
    private String out_trade_no; //原支付交易对应的商户订单号
    private String channel = "ORIGINAL";
    private String user_received_account;
    private LocalDateTime success_time; //退款成功时间，当退款状态为退款成功时有返回。
    private LocalDateTime create_time; //退款受理时间
    /**
     * 退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往服务商平台-交易中心，手动处理此笔退款。
     * 枚举值：
     * SUCCESS：退款成功
     * CLOSED：退款关闭
     * PROCESSING：退款处理中
     * ABNORMAL：退款异常
     */
    private String status;
    private RefundRetAmount refundRetAmount;

}
