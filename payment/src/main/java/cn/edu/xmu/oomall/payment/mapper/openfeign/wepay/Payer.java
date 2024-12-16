//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payer {
    /**
     * 用户服务标识
     * payTrans.sp_openid
     */
    private String sp_openid;
}
