//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


/**
 * 查询订单返回值
 **/

@ToString
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetTransRetObj {
    /*必选*/
    private String trade_no;//支付宝交易号
    private String out_trade_no;//商家订单号
    private String buyer_logon_id; // 买家支付宝账号
    private String trade_status; // WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
    private Double total_amount; // yuan
}
