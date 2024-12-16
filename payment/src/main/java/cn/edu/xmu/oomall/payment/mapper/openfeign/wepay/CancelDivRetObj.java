package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 解绑分账响应参数
 * @author huangzian
 * 2023-dgn1-006
 */
@NoArgsConstructor
@Data
public class CancelDivRetObj {
    /**
     * 子商户号
     */
    private String sub_mchid;
    /**
     * 分账接收方类型
     */
    private String type="MERCHANT_ID";
    /**
     * 分账接收方账号
     */
    private String account;
}
