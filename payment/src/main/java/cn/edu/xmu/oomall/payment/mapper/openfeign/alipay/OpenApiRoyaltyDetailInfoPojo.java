//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenApiRoyaltyDetailInfoPojo {
    /*必选*/
    /**
     * 收入方支付宝账号(平台支付宝账号)
     * channel.spmchid
     */
    private String trans_in;

    /*可选*/
    private String royalty_type = "transfer";
    /**
     * 支出方账号
     * channel.submchid
     */
    private String trans_out;
    private String trans_in_type = "userId";
    private String trans_out_type = "userId";
    /**
     * 分账的金额，单位为元
     */
    private Double amount;
}
