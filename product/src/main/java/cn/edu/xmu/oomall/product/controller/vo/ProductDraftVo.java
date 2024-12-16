//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.bo.Category;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.dao.bo.ProductDraft;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 产品草稿对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({ ProductDraft.class })
public class ProductDraftVo {
    private Long id;
    private IdNameTypeVo shop;
    private String name;
    private Long originalPrice;
    private String originPlace;

    private String unit;
    private IdNameTypeVo category;
    private IdNameTypeVo product;
    private IdNameTypeVo creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameTypeVo modifier;

    public ProductDraftVo(ProductDraft draft) {
        super();
        CloneFactory.copy(this, draft);
        this.setCreator(IdNameTypeVo.builder().id(draft.getCreatorId()).name(draft.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(draft.getModifierId()).name(draft.getModifierName()).build());
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

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public void setShop(Shop shop) {
        this.shop = IdNameTypeVo.builder().id(shop.getId()).name(shop.getName()).build();
    }

    public IdNameTypeVo getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category != null)
            this.category = IdNameTypeVo.builder().id(category.getId()).name(category.getName()).build();
    }

    public IdNameTypeVo getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product!=null) {
            this.product = IdNameTypeVo.builder().id(product.getId()).name(product.getName()).build();
        }
    }

    public IdNameTypeVo getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeVo creator) {
        this.creator = creator;
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

    public IdNameTypeVo getModifier() {
        return modifier;
    }

    public void setModifier(IdNameTypeVo modifier) {
        this.modifier = modifier;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
