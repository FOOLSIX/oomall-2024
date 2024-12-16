package cn.edu.xmu.oomall.product.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuzhicheng
 * @create 2022-12-04 12:19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({Product.class})
public class FullProductVo {
    private Long id;
    private IdNameTypeVo shop;
    private List<IdNameTypeVo> otherProducts;
    private String name;
    private String skuSn;
    private Long originalPrice;
    private Long weight;
    private Byte status;
    private String unit;
    private String barCode;
    private String originPlace;
    private Long freeThreshold;
    private Integer commissionRatio;
    private IdNameTypeVo category;
    private IdNameTypeVo template;
    private IdNameTypeVo shopLogistic;
    private IdNameTypeVo creator;
    private IdNameTypeVo modifier;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public FullProductVo(Product product){
        super();
        CloneFactory.copy(this, product);
        //设置返回对象属性
        this.setShop1(IdNameTypeVo.builder().id(product.getShop().getId()).name(product.getShop().getName()).build());
        this.setOtherProducts1(product.getOtherProduct().stream().map(o -> IdNameTypeVo.builder().id(o.getId()).name(o.getName()).build()).collect(Collectors.toList()));
        this.setCategory1(IdNameTypeVo.builder().id(product.getCategory().getId()).name(product.getCategory().getName()).build());
        if(product.getLogistics() != null) {
            this.setShopLogistic1(IdNameTypeVo.builder().id(product.getLogistics().getId()).name(product.getLogistics().getName()).build());
        }
        this.setTemplate1(IdNameTypeVo.builder().id(product.getTemplate().getId()).name(product.getTemplate().getName()).build());
        this.setCreator(IdNameTypeVo.builder().id(product.getCreatorId()).name(product.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(product.getModifierId()).name(product.getModifierName()).build());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdNameTypeVo getShop() {
        return shop;
    }

    public void setShop1(IdNameTypeVo shop) {
        this.shop = shop;
    }

    public List<IdNameTypeVo> getOtherProducts() {
        return otherProducts;
    }

    public void setOtherProducts1(List<IdNameTypeVo> otherProducts) {
        this.otherProducts = otherProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public Long getFreeThreshold() {
        return freeThreshold;
    }

    public void setFreeThreshold(Long freeThreshold) {
        this.freeThreshold = freeThreshold;
    }

    public Integer getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(Integer commissionRatio) {
        this.commissionRatio = commissionRatio;
    }

    public IdNameTypeVo getCategory() {
        return category;
    }

    public void setCategory1(IdNameTypeVo category) {
        this.category = category;
    }

    public IdNameTypeVo getShopLogistic() {
        return shopLogistic;
    }

    public void setShopLogistic1(IdNameTypeVo shopLogistic) {
        this.shopLogistic= shopLogistic;
    }

    public IdNameTypeVo getTemplate() {
        return template;
    }

    public void setTemplate1(IdNameTypeVo template) {
        this.template = template;
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
