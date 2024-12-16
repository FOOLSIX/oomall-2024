//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 取消订单返回值
 * @author Wenbo Li
 * */

@ToString
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelOrderRetObj{
    /*可选*/
    private String trade_no; //支付宝交易号
    private String out_trade_no; //创建交易传入的商户订单号
}
