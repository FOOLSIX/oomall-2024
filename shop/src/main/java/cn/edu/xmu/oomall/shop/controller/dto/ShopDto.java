//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.controller.dto;


import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 */
@NoArgsConstructor
public class ShopDto {
    @NotBlank(message = "店铺名称不能为空")
    private String name;

    /**
     * 联系人信息
     */
    private ShopConsigneeDto consignee;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShopConsigneeDto getConsignee1() {
        return consignee;
    }

    public void setConsignee(ShopConsigneeDto consignee) {
        this.consignee = consignee;
    }
}
