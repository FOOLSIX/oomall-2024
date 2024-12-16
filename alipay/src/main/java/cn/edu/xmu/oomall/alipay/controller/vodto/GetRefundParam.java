package cn.edu.xmu.oomall.alipay.controller.vodto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退款(退分账)查询参数
 * */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetRefundParam {
    /**
     * 商户订单号。 订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
     * refundTrans.payTrans.out_no
     * divRefundTrans.refundTrans.payTrans.out_no
     * */
    private String outTradeNo;
    private String tradeNo;

    private String[] queryOptions = new String[]{"gmt_refund_pay"}; //退款执行成功的时间；
}
