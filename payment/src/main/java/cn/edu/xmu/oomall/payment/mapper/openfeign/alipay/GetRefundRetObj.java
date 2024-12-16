//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询退款返回值
 * */
@ToString
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRefundRetObj {
    /*可选*/
    private String trade_no; //支付宝交易号
    private String out_trade_no; // 创建交易传入的商户订单号
    private String out_request_no; // 本笔退款对应的退款请求号
    private Double total_amount; // 该笔退款所对应的交易的订单金额。单位：元。
    private Double refund_amount; // 本次退款请求，对应的退款金额。单位：元。
    private String refund_status; //REFUND_SUCCESS, 为空说明失败
    /**
     * 退分账明细
     * */
    private List<RefundRoyaltyResult> refund_royaltys;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime gmt_refund_pay; // 需要在入参的query_options中指定"gmt_refund_pay"值时才返回该字段信息。
}

