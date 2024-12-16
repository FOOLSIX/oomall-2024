package cn.edu.xmu.oomall.payment.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
public class AlipayNotifyDto {

    @NotNull
    @JsonProperty(value = "app_id")
    private String appId;

    @NotNull
    @JsonProperty(value = "trade_no")
    private String tradeNo;

    @NotNull
    @JsonProperty(value = "out_trade_no")
    private String outTradeNo;

    @NotNull
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value = "gmt_payment")
    private LocalDateTime gmtPayment;

    @NotNull
    @JsonProperty(value = "trade_status")
    private String tradeStatus;

    @NotNull
    @JsonProperty(value = "recipet_amount")
    private Long receiptAmount;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public LocalDateTime getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(LocalDateTime gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Long getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(Long receiptAmount) {
        this.receiptAmount = receiptAmount;
    }
}
