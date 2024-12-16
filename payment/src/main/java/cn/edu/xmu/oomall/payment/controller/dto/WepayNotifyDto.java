package cn.edu.xmu.oomall.payment.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class WepayNotifyDto {

    @Data
    @NotNull
    @NoArgsConstructor
    public class WePayResource {

        @Data
        @NotNull
        @NoArgsConstructor
        public class Payer {
            @JsonProperty(value = "sp_openid")
            private String spOpenId;
        };

        @Data
        @NoArgsConstructor
        public class Amount {
            @JsonProperty(value = "total")
            @NotNull
            private Long total;

            @JsonProperty(value = "payer_total")
            @NotNull
            private Long payerTotal;
        };

        @JsonProperty(value = "sp_appid")
        @NotNull
        private String spAppId;

        @JsonProperty(value = "sp_mchid")
        @NotNull
        private String spMchId;

        @JsonProperty(value = "sub_mchid")
        @NotNull
        private String subMchId;

        @JsonProperty(value = "out_trade_no")
        @NotNull
        private String outTradeNo;

        @JsonProperty(value = "transaction_id")
        @NotNull
        private String transactionId;

        @JsonProperty(value = "trade_state")
        @NotNull
        private String tradeState;


        @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
        @JsonProperty(value = "success_time")
        @NotNull
        private LocalDateTime successTime;

        @JsonProperty(value = "amount")
        @NotNull
        private Amount amount;

        @JsonProperty(value = "payer")
        @NotNull
        private Payer payer;
    }

    @JsonProperty(value = "id")
    @NotNull
    private String id;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty(value = "create_time")
    @NotNull
    private String createTime;

    @JsonProperty(value = "resource")
    @NotNull
    private WePayResource resource;
}
