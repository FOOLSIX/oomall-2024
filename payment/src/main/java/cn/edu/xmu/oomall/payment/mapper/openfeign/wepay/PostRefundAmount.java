//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRefundAmount {
    /**
     * 原订单金额
     * refundTrans.payTrans.amount
     */
    private Long total;
    /**
     * 货币类型
     * "CNY"
     */
    private String currency = "CNY";
    /**
     * 退款金额
     * refundTrans.amount
     */
    private Long refund;
}
