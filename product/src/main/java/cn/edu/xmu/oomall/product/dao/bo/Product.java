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

    private String skuSn;

    private String name;

    private Long originalPrice;

    private Long weight;

    private String barcode;

    private String unit;

    private String originPlace;

    @Setter
    @CopyFrom.Exclude({ProductDraft.class, ProductDto.class, ProductPo.class})
    private Integer commissionRatio;

    public Integer getCommissionRatio(){
        if (null == this.commissionRatio && null != getCategory()){
            this.commissionRatio = this.getCategory().getCommissionRatio();
        }
        return this.commissionRatio;
    }

    private Byte status;

    public void setStatus(Byte status){
        this.status = status;
    }

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
        this.hasConflictOnsale(onsale);
        log.debug("createOnsale: onsale = {}", onsale);
        return this.onsaleDao.insert(onsale,user);
    }

    /**
     * @author :37220222203708
     */
    public String updateOnsale(OnSale onsale, UserDto user) {
        this.hasConflictOnsale(onsale);
        log.debug("updateOnsale: onsale = {}", onsale);
        return this.onsaleDao.save(onsale,user);
    }



    /**
     *  //判断是否存在时间重叠的onsale
     * @param onsale 判断的onsale对象
     * @throws BusinessException
     */
    @Setter
    @JsonIgnore
    @ToString.Exclude
    private OnsalePoMapper onsalePoMapper;
    private void hasConflictOnsale(OnSale onsale) throws BusinessException{
        log.debug("hasConflictOnsale: onsale = {}", onsale);
        if (Objects.isNull(onsale.getProductId()) && Objects.isNull(onsale.getBeginTime()) && Objects.isNull(onsale.getEndTime())){
            throw new IllegalArgumentException("OnsaleDao.hasConflictOnsale: onsale's productId, beginTime and endTime can not be null");
        }

        PageRequest pageable = PageRequest.of(0,MAX_RETURN, Sort.by(Sort.Direction.ASC, "beginTime"));
        List<OnsalePo> poList =  this.onsalePoMapper.findOverlap(onsale.getProductId(), onsale.getBeginTime(), onsale.getEndTime(), pageable);
        if (!poList.isEmpty()){
            log.debug("hasConflictOnsale: poList Size = {}, onsale's id = {}",poList.size(), onsale.getId());
            if (!Objects.isNull(onsale.getId())) {
                //修改的目标onsale不计算在重复范围内
                poList = poList.stream().filter(o -> !onsale.getId().equals(o.getId())).collect(Collectors.toList());
            }

            if (poList.size() > 0) {
                throw new BusinessException(ReturnNo.GOODS_ONSALE_CONFLICT, String.format(ReturnNo.GOODS_ONSALE_CONFLICT.getMessage(), poList.get(0).getId()));
            }
        }
    }
    private Long goodsId;
    /**
     * 相关商品
     */
    @JsonIgnore
    @ToString.Exclude
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

    private Long categoryId;
    /**
     * 所属分类
     */
    @JsonIgnore
    @Setter
    @ToString.Exclude
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

    private Long shopId;

    @JsonIgnore
    @ToString.Exclude
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
    /**
     *@author jyx
     */
    @JsonIgnore
    @ToString.Exclude
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

    private Long templateId;

    @JsonIgnore
    @ToString.Exclude
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

    private Long shopLogisticId;



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

    public String getSkuSn() {
        return skuSn;
    }

    public void setSkuSn(String skuSn) {
        this.skuSn = skuSn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public Long getShopLogisticId() {
        return shopLogisticId;
    }

    public void setShopLogisticId(Long shopLogisticId) {
        this.shopLogisticId = shopLogisticId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getFreeThreshold() {
        return freeThreshold;
    }

    public void setFreeThreshold(Long freeThreshold) {
        this.freeThreshold = freeThreshold;
    }
}
