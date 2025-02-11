//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.product.controller.dto.ProductVo;
import cn.edu.xmu.oomall.product.mapper.po.ProductPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({ ProductDraft.class, ProductVo.class, ProductPo.class })
public class Product extends OOMallObject implements Serializable {

    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Product.class);

    // 无相关的商品
    public static final Long NO_RELATE_PRODUCT = 0L;

    // 无相关的运费模板
    public static final Long NO_TEMPLATE = 0L;

    // 使用默认的免邮门槛
    public static final Long DEFAULT = -1L;

    /**
     * 共两种状态
     */
    // 禁售中
    @JsonIgnore
    public static final Byte BANNED = 0;

    // 上架
    @JsonIgnore
    public static final Byte ONSHELF = 1;

    // 下架
    @JsonIgnore
    public static final Byte OFFSHELF = 2;

    // 未禁售
    @JsonIgnore
    private static final Byte ALLOW = 3;

    /**
     * 状态和名称的对应
     */
    @JsonIgnore
    public static final Map<Byte, String> STATUSNAMES = new HashMap<Byte, String>() {
        {
            put(ONSHELF, "上架");
            put(BANNED, "禁售");
            put(OFFSHELF, "下架");
        }
    };

    /**
     * 获得当前状态名称
     * 
     * @author Ming Qiu
     *         <p>
     *         date: 2022-11-13 0:43
     * @return
     */
    @JsonIgnore
    public String getStatusName() {
        return STATUSNAMES.get(this.status);
    }

    private String skuSn;

    private String name;

    private Long originalPrice;

    private Long weight;

    private String barcode;

    private String unit;

    private String originPlace;

    @Setter
    @CopyFrom.Exclude({ ProductDraft.class, ProductVo.class, ProductPo.class })
    private Integer commissionRatio = 0;

    public Integer getCommissionRatio() {
        return this.commissionRatio;
    }

    private Byte status;

    public void setStatus(Byte status) {
        this.status = status;
    }

    private Long goodsId;
    /**
     * 相关商品
     */
    @JsonIgnore
    @ToString.Exclude
    private List<Product> otherProduct;

    private Long categoryId;

    private Long shopId;

    private Long templateId;

    private Long shopLogisticId;

    private Long freeThreshold;

    public void ban() {
        this.status = BANNED;
    }

    public void allow() {
        this.status = ALLOW;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
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

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
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

    public Long getShopLogisticId() {
        return shopLogisticId;
    }

    public void setShopLogisticId(Long shopLogisticId) {
        this.shopLogisticId = shopLogisticId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    @CopyFrom.Exclude({ ProductDraft.class, ProductPo.class })
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getFreeThreshold() {
        return freeThreshold;
    }

    public void setFreeThreshold(Long freeThreshold) {
        this.freeThreshold = freeThreshold;
    }
}
