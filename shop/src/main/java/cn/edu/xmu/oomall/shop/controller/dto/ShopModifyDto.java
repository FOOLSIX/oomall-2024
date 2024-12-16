package cn.edu.xmu.oomall.shop.controller.dto;

import lombok.NoArgsConstructor;

/**
 */
@NoArgsConstructor
public class ShopModifyDto {
    /**
     * 联系人信息
     */
    ShopConsigneeDto consignee;

    /**
     * 商铺免邮金额
     */
    Integer freeThreshold;

    public ShopConsigneeDto getConsignee1() {
        return consignee;
    }

    public void setConsignee(ShopConsigneeDto consignee) {
        this.consignee = consignee;
    }

    public Integer getFreeThreshold() {
        return freeThreshold;
    }

    public void setFreeThreshold(Integer freeThreshold) {
        this.freeThreshold = freeThreshold;
    }
}
