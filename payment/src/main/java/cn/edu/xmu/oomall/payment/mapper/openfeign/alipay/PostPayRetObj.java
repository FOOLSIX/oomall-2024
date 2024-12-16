//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 下单返回值
 * */
@ToString
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostPayRetObj{
    /*必选*/
    /**
     * 商家订单号
     * */
    private String out_trade_no;
    /**
     * 支付宝交易号
     * */
    private String trade_no;
    /**
     * 交易总金额（元）
     * */
    private Long total_amount;
    /**
     * 收款方支付宝账号
     * */
    private String seller_id;
    /**
     * 商户原始订单号
     * */
    private String merchant_order_no;
}
