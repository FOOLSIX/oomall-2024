//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.*;

/**
 * 请求分账回退参数
 * @author Wenbo Li
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDivRefundParam {
    /**
     * 子商户号
     */
    private String sub_mchid;

    /**
     * 微信分账单号
     * divRefundTrans.divPayTrans.trans_no
     * */
    private String order_id;

    /*必选*/
    /**
     * 商户回退单号
     * divRefundTrans.out_no
     * */
    private String out_return_no;

    /**
     * 分账接受方账号（平台）
     * spMchid
     * */
    private String return_mchid;
    /**
     * 回退金额
     * divRefundTrans.amount
     * */
    private Long amount;

    private String description = "退款";
}
