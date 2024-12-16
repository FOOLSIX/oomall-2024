package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rui Li
 * task 2023-dgn1-005
 */
@Data
@NoArgsConstructor
public class TransRetPayer {
    /**
     *  用户在服务商appid下的唯一标识。
     */
    private String sp_openid;
}
