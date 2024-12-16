package cn.edu.xmu.oomall.shop.controller.dto;

import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 */
@NoArgsConstructor
public class ShopConsigneeDto {
    /**
     * 店铺联系人姓名和电话
     */
    @NotBlank(message = "店铺联系人姓名不能为空")
    private String name;
    @NotBlank(message = "店铺联系人电话不能为空")
    private String mobile;

    @NotNull(message = "地区必填")
    private Long regionId;

    @NotBlank(message = "店铺联系人详细地址不能为空")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
