//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 预下单下单参数
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPayParam {
    /*必选*/
    /**
     * 服务商应用ID
     * payTrans.account.channel.appid
     */
    private String sp_appid;

    /**
     * 服务商户号
     * payTrans.account.channel.SpMchid
     */
    private String sp_mchid;

    /**
     * 子商户号
     */
    private String sub_mchid;

    /**
     * 商品描述
     * any
     */
    private String description;

    /**
     * 商户订单号
     * payTrans.out_trade_no
     */
    private String out_trade_no;

    /**
     * 通知地址
     * payTrans.account.channel.notifyUrl
     */
    private String notify_url;

    /**
     * 订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE
     */
    private LocalDateTime time_expire;

    /**
     *  订单金额
     */
    private PayAmount amount;


    /**
     * 支付者
     * @PayTrans: payTrans.getSpOpenid()
     */
    private Payer payer;
}

