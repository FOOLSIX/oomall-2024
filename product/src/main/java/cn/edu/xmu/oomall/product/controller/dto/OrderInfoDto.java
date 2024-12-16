package cn.edu.xmu.oomall.product.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInfoDto {
    @NotNull(message = "onsale id 不能为空")
    private Long onsaleId;
    @Min(value = 1, message = "数量大于1")
    private Integer quantity;
}