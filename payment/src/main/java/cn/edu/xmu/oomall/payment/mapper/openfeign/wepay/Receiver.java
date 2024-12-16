//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receiver {
    /*必选*/
    private String type = "MERCHANT_ID"; //MERCHANT_ID, PERSONAL_OPENID
    /**
     * 分账接收方类型为MERCHANT_ID时，分账接收方账号为商户号
     * channel.mchid
     */
    private String account;
    /**
     * 分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额
     * divPayTrans.amount
     */
    private Long amount;

    /**
     * 分账的原因描述，分账账单中需要体现
     */
    private String description;
}
