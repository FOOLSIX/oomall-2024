//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import cn.edu.xmu.oomall.product.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@Slf4j
public abstract class Activity extends OOMallObject implements Serializable {

    /**
     * 新建状态
     */
    public static Byte NEW = 0;

    /**
     * 活跃状态
     */
    public static Byte ACTIVE = 1;

    /**
     * 状态和名称的对应
     */
    @JsonIgnore
    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(NEW, "新建");
            put(ACTIVE, "活跃");
        }
    };

    /**
     * 商铺id
     */
    protected Long shopId;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected ShopDao shopDao;

    @JsonIgnore
    @ToString.Exclude
    protected Shop shop;

    public Shop getShop(){
        if (Objects.isNull(shopId)){
            return null;
        }
        if (Objects.isNull(shop) && !Objects.isNull(shopDao)){
            this.shop = this.shopDao.findById(this.shopId);
        }
        return this.shop;
    }
    /**
     * 活动名称
     */
    protected String name;
    /**
     * mongo数据库主键id
     */
    protected String objectId;
    /**
     * 活动Bean名称
     */
    protected String actClass;

    protected Byte status;


    /**
     * 关联的onsale
     */
    @ToString.Exclude
    @JsonIgnore
    protected List<OnSale> onsaleList;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected OnSaleDao onsaleDao;


    public List<OnSale> getOnsaleList(){
        if (Objects.isNull(this.onsaleList) && Objects.nonNull(this.onsaleDao)){
            this.onsaleList = this.onsaleDao.retrieveByActId(this.id, 1, MAX_RETURN);
            log.debug("getOnsaleList: onsaleList = {}", onsaleList);
        }
        return this.onsaleList;
    }

    public abstract void setId(Long id);

    public abstract Long getId();

    public abstract void  setCreatorId(Long creatorId);

    public abstract Long  getCreatorId();

    public abstract void setCreatorName(String creatorName);

    public abstract String getCreatorName();

    public abstract void setModifierId(Long modifierId);

    public abstract Long getModifierId();
    public abstract void setModifierName(String modifierName);

    public abstract String getModifierName();

    public abstract void setGmtCreate(LocalDateTime gmtCreate);

    public abstract LocalDateTime getGmtCreate();

    public abstract void setGmtModified(LocalDateTime gmtModified);

    public abstract LocalDateTime getGmtModified();

    public abstract void setShopId(Long shopId);

    public abstract Long getShopId();

    public abstract void setName(String name);

    public abstract String getName();

    public abstract void setObjectId(String objectId);
    public abstract String getObjectId();

    public abstract void setActClass(String actClass);

    public abstract String getActClass();

    public abstract Activity publish();

    public abstract Byte getStatus();

    public abstract void setStatus(Byte status);
}