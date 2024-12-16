package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询订单返回值
 * @author Wenbo Li
 * modified By Rui Li
 * task 2023-dgn1-005
 * */

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTransRetObj {
    /*必选*/
    private String sp_appid; //服务商申请的公众号或移动应用appid。
    private String sp_mchid; //服务商户号
    private String sub_mchid; //子商户的商户号
    private String out_trade_no; //商户系统内部订单号
    private String transaction_id; //微信支付系统生成的订单号
    private String trade_type = "JSAPI"; // 交易类型
    /**
     * 交易状态，枚举值：
     * SUCCESS：支付成功
     * REFUND：转入退款
     * NOTPAY：未支付
     * CLOSED：已关闭
     * REVOKED：已撤销（仅付款码支付会返回）
     * USERPAYING：用户支付中（仅付款码支付会返回）
     * PAYERROR：支付失败（仅付款码支付会返回）
     */
    private String trade_state;
    private String trade_state_desc; // 交易状态描述
    /*可选*/
    private String success_time;
    TransRetPayer transRetPayer; //支付者信息
    TransRetAmount transRetAmount; //订单金额信息，当支付成功时返回该字段。

}
