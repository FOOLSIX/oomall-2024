//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.dao.channel.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求支付
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPayTransAdaptorVo {

    /**
     * 支付交易id
     * modified by Chihua Ying
     * task 2023-dgn1-004
     */
    private Long id;

    /**
     *
     * wepay：
     * alipay: out_trade_no 商户网站唯一订单号
     */
    private String outTradeNo;

    /**
     *
     * wepay： prepay_id 预支付单号
     * alipay: trade_no 该交易在支付宝系统中的交易流水号。最长64位。
     */
    private String prepayId;

    /**
     *
     * wepay：
     * alipay: total_amount 该笔订单的资金总额，单位为人民币（分）
     */
    private Long totalAmount;

    /**
     *
     * wepay：
     * alipay: seller_id 收款支付宝账号对应的支付宝唯一用户号
     */
    private String sellerId;

    /**
     *
     * wepay：
     * alipay: merchant_order_no 商户原始订单号
     */
    private String merchantOrderNo;

}
