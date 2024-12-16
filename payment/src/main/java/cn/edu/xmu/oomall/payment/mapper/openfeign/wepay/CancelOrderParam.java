package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.*;

/**
 * 取消订单请求参数
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderParam {
    /*必选*/
    /**
     * 直连商户
     * payTrans.account.channel.SpMchid
     * */
    private String sp_mchid;

    /**
     * 子商户的商户号，
     */
    private String sub_mchid;
}
