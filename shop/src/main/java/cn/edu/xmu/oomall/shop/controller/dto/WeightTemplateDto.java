//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.controller.dto;

import cn.edu.xmu.oomall.shop.mapper.po.WeightThresholdPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class WeightTemplateDto extends RegionTemplateDto {

    private Integer firstWeight;

    private Long firstWeightFreight;

    private List<WeightThresholdPo> thresholds;

    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Long getFirstWeightFreight() {
        return firstWeightFreight;
    }

    public void setFirstWeightFreight(Long firstWeightFreight) {
        this.firstWeightFreight = firstWeightFreight;
    }

    public List<WeightThresholdPo> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<WeightThresholdPo> thresholds) {
        this.thresholds = thresholds;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }
    public Integer getUnit() {
        return unit;
    }

    public void setUpperLimit(Integer upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Integer getUpperLimit() {
        return upperLimit;
    }

    public Long getFirstWeightPrice() {
        return firstWeightFreight;
    }


}
