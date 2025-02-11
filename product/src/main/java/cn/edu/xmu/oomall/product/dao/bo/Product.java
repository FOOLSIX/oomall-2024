//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.product.controller.dto.ProductDto;
import cn.edu.xmu.oomall.product.dao.CategoryDao;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleExecutor;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import cn.edu.xmu.oomall.product.dao.openfeign.FreightDao;
import cn.edu.xmu.oomall.product.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.product.dao.openfeign.TemplateDao;
import cn.edu.xmu.oomall.product.mapper.jpa.OnsalePoMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Logistics;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
import cn.edu.xmu.oomall.product.mapper.po.OnsalePo;
import cn.edu.xmu.oomall.product.mapper.po.ProductPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;

@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({ProductDraft.class, ProductDto.class, ProductPo.class})
@Slf4j
public class Product extends OOMallObject implements Serializable {

    //无相关的商品
    @JsonIgnore
    public static final Long NO_RELATE_PRODUCT=0L;

    //无相关的运费模板
    @JsonIgnore
    public static final Long NO_TEMPLATE=0L;

    //使用默认的免邮门槛
    @JsonIgnore
    public static final Long DEFAULT=-1L;


    /**
     * 共两种状态
     */
    //禁售中
    @JsonIgnore
    public static final  Byte BANNED = 0;

    //上架
    @JsonIgnore
    public static final  Byte ONSHELF  = 1;

    //下架
    @JsonIgnore
    public static final  Byte OFFSHELF  = 2;

    //未禁售
    @JsonIgnore
    private static final  Byte ALLOW  = 3;

