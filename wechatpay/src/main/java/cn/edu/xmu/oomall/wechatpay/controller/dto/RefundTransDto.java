package cn.edu.xmu.oomall.wechatpay.controller.dto;

import cn.edu.xmu.oomall.wechatpay.dao.bo.RefundAmount;
import cn.edu.xmu.oomall.wechatpay.dao.bo.RefundTrans;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundTransDto {

    private String refundId;

    private String outRefundNo;

    private String transactionId;

    private String outTradeNo;

    private final String channel = "ORIGINAL";

    private String userReceivedAccount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String status;

    private RefundAmount amount;

    public RefundTransDto(RefundTrans refundTrans) {
        this.refundId = refundTrans.getRefundId();
        this.outRefundNo = refundTrans.getOutRefundNo();
        this.transactionId = refundTrans.getTransactionId();
        this.outTradeNo = refundTrans.getOutTradeNo();
        this.userReceivedAccount = refundTrans.getUserReceivedAccount();
        this.createTime = refundTrans.getCreateTime();
        this.status = refundTrans.getStatus();
        this.amount = new RefundAmount(refundTrans.getAmount());
    }
}
