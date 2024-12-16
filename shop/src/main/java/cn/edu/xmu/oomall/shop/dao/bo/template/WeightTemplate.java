package cn.edu.xmu.oomall.shop.dao.bo.template;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.controller.dto.WeightTemplateDto;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.mapper.po.RegionTemplatePo;
import cn.edu.xmu.oomall.shop.mapper.po.WeightTemplatePo;
import cn.edu.xmu.oomall.shop.mapper.po.WeightThresholdPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({WeightTemplateDto.class, WeightTemplatePo.class, RegionTemplatePo.class})
public class WeightTemplate extends RegionTemplate implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(WeightTemplate.class);

    /**
     * 首重
     */
    private Integer firstWeight;

    /**
     * 首重以下均为此费用
     */
    private Long firstWeightPrice;

    private List<WeightThresholdPo> thresholds;

    @Override
    public Long cacuFreight(Collection<ProductItem> pack) {
        Long result = this.firstWeightPrice;
        Integer weight = pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get();
        logger.debug("cacuFreight: weight = {}", weight);
        Integer prevThreshold = this.firstWeight;
        if (weight - this.firstWeight > 0) {
            for (WeightThresholdPo threshold : this.thresholds) {
                Integer upper = weight - threshold.getBelow() > 0 ? threshold.getBelow() : weight;
                //计算有多少个计价单位
                long num = (upper - prevThreshold) / this.unit;
                logger.debug("cacuFreight: upper = {}, prevThreshold = {},  {}", upper, prevThreshold, (upper - prevThreshold) % this.unit);
                if (0 != ((upper - prevThreshold) % this.unit)) {
                    num += 1;
                }
                result += threshold.getPrice() * num;
                prevThreshold = threshold.getBelow();
                logger.debug("cacuFreight: result = {}, threshold = {}, num = {}", result, threshold, num);
                if (upper == weight) {
                    break;
                }
            }
        }
        return result;
    }

    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Long getFirstWeightPrice() {
        return firstWeightPrice;
    }

    public void setFirstWeightPrice(Long firstWeightPrice) {
        this.firstWeightPrice = firstWeightPrice;
    }

    public List<WeightThresholdPo> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<WeightThresholdPo> thresholds) {
        this.thresholds = thresholds;
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
