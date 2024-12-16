package cn.edu.xmu.oomall.product.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.product.controller.dto.CouponActDto;
import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;
import cn.edu.xmu.oomall.product.mapper.po.CouponActPo;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 优惠活动
 */
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({ActivityPo.class, CouponActPo.class, CouponActDto.class})
public class CouponAct extends Activity {

    public static final String ACTCLASS = "couponActDao";

    private LocalDateTime couponTime;

    private Integer quantity;


    private Integer quantityType;


    private Integer validTerm;


    private BaseCouponDiscount strategy;


    public CouponAct() {
        super();
        this.actClass = ACTCLASS;
    }

    /**
     * 发布活动
     * @return
     */
    public CouponAct publish(){
        if (NEW != this.status){
            throw new BusinessException(ReturnNo.STATENOTALLOW,String.format(ReturnNo.STATENOTALLOW.getMessage(),"优惠活动", this.id, STATUSNAMES.get(NEW)));
        }
        CouponAct ret = new CouponAct();
        ret.setId(this.id);
        ret.setStatus(ACTIVE);
        return ret;
    }

    /**
     * 取消活动
     */
    public void cancel(){
        if (null != this.onsaleDao) {
            this.onsaleDao.deleteRelateOnsales(this.id);
        }
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getActClass() {
        return actClass;
    }

    public void setActClass(String actClass) {
        this.actClass = actClass;
    }

    public LocalDateTime getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(LocalDateTime couponTime) {
        this.couponTime = couponTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(Integer quantityType) {
        this.quantityType = quantityType;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }

    public BaseCouponDiscount getStrategy() {
        return strategy;
    }

    @CopyFrom.Exclude({CouponActPo.class})
    public void setStrategy(BaseCouponDiscount strategy) {
        this.strategy = strategy;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}