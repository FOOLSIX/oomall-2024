package cn.edu.xmu.oomall.wechatpay.mapper.notify.po;

import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaymentNotifyPo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class WePayResource {
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Payer {
            @JsonProperty(value = "sp_openid")
            private String spOpenId;
        };

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Amount {
            @JsonProperty(value = "total")
            private Long total;

            @JsonProperty(value = "payer_total")
            private Long payerTotal;
        };

        @JsonProperty(value = "sp_appid")
        private String spAppId;

        @JsonProperty(value = "sp_mchid")
        private String spMchId;

        @JsonProperty(value = "sub_mchid")
        private String subMchId;

        @JsonProperty(value = "out_trade_no")
        private String outTradeNo;

        @JsonProperty(value = "transaction_id")
        private String transactionId;

        @JsonProperty(value = "trade_state")
        private String tradeState;

        @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
        @JsonProperty(value = "success_time")
        private LocalDateTime successTime;

        @JsonProperty(value = "amount")
        private Amount amount;

        @JsonProperty(value = "payer")
        private Payer payer;
    }

    @JsonProperty(value = "id")
    private String id;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value = "create_time")
    private String createTime;

    @JsonProperty(value = "resource")
    private WePayResource resource;

    public PaymentNotifyPo(PayTrans payTrans) { // PayTrans没有id属性
        this.createTime = LocalDateTime.now().toString();
        this.resource = new WePayResource(
                payTrans.getSpAppid(),
                payTrans.getSpMchid(),
                payTrans.getSubMchid(),
                payTrans.getOutTradeNo(),
                payTrans.getTransactionId(),
                payTrans.getTradeState(),
                payTrans.getSuccessTime(),
                new WePayResource.Amount(
                        payTrans.getAmount().getTotal().longValue(), payTrans.getAmount().getTotal().longValue()),
                new WePayResource.Payer(
                        payTrans.getPayer().getSpOpenid()
                )
        );
    }

}
