package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rui Li
 * task 2023-dgn1-005
 */
@Data
@NoArgsConstructor
public class TransRetAmount {

    private Long total;//订单总金额，单位为分。
    private Long payer_total;//用户支付金额，单位为分。（指使用优惠券的情况下，这里等于总金额-优惠券金额）
}
