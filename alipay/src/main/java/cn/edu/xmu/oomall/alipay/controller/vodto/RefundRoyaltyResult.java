//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.alipay.controller.vodto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundRoyaltyResult {
    /*必选*/
    /**
     * 退分账金额
     */
    private Double refundAmount; //退分账金额。单位：元。
    private String resultCode = "SUCCESS"; // 退分账结果码

    /*可选*/
    private String royaltyType = "transfer";
    private String transOut; //转出人支付宝账号对应用户ID
    private String transIn; //转入人支付宝账号对应用户ID
}
