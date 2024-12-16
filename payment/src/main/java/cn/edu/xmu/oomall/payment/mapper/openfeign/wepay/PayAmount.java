//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayAmount {
    /*必选*/
    /**
     * 总金额
     * payTrans.amount
     */
    private Long total;
}
