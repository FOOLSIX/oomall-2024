//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.*;

/**
 * 发起退款参数
 * @author Wenbo Li
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRefundParam {
    /*必选*/
    /**
     * 子商户号
     */
    private String sub_mchid;

    /**
     * 微信订单号
     * refundTrans.payTrans.trans_no
     * */
    private String transaction_id;


    /*必选*/
    /**
     * 商户退款单号
     * refundTrans.out_no
     * */
    private String out_refund_no;

    private PostRefundAmount amount;

}
