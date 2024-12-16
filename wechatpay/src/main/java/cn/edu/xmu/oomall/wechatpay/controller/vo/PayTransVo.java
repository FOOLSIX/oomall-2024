package cn.edu.xmu.oomall.wechatpay.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayTransVo {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payer {
        @NotBlank
        private String spOpenid;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayAmount {
        @NotBlank
        private Integer total;
    }

    @NotBlank
    private String spAppid;

    @NotBlank
    private String spMchid;

    @NotBlank
    private String subMchid;

    @NotBlank
    private String description;

    @NotBlank
    private String outTradeNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime timeExpire;

    @NotBlank
    private String notifyUrl;

    @NotNull
    private PayAmount amount;

    @NotNull
    private Payer payer;
}
