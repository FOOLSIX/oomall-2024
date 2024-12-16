//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.*;

/**
 * 下单参数
 * @author Wenbo Li
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPayParam {
    /**
     * 商户订单号
     * payTrans.out_no
     * */
    private String out_trade_no;
    /**
     * 订单描述
     * */
    private String subject = "订单支付";
    /**
     * 订单总金额
     * payTrans.amount
     * */
    private Double total_amount;

    public void setTotal_amount(Long amount){
        //单位为元
        this.total_amount = amount / 100.0;
    }

    /**
     * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
     */
    private String notify_url;
}
