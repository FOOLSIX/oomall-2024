//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.*;

import java.util.List;

/**
 * 发起分账参数
 * @author Wenbo Li
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDivPayParam {
    /*必选*/
    /**
     * 微信分配的服务商appid
     * channel.appid
     * */
    private String appid;

    /**
     * 微信支付分配的子商户号，即分账的出资商户号。
     */
    private String sub_mchild;

    /**
     * 微信支付订单号
     * divPayTrans.payTrans.trans_no
     * */
    private String transaction_id;

    /**
     * 服务商系统内部的分账单号，在服务商系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@
     * divPayTrans.out_no
     */
    private String out_order_no;

    /**
     * 账户接收方
     * */
    private List<Receiver> receivers;

    /**
     * 是否解冻剩余未分账资金
     * 1、如果为true，该笔订单剩余未分账的金额会解冻回分账方商户；
     * 2、如果为false，该笔订单剩余未分账的金额不会解冻回分账方商户，可以对该笔订单再次进行分账。
     * */
    private boolean unfreeze_unsplit=false;
}

