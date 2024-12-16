//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.shop.dao.bo.Shop;
import com.fasterxml.jackson.annotation.JsonInclude;
import cn.edu.xmu.javaee.core.util.CloneFactory;

import java.time.LocalDateTime;

/**
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({Shop.class})
public class ShopVo {
    private Long id;
    private String name;
    private Long deposit;
    private Long depositThreshold;
    private Byte status;
    private ConsigneeVo consignee;
    private Integer freeThreshold;
    private IdNameTypeVo creator;
    private IdNameTypeVo modifier;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public ShopVo(Shop shop){
        super();
        CloneFactory.copy(this, shop);
        this.creator = IdNameTypeVo.builder().id(shop.getCreatorId()).name(shop.getCreatorName()).build();
        this.modifier = IdNameTypeVo.builder().id(shop.getModifierId()).name(shop.getModifierName()).build();
        this.consignee = ConsigneeVo.builder().regionId(shop.getRegionId()).address(shop.getAddress()).mobile(shop.getMobile()).name(shop.getConsignee()).build();
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

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public Long getDepositThreshold() {
        return depositThreshold;
    }

    public void setDepositThreshold(Long depositThreshold) {
        this.depositThreshold = depositThreshold;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public ConsigneeVo getConsignee() {
        return consignee;
    }

    public void setConsignee1(ConsigneeVo consignee) {
        this.consignee = consignee;
    }


    public Integer getFreeThreshold() {
        return freeThreshold;
    }

    public void setFreeThreshold(Integer freeThreshold) {
        this.freeThreshold = freeThreshold;
    }

    public IdNameTypeVo getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeVo creator) {
        this.creator = creator;
    }

    public IdNameTypeVo getModifier() {
        return modifier;
    }

    public void setModifier(IdNameTypeVo modifier) {
        this.modifier = modifier;
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
