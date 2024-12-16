package cn.edu.xmu.oomall.wechatpay.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DivRefundTransVo {
    @NotBlank
    private String subMchid;
    @NotBlank
    private String outOrderNo;
    @NotBlank
    private String outReturnNo;
    @NotBlank
    private String returnMchid;
    @NotNull
    private Integer amount;
    @NotBlank
    private String description;
}
