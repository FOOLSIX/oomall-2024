package cn.edu.xmu.oomall.payment.dao.channel.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求分账
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDivPayAdaptorVo {
    /**
     * 支付订单号
     */
    private String transactionId;
    /**
     * 分账单号
     */
    private String orderId;
}
