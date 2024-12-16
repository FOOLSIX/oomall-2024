package cn.edu.xmu.oomall.wechatpay.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DivPayTransVo {
    @NotBlank
    private String appid;

    @NotBlank
    private String subMchid;

    @NotBlank
    private String transactionId;

    @NotBlank
    private String outOrderNo;

    @NotNull
    private Collection<DivReceiverVo> receivers;

    @NotNull
    private Boolean unfreezeUnsplit;
}