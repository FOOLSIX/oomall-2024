package cn.edu.xmu.oomall.wechatpay.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayTransCancelVo {
    @NotBlank
    private String spMchid;
    @NotBlank
    private String subMchid;
}