    /**
     * 状态和名称的对应
     */
    @JsonIgnore
    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(ONSHELF, "上架");
            put(BANNED, "禁售");
            put(OFFSHELF, "下架");
        }
    };
    /**
     * 获得当前状态名称
     * @author Ming Qiu
     * <p>
     * date: 2022-11-13 0:43
     * @return
     */
    @JsonIgnore
    public String getStatusName(){
        return STATUSNAMES.get(this.status);
    }

    @Setter
    @Getter
    private String skuSn;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private Long originalPrice;

    @Setter
    @Getter
    private Long weight;

    @Setter
    @Getter
    private String barcode;

    @Setter
    @Getter
    private String unit;

    @Setter
    @Getter
    private String originPlace;

    @Setter
    @Getter
    @CopyFrom.Exclude({ProductDraft.class, ProductDto.class})
    private Integer commissionRatio;

    public Integer getCommissionRatio(){
        if (null == this.commissionRatio && null != getCategory()){
            this.commissionRatio = this.getCategory().getCommissionRatio();
        }
        return this.commissionRatio;
    }

    @Setter
    @Getter
    private Byte status;


    /**
     * 获得商品状态
     * @return
     */
    public Byte getStatus() {
        log.debug("getStatus: id ={}",this.id);
        LocalDateTime now = LocalDateTime.now();
        if(this.status == null )return null;
        if ((this.status.equals(Product.BANNED))) {
            return Product.BANNED;
        }else{
            if (null == this.getValidOnsale()){
                return Product.OFFSHELF;
            }else{
                if (this.getValidOnsale().getBeginTime().isBefore(now) && this.getValidOnsale().getEndTime().isAfter(now)) {
                    return Product.ONSHELF;
                }else{
                    return Product.OFFSHELF;
                }
            }
        }
    }

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private OnSaleDao onsaleDao;

    /**
     * @author :37220222203708
     */
    public OnSale createOnsale(OnSale onsale, UserDto user) {
        if (this.status.equals(Product.BANNED)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "产品", this.id, this.status));
        }

        if (this.onsaleDao.hasOverlap(onsale)){
            throw new BusinessException(ReturnNo.GOODS_ONSALE_CONFLICT, String.format(ReturnNo.GOODS_ONSALE_CONFLICT.getMessage(),onsale.getId()));
        }
        log.debug("createOnsale: onsale = {}", onsale);
        return this.onsaleDao.insert(onsale,user);
    }

    /**
     * @author :37220222203708
     */
    public String updateOnsale(OnSale onsale, UserDto user) {
        if (this.status.equals(Product.BANNED)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "产品", this.id, this.status));
        }
        if (this.onsaleDao.hasOverlap(onsale)){
            throw new BusinessException(ReturnNo.GOODS_ONSALE_CONFLICT, String.format(ReturnNo.GOODS_ONSALE_CONFLICT.getMessage(),onsale.getId()));
        }
        log.debug("updateOnsale: onsale = {}", onsale);
        return this.onsaleDao.save(onsale,user);
    }

    @Setter
    @Getter
    private Long goodsId;
    /**
     * 相关商品
     */
    @JsonIgnore
    @ToString.Exclude
    @Setter
    @Getter
    private List<Product> otherProduct;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ProductDao productDao;
    /**
     * @author jyx
     */
    @JsonIgnore
    public List<Product> getOtherProduct(){
        if (null == this.otherProduct&& null != this.productDao){
            this.otherProduct = this.productDao.retrieveOtherProductById(this.shopId, this.goodsId);
        }
        return this.otherProduct;
    }
    /**
     * 有效上架， 包括即将上架
     */
    @JsonIgnore
    @ToString.Exclude
    @Setter
    @Getter
    private OnSale validOnsale;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private OnSaleExecutor onsaleExecutor;

    /**
     * 采用command模式获取不同的onsale
     *
     * @author Ming Qiu
     * <p>
     * date: 2022-12-04 19:21
     * @return
     */
    public OnSale getValidOnsale(){
        if (Objects.isNull(this.validOnsale) && Objects.nonNull(this.onsaleExecutor)){
            log.debug("getValidOnsale: onsaleExecutor = {}", this.onsaleExecutor);
            this.validOnsale = this.onsaleExecutor.execute();
        }

        log.debug("getValidOnsale: validOnsale = {}", this.validOnsale);
        if (Objects.isNull(this.validOnsale) || this.validOnsale.getId().equals(OnSale.NOTEXIST)){
            return null;
        }
        return this.validOnsale;
    }

    @JsonIgnore
    public Long getPrice() {
        if (Objects.nonNull(this.getValidOnsale())) {
            return this.validOnsale.getPrice();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public Integer getQuantity() {
        if (Objects.nonNull(this.getValidOnsale())) {
            return this.validOnsale.getQuantity();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public LocalDateTime getBeginTime() {
        if (Objects.nonNull(this.getValidOnsale())) {
            return this.validOnsale.getBeginTime();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public LocalDateTime getEndTime() {
        if (Objects.nonNull(this.getValidOnsale())) {
            return this.validOnsale.getEndTime();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public Integer getMaxQuantity() {
        if (Objects.nonNull(this.getValidOnsale())) {
            return this.validOnsale.getMaxQuantity();
        } else {
            return null;
        }
    }

    @Setter
    @Getter
    private Long categoryId;
    /**
     * 所属分类
     */
    @JsonIgnore
    @Setter
    @ToString.Exclude
    @Getter
    private Category category;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private CategoryDao categoryDao;
    /**
     * @author jyx
     */
    @JsonIgnore
    public Category getCategory(){
        if (Objects.isNull(this.categoryId)){
            return null;
        }

        if (Objects.isNull(this.category) && Objects.nonNull(this.categoryDao)){
            this.category = this.categoryDao.findById(this.categoryId);
        }
        return this.category;
    }

    @JsonIgnore
    public List<Activity> getActList(){
        if (Objects.nonNull(this.getValidOnsale())) {
            return this.getValidOnsale().getActList();
        }
        return new ArrayList<>();
    }

    @Setter
    @Getter
    private Long shopId;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    @Getter
    private Shop shop;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    private ShopDao shopDao;
    /**
     * @author jyx
     */
    @JsonIgnore
    public Shop getShop(){
        if (Objects.isNull(this.shopId)){
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA, String.format(ReturnNo.INCONSISTENT_DATA.getMessage(), "产品", this.id, "shopId为空"));
        }

        if (Objects.isNull(this.shop) && Objects.nonNull(this.shopDao)){
            this.shop = this.shopDao.findById(this.shopId);
        }
        return this.shop;
    }

    @Setter
    @Getter
    private Long shopLogisticId;

    /**
     *@author jyx
     */
    @JsonIgnore
    @ToString.Exclude
    @Setter
    @Getter
    private Logistics logistics;
    /**
     *@author jyx
     */
    @JsonIgnore
    @ToString.Exclude
    @Setter
    private FreightDao freightDao;

    @JsonIgnore
    public Logistics getLogistics(){
        if(Objects.isNull(this.shopId)){
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA, String.format(ReturnNo.INCONSISTENT_DATA.getMessage(), "产品", this.id, "shopId为空"));
        } else if (Objects.isNull(this.shopLogisticId)) {
            return null;
        }
        if (Objects.isNull(this.logistics) && Objects.nonNull(this.freightDao)){
            try {
                this.logistics = this.freightDao.GetLogistics(this.shopId,this.shopLogisticId);
            } catch (BusinessException e) {
                if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()) {
                    throw new BusinessException(ReturnNo.INCONSISTENT_DATA, String.format(ReturnNo.INCONSISTENT_DATA.getMessage(), "产品", this.id, "shopLogisticId无法找到对应物流渠道"));
                }
            }
        }
        return this.logistics;
    }

    @Setter
    @Getter
    private Long templateId;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    @Getter
    private Template template;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    private TemplateDao templateDao;
    /**
     *@author jyx
     */
    @JsonIgnore
    public Template getTemplate() {
        if (Objects.isNull(this.shopId)){
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA, String.format(ReturnNo.INCONSISTENT_DATA.getMessage(), "产品", this.id, "shopId为空"));
        }
        if (Objects.isNull(this.templateId)){
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA, String.format(ReturnNo.INCONSISTENT_DATA.getMessage(), "产品", this.id, "templateId为空"));
        }
        if (Objects.isNull(this.template) && Objects.nonNull(this.templateDao)){
            this.template = this.templateDao.findById(this.shopId, this.templateId);
        }
        return this.template;
    }


    @Setter
    @Getter
    private Long freeThreshold;

    public void ban(){
        if(this.status.equals(BANNED)){
            throw new BusinessException(ReturnNo.STATENOTALLOW,String.format(ReturnNo.STATENOTALLOW.getMessage(),"BANNED",this.id));
        }
        this.status = BANNED;
    }

    public void allow(){
        if(this.status.equals(ALLOW)){
            throw new BusinessException(ReturnNo.STATENOTALLOW,String.format(ReturnNo.STATENOTALLOW.getMessage(),"ALLOW",this.id));
        }
        this.status = ALLOW;
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
