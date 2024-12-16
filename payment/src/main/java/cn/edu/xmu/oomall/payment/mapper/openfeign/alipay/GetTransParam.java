//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 查询支付单参数
 */
@AllArgsConstructor
@Builder
public class GetTransParam {

    /**
     * 订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
     */
    private String out_trade_no;

    /**
     * 支付宝交易号，和商户订单号不能同时为空
     */
    private String trade_no;

}
