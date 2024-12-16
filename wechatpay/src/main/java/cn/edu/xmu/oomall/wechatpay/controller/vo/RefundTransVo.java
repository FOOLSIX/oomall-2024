package cn.edu.xmu.oomall.wechatpay.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundTransVo {

    @NotBlank
    private String subMchid;

    @NotBlank
    private String transactionId;

    @NotBlank
    private String outRefundNo;

    @NotNull
    private RefundAmountVo amount;
}
