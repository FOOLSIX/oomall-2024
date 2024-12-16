package cn.edu.xmu.oomall.alipay.controller.vodto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 发起退款返回值
 * @author Wenbo Li
 * */
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@ToString
@Data
public class PostRefundRetDto{
    /*必选*/
    private String tradeNo; //支付宝交易号
    private String outTradeNo; //商户订单号
    private String buyerLoginId; //用户的登录id
    private Double refundFee; // 退款总金额。单位：元。 指该笔交易累计已经退款成功的金额
    private String fundChange; //接口返回fund_change=Y为退款成功，fund_change=N或无此字段值返回时需通过退款查询接口进一步确认退款状态。
}
