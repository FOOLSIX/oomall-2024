//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.alipay.controller.vodto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 取消订单返回值
 * @author Wenbo Li
 * */

@ToString
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelOrderDto{
    /*可选*/
    private String tradeNo; //支付宝交易号
    private String outTradeNo; //创建交易传入的商户订单号

    public CancelOrderDto(String tradeNo, String outTradeNo) {
        this.tradeNo = tradeNo;
        this.outTradeNo = outTradeNo;
    }
}
