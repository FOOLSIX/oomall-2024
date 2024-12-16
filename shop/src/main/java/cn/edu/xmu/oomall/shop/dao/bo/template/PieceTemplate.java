//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.bo.template;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.controller.dto.PieceTemplateDto;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.mapper.po.PieceTemplatePo;
import cn.edu.xmu.oomall.shop.mapper.po.RegionTemplatePo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;


@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({PieceTemplateDto.class, PieceTemplatePo.class, RegionTemplatePo.class})
public class PieceTemplate extends RegionTemplate implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PieceTemplate.class);

    /**
     * 首件数目
     */
    private Integer firstItems;

    /**
     * 起步费用
     */
    private Long firstPrice;

    /**
     * 续件
     */
    private Integer additionalItems;

    /**
     * 每增加additionalItems件商品，增加多少费用,小于additionalItems也是同样价钱
     */
    private Long additionalPrice;

    @Override
    public Long cacuFreight(Collection<ProductItem> pack) {
        Integer total = pack.stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get();
        logger.debug("cacuFreight: total = {}, template = {}", total, this);
        Long result = this.firstPrice;
        Integer rest = total - this.firstItems;
        if (rest > 0) {
            result += (rest / this.additionalItems) * this.additionalPrice;
            if (0 != rest % this.additionalItems) {
                result += this.additionalPrice;
            }
            logger.debug("cacuFreight: result = {}", result);
        }
        return result;
    }

    public Integer getFirstItems() {
        return firstItems;
    }

    public void setFirstItems(Integer firstItems) {
        this.firstItems = firstItems;
    }

    public Long getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Long firstPrice) {
        this.firstPrice = firstPrice;
    }

    public Integer getAdditionalItems() {
        return additionalItems;
    }

    public void setAdditionalItems(Integer additionalItems) {
        this.additionalItems = additionalItems;
    }

    public Long getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(Long additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public Integer getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Integer upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
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
}
