package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rui Li
 * task 2023-dgn1-005
 */
@Data
@NoArgsConstructor
public class RefundRetFrom {
    String account= "AVAILABLE";
    Long amount; //对应账户出资金额
}
