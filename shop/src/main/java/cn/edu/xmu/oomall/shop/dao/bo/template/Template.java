package cn.edu.xmu.oomall.shop.dao.bo.template;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.shop.controller.dto.TemplateDto;
import cn.edu.xmu.oomall.shop.dao.bo.Region;
import cn.edu.xmu.oomall.shop.dao.bo.divide.DivideStrategy;
import cn.edu.xmu.oomall.shop.dao.bo.divide.PackAlgorithm;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import cn.edu.xmu.oomall.shop.mapper.po.TemplatePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 运费模板对象
 */
@ToString(callSuper = true, doNotUseGetters = true)
@NoArgsConstructor
@CopyFrom({TemplateDto.class, TemplatePo.class})
public class Template extends OOMallObject implements Serializable, Cloneable {
    private static final Logger logger = LoggerFactory.getLogger(Template.class);
    /**
     * 默认模板
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte DEFAULT = 1;

    @ToString.Exclude
    @JsonIgnore
    public static final Byte COMMON = 0;

    @ToString.Exclude
    @JsonIgnore
    public static final String PIECE = "pieceTemplateDao";

    @ToString.Exclude
    @JsonIgnore
    public static final String WEIGHT = "weightTemplateDao";

    @ToString.Exclude
    @JsonIgnore
    public static Map<String, TemplateType> TYPE = new HashMap<>() {
        {
            put(PIECE, new Piece());
            put(WEIGHT, new Weight());
        }
    };

    /**
     * 商铺id
     */
    private Long shopId;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 1 默认
     */
    private Byte defaultModel;

    /**
     * 模板类名
     */
    protected String templateBean;

    /**
     * 分包策略
     */
    protected String divideStrategy;

    @Getter
    @Setter
    protected DivideStrategy strategy;
    /**
     * 打包算法
     */
    protected String packAlgorithm;


    public TemplateType gotType() {
        assert this.templateBean != null;
        return TYPE.get(this.templateBean);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Template template = (Template) super.clone();
        template.setDefaultModel(COMMON);
        return template;
    }

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RegionTemplateDao regionTemplateDao;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RegionDao regionDao;
    /**
     * 更新地区的运费模板
     *
     * @param regionTemplate1   地区模板值
     * @param user 操作者
     * @return 影响的key
     */
    public String updateRegionTemplate(RegionTemplate regionTemplate1, UserDto user) {
        Optional<RegionTemplate> regionTemplate = this.regionTemplateDao.retrieveByTemplateAndRegionId(this, regionTemplate1.getRegionId());
        RegionTemplate oldBo = null;
        if (regionTemplate.isPresent()) {
            oldBo = regionTemplate.get();
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
        regionTemplate1.setId(oldBo.getId());
        logger.debug("oldBo = {}", oldBo);
        regionTemplate1.setObjectId(oldBo.getObjectId());
        return this.regionTemplateDao.save(this, regionTemplate1, user);
    }

    /**
     * 创建地区模板
     * @param clazz 模板类别
     * @param regionTemplate 地区模板值对象
     * @param user 创建用户
     */
    public void createRegionTemplate(Class clazz, RegionTemplate regionTemplate, UserDto user){
        if (!this.gotType().getClass().equals(clazz)){
            throw new BusinessException(ReturnNo.FREIGHT_TEMPLATENOTMATCH);
        }
        this.regionTemplateDao.insert(this,regionTemplate,user);
    }

    /**
     * 根据运费模板id和地区id来查找地区模板信息
     * 如果没有与rid对应的地区模板，则会继续查询rid最近的上级地区模板
     * 用于计算运费
     *
     * @param regionId 地区id
     * @return 地区运费模板
     */
    public RegionTemplate findRegionTemplate(Long regionId){
        Optional<RegionTemplate> ret = this.regionTemplateDao.retrieveByTemplateAndRegionId(this, regionId);
        //若没有与rid对应的地区模板，继续查找最近的上级地区模板
        if (ret.isEmpty()) {
            List<Region> pRegions = this.regionDao.retrieveParentRegionsById(regionId);
            /*
             * 由近到远查询地区模板,只要找到一个不为空的地区模板就结束查询
             */
            for (Region r : pRegions) {
                ret = this.regionTemplateDao.retrieveByTemplateAndRegionId(this, r.getId());
                if (ret.isPresent()) {
                    break;
                }
            }
        }
        if (ret.isPresent()) {
            RegionTemplate bo = ret.get();
            logger.debug("findByTemplateIdAndRegionId: regionTemplate={}", bo);
            return bo;
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(Byte defaultModel) {
        this.defaultModel = defaultModel;
    }

    public String getTemplateBean() {
        return templateBean;
    }

    public void setTemplateBean(String templateBean) {
        this.templateBean = templateBean;
    }

    public String getDivideStrategy() {
        return divideStrategy;
    }

    public void setDivideStrategy(String divideStrategy) {
        this.divideStrategy = divideStrategy;
    }

    public String getPackAlgorithm() {
        return packAlgorithm;
    }

    public void setPackAlgorithm(String packAlgorithm) {
        this.packAlgorithm = packAlgorithm;
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
