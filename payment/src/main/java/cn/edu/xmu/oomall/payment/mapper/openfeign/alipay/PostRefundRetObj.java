package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 发起退款返回值
 * @author Wenbo Li
 * */
@ToString
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRefundRetObj{
    /*必选*/
    private String trade_no; //支付宝交易号
    private String out_trade_no; //商户订单号
    private String buyer_login_id; //用户的登录id
    private Double refund_fee; // 退款总金额。单位：元。 指该笔交易累计已经退款成功的金额
    private String fund_change; //接口返回fund_change=Y为退款成功，fund_change=N或无此字段值返回时需通过退款查询接口进一步确认退款状态。
}
