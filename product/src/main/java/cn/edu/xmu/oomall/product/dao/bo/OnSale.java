//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.product.controller.dto.GrouponActDto;
import cn.edu.xmu.oomall.product.controller.dto.OnSaleDto;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.activity.ActivityDao;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import cn.edu.xmu.oomall.product.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.po.OnsalePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({OnsalePo.class, OnSaleDto.class, GrouponActDto.class})
@Slf4j
public class OnSale extends OOMallObject implements Serializable {
    @JsonIgnore
    public  final static Long NOTEXIST = -1L;

    @Builder
    public OnSale(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Long price, LocalDateTime beginTime, LocalDateTime endTime, Integer quantity, Integer maxQuantity, Long shopId, Long productId, Byte type, Long deposit, LocalDateTime payTime) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.price = price;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.quantity = quantity;
        this.maxQuantity = maxQuantity;
        this.shopId = shopId;
        this.productId = productId;
        this.type = type;
        this.deposit = deposit;
        this.payTime = payTime;
    }

    /**
     * 正常
     */
    @JsonIgnore
    public static final Byte NORMAL = 0;
    /**
     * 秒杀
     */
    @JsonIgnore
    public static final Byte SECONDKILL = 1;

    /**
     * 预售
     */
    @JsonIgnore
    public static final Byte GROUPON = 2;

    /**
     * 预售
     */
    @JsonIgnore
    public static final Byte ADVSALE = 3;


    private Long price;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer quantity;
    private Integer maxQuantity;
    private Long shopId;
    private Byte type;
    private Long productId;
    private LocalDateTime payTime;
    private Long deposit;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ShopDao shopDao;

    @JsonIgnore
    @ToString.Exclude
    private Shop shop;

    /**
     * @modify Rui Li
     * @task 2023-dgn2-007
     */
    public Shop getShop() {
        if (Objects.isNull(this.shop) && Objects.nonNull(this.shopDao)){
            this.shop = this.shopDao.findById(this.shopId);
        }
        return this.shop;
    }




    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ProductDao productDao;

    @JsonIgnore
    @ToString.Exclude
    private Product product;

    public Product getProduct() {
        if (null == this.product && null != this.productDao) {
            this.product = this.productDao.findValidById(this.shopId, this.productId);
        }
        return this.product;
    }


    @JsonIgnore
    @ToString.Exclude
    private List<Activity> actList;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ActivityDao activityDao;

    public List<Activity> getActList() {
        if (Objects.isNull(this.actList) && Objects.nonNull(this.activityDao)) {
            this.actList = this.activityDao.retrieveByOnsaleId(this.id);
            log.debug("getActList: actList = {}", actList);
        }
        log.debug("getActList: actList = {}", this.actList);
        return this.actList;

    }

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private OnSaleDao onSaleDao;

    /**
     * 取消销售
     * @param cancelTime 取消时间
     * @return 取消销售需修改的属性
     */
    public String cancel(LocalDateTime cancelTime, UserDto user){
        OnSale updateOnsale = OnSale.builder().id(this.id).build();

        if(this.getBeginTime().isAfter(cancelTime)){
            //销售活动未开始
            updateOnsale.setEndTime(this.getBeginTime());
        }else if (this.getEndTime().isBefore(cancelTime)) {
            //销售活动已结束
            updateOnsale.setEndTime(this.getEndTime());
        } else{
            //销售活动正在进行中
            updateOnsale.setEndTime(cancelTime);
        }
        return this.onSaleDao.save(updateOnsale, user);
    }

    public Boolean hasValue(){
        return (Objects.nonNull(this.productId) || Objects.nonNull(this.beginTime) || Objects.nonNull( this.endTime) || Objects.nonNull(this.price) || Objects.nonNull(this.shopId) ||
                Objects.nonNull(this.quantity) || Objects.nonNull(this.maxQuantity) || Objects.nonNull(this.deposit) || Objects.nonNull(this.payTime) || Objects.nonNull(this.type));
    }


    public Byte getType() {
        return this.type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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
