//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.alipay.controller.vodto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询退款返回值
 * */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRefundRetDto {
    /*可选*/
    private String tradeNo; //支付宝交易号
    private String outTradeNo; // 创建交易传入的商户订单号
    private String outRequestNo; // 本笔退款对应的退款请求号
    private Double totalAmount; // 该笔退款所对应的交易的订单金额。单位：元。
    private Double refundAmount; // 本次退款请求，对应的退款金额。单位：元。
    private String refundStatus; //REFUND_SUCCESS, 为空说明失败
    /**
     * 退分账明细
     * */
    private List<RefundRoyaltyResult> refundRoyaltys;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime gmtRefundPay; // 需要在入参的query_options中指定"gmt_refund_pay"值时才返回该字段信息。
}

