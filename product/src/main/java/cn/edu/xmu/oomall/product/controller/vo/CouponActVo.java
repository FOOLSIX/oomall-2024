//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.bo.CouponAct;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({CouponAct.class})
public class CouponActVo {

    private Long id;

    private String name;

    private Integer quantity;

    private LocalDateTime couponTime;

    private Integer quantityType;

    private IdNameTypeVo shop;

    private Integer validTerm;

    private BaseCouponDiscount strategy;

    private IdNameTypeVo creator;

    private IdNameTypeVo modifier;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public CouponActVo(CouponAct act){
        this();
        CloneFactory.copy(this, act);
        this.setShop1(IdNameTypeVo.builder().id(act.getShopId()).name(act.getShop().getName()).build());
        this.setCreator(IdNameTypeVo.builder().id(act.getCreatorId()).name(act.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(act.getModifierId()).name(act.getModifierName()).build());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(LocalDateTime couponTime) {
        this.couponTime = couponTime;
    }

    public Integer getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(Integer quantityType) {
        this.quantityType = quantityType;
    }

    public IdNameTypeVo getShop() {
        return shop;
    }

    public void setShop1(IdNameTypeVo shop) {
        this.shop = shop;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }

    public BaseCouponDiscount getStrategy() {
        return strategy;
    }

    public void setStrategy(BaseCouponDiscount strategy) {
        this.strategy = strategy;
    }

    public IdNameTypeVo getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeVo creator) {
        this.creator = creator;
    }

    public IdNameTypeVo getModifier() {
        return modifier;
    }

    public void setModifier(IdNameTypeVo modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}