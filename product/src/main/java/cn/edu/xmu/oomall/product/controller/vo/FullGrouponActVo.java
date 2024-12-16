//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.bo.GrouponAct;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.model.Threshold;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({GrouponAct.class, OnSale.class})
public class FullGrouponActVo {

    private Long id;

    private String name;

    private List<Threshold> thresholds;

    private IdNameTypeVo shop;

    private LocalDateTime gmtCreate;
    private IdNameTypeVo creator;
    private LocalDateTime gmtModified;
    private IdNameTypeVo modifier;

    private List<SimpleOnsaleVo> onsaleList;

    public FullGrouponActVo(GrouponAct act){
        super();
        Shop shop = act.getShop();
        List<OnSale> onsales = act.getOnsaleList();
        if (onsales.size() != 1) {
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA);
        }

        CloneFactory.copy(this, act);
        this.setShop1(IdNameTypeVo.builder().id(shop.getId()).name(shop.getName()).build());
        this.setOnsaleList1(onsales.stream().map(o -> CloneFactory.copy(new SimpleOnsaleVo(), o)).collect(Collectors.toList()));
        this.setCreator(IdNameTypeVo.builder().id(act.getCreatorId()).name(act.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(act.getModifierId()).name(act.getModifierName()).build());
        this.setThresholds(act.getThresholds());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Threshold> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<Threshold> thresholds) {
        this.thresholds = thresholds;
    }

    public IdNameTypeVo getShop() {
        return shop;
    }

    public void setShop1(IdNameTypeVo shop) {
        this.shop = shop;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public IdNameTypeVo getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeVo creator) {
        this.creator = creator;
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

    public List<SimpleOnsaleVo> getOnsaleList() {
        return onsaleList;
    }

    public void setOnsaleList1(List<SimpleOnsaleVo> onsaleList) {
        this.onsaleList = onsaleList;
    }
}
