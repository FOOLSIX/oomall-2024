//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.WeightTemplate;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@NoArgsConstructor
@Document(collection = "regionTemplate")
@CopyFrom({WeightTemplate.class, RegionTemplate.class})
@ToString
public class WeightTemplatePo {

    @Id
    private String objectId;

    private Integer firstWeight;

    private Long firstWeightPrice;

    private List<WeightThresholdPo> thresholds;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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
}
