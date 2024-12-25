package cn.edu.xmu.oomall.customer.controller.dto;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;


public class CartItemDto {

    @NotNull(message = "productid不能为空", groups = {NewGroup.class})
    private Long productId;
    @Min(value = 0, message = "数量不能小于0")
    @Max(value = 1000, message = "数量不能大于999")
    @NotNull(message = "数量不能为0", groups = {NewGroup.class})
    private int quantity;


}
