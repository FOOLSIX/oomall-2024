//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.bo.template;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.shop.dao.TemplateDao;
import cn.edu.xmu.oomall.shop.dao.bo.Region;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.dao.bo.divide.DivideStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 运费模板的父类
 */
@ToString(callSuper = true, doNotUseGetters = true)
@NoArgsConstructor
public abstract class RegionTemplate extends OOMallObject implements Cloneable, Serializable {

    /**
     * 包裹的件数上限
     */
    protected Integer upperLimit;

    /**
     * 续重或续件计算单位 克或个
     */
    protected Integer unit;

    protected Long regionId;

    protected Long templateId;

    @JsonIgnore
    protected String objectId;


    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected TemplateDao templateDao;

    @JsonIgnore
    @ToString.Exclude
    protected Template template;

    /**
     * @return
     * @author ZhaoDong Wang
     * 2023-dgn1-009
     */
    public Template getTemplate() {
        if (null == this.template && !(null == this.templateDao)) {
            this.template = this.templateDao.findById(PLATFORM, this.templateId);
        }
        return this.template;
    }

    public String getTemplateBean() {
        return this.getTemplate().getTemplateBean();
    }

    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected RegionDao regionDao;

    /**
     * 配送地区
     */
    @JsonIgnore
    @ToString.Exclude
    protected Region region;

    public Region getRegion() {
        if (null != this.regionId && null == this.region) {
            this.region = this.regionDao.findById(this.regionId);
        }
        return this.region;
    }

    /**
     * 计算包裹运费
     *
     * @param productItems
     * @return
     * @author ZhaoDong Wang
     * 2023-dgn1-009
     */
    public Collection<TemplateResult> calculate(Collection<ProductItem> productItems) {
        return this.getTemplate().getStrategy().divide(this, productItems).stream().map(pack -> {
            Long fee = cacuFreight(pack);
            return new TemplateResult(fee, pack);
        }).collect(Collectors.toList());
    }

    /**
     * 根据包裹里的商品计算运费
     *
     * @param pack
     * @return
     * @author Ming Qiu
     * <p>
     * date: 2022-11-20 15:54
     */
    public abstract Long cacuFreight(Collection<ProductItem> pack);

    /**
     * 用不同的regionId克隆模板
     *
     * @param regionId
     * @return
     * @throws CloneNotSupportedException
     * @author Ming Qiu
     * <p>
     * date: 2022-11-20 14:48
     */
    public RegionTemplate cloneWithRegion(Long regionId) throws CloneNotSupportedException {
        RegionTemplate template = (RegionTemplate) super.clone();
        template.setRegionId(regionId);
        return template;
    }

    public TemplateType gotType() {
        return this.getTemplate().gotType();
    }

    public abstract Integer getUpperLimit();

    public abstract void setUpperLimit(Integer upperLimit);

    public abstract Integer getUnit();

    public abstract void setUnit(Integer unit);

    public abstract String getObjectId();

    public abstract void setObjectId(String objectId);

    public abstract Long getRegionId();

    public abstract void setRegionId(Long regionId);

    public abstract Long getTemplateId();

    public abstract void setTemplateId(Long templateId);

    public abstract void setId(Long id);

    public abstract Long getId();

    public abstract void setCreatorId(Long creatorId);

    public abstract Long getCreatorId();

    public abstract void setCreatorName(String creatorName);

    public abstract String getCreatorName();

    public abstract void setModifierId(Long modifierId);

    public abstract Long getModifierId();

    public abstract void setModifierName(String modifierName);

    public abstract LocalDateTime getGmtCreate();

    public abstract void setGmtCreate(LocalDateTime gmtCreate);

    public abstract LocalDateTime getGmtModified();

    public abstract void setGmtModified(LocalDateTime gmtModified);
}
