//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.*;

/**
 * 取消订单信息参数
 * @author Wenbo Li
 * */

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderParam {
    /*二选一*/
    /**
     * 支付宝交易号
     * payTrans.trans_no
     * */
    private String trade_no;

}
