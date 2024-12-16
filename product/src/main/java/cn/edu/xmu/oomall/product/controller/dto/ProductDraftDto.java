//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.dto;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * @author wuzhicheng
 * @create 2022-12-04 11:04
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDraftDto {
    @NotNull(message = "名称不能为空", groups = {NewGroup.class})
    private String name;

    @Min(value = 0, message = "原价不能小于0")
    @NotNull(message = "原价不能为空", groups = {NewGroup.class})
    private Long originalPrice;

    @NotNull(message = "分类不能为空", groups = {NewGroup.class})
    private Long categoryId;

    private String unit;

    private String originPlace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }
}
