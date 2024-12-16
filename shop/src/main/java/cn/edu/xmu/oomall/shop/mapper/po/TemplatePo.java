//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "shop_template")
@CopyFrom({Template.class})
public class TemplatePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopId;

    private String name;

    private Byte defaultModel;

    private String templateBean;

    /**
     * 分包策略
     */
    private String divideStrategy;

    /**
     * 打包属性
     */
    private String packAlgorithm;

    /**
     * 创建者id
     */
    private Long creatorId;

    /**
     * 创建者
     */
    private String creatorName;

    /**
     * 修改者id
     */
    private Long modifierId;

    /**
     * 修改者
     */
    private String modifierName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * @return
     * @author ZhaoDong Wang
     * 2023-dgn1-009
     */
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
