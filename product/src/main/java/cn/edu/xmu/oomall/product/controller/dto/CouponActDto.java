//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.dto;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 优惠活动vo
 */
@NoArgsConstructor
@ToString
@Data
public class CouponActDto {

    @NotBlank(message = "优惠活动名称不能为空", groups = {NewGroup.class})
    private String name;

    @Min(value = 0, message = "0-不用优惠券", groups = {Default.class, NewGroup.class})
    @NotNull(groups = {NewGroup.class})
    private Integer quantity;

    @Min(value = 0, message = "0-每人数量", groups = {Default.class, NewGroup.class})
    @Max(value = 1, message = "1-总数控制", groups = {Default.class, NewGroup.class})
    @NotNull(groups = {NewGroup.class})
    private Integer quantityType;

    @Min(value = 0, message = "0-与活动同", groups = {Default.class, NewGroup.class})
    @NotNull(groups = {NewGroup.class})
    private Integer validTerm;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @NotNull(groups = {NewGroup.class})
    private LocalDateTime couponTime;

    @NotNull(message = "优惠券规则不能为空", groups = {NewGroup.class})
    private BaseCouponDiscount strategy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(Integer quantityType) {
        this.quantityType = quantityType;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }

    public LocalDateTime getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(LocalDateTime couponTime) {
        this.couponTime = couponTime;
    }

    public BaseCouponDiscount getStrategy() {
        return strategy;
    }

    public void setStrategy(JsonNode strategy) {
        this.strategy = BaseCouponDiscount.getInstance(strategy.toString()).orElse(null);
    }
}