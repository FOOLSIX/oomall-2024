//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
/**
 * 商品视图对象
 * @author Ming Qiu
 **/
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVo {

    private String skuSn;

    @Min(0)
    private Long weight;

    private String barcode;

    @Min(0)
    private Long freeThreshold;

    private Long commissionRatio;

    private Long templateId;

    private Long shopLogisticsId;

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getFreeThreshold() {
        return freeThreshold;
    }

    public void setFreeThreshold(Long freeThreshold) {
        this.freeThreshold = freeThreshold;
    }

    public Long getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(Long commissionRatio) {
        this.commissionRatio = commissionRatio;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getShopLogisticsId() {
        return shopLogisticsId;
    }

    public void setShopLogisticsId(Long shopLogisticsId) {
        this.shopLogisticsId = shopLogisticsId;
    }
}
