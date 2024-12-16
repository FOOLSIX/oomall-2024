//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefundRoyaltyResult {
    /*必选*/
    /**
     * 退分账金额
     */
    private Double refund_amount; //退分账金额。单位：元。
    private String result_code = "SUCCESS"; // 退分账结果码

    /*可选*/
    private String royalty_type = "transfer";
    private String trans_out; //转出人支付宝账号对应用户ID
    private String trans_in; //转入人支付宝账号对应用户ID
}
