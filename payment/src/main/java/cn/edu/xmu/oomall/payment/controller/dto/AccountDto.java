package cn.edu.xmu.oomall.payment.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * @author 黄坤鹏
 * @date 2022/11/9 8:28
 */
@Data
@NoArgsConstructor
public class AccountDto {

    @NotBlank(message = "子商户号必填")
    private String subMchid;
}
