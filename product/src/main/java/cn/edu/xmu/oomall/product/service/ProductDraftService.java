//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.returnval.TwoTuple;
import cn.edu.xmu.oomall.product.dao.CategoryDao;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.ProductDraftDao;
import cn.edu.xmu.oomall.product.dao.bo.Category;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.dao.bo.ProductDraft;
import cn.edu.xmu.oomall.product.mapper.openfeign.SearchMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.ProductEs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 草稿产品
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class ProductDraftService {


    private final ProductDraftDao productDraftDao;

    private final CategoryDao categoryDao;

    private final ProductDao productDao;

    private final RedisUtil redisUtil;

    private final SearchMapper searchMapper;


    /**
     * 商铺管理员申请增加新的Product
     * @param shopId 商铺id
     * @param draft 草稿对象
     * @param user 操作用户
     * @return
     */
    public ProductDraft createDraft(Long shopId, ProductDraft draft, UserDto user) {

        log.debug("createDraft: shopId = {}, draft = ", shopId, draft);
        Category category = this.categoryDao.findById(draft.getCategoryId());
        if(category.beFirstClassCategory()){
            throw new BusinessException(ReturnNo.CATEGORY_NOTALLOW, String.format(ReturnNo.CATEGORY_NOTALLOW.getMessage(), category.getId()));
        }

        ProductDraft newObj = this.productDraftDao.insert(draft, user);
        return newObj;
    }

    /**
     * 管理员或店家物理删除审核中的Products
     * @author wuzhicheng
     * @param shopId
     * @param id
     * @param user
     */
    public void delDraftProduct(Long shopId, Long id, UserDto user) {
        log.debug("delProducts: shopId = {}, productId = {}", shopId, id);
        this.productDraftDao.findById(id, shopId);
        this.productDraftDao.delete(id);
    }

    /**
     * 店家或管理员修改审核货品信息
     * @param shopId 商铺id
     * @param productDraft 修改对象
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void modifyById(Long shopId, ProductDraft productDraft, UserDto user) {
        log.debug("modifyById: shopId = {}, productDraft = {}", shopId, productDraft);
        this.productDraftDao.findById(productDraft.getId(), shopId);
        Category category = this.categoryDao.findById(productDraft.getCategoryId());
        if(category.beFirstClassCategory()){
            throw new BusinessException(ReturnNo.CATEGORY_NOTALLOW, String.format(ReturnNo.CATEGORY_NOTALLOW.getMessage(), productDraft.getCategoryId()));
        }
        this.productDraftDao.save(productDraft, user);
    }

    /**
     * 店家查询草稿商品
     * @param shopId
     * @param page
     * @param pageSize
     * @return
     */
    public List<ProductDraft> getAllProductDraft(Long shopId, Integer page, Integer pageSize) {
        return this.productDraftDao.retrieveProductDraftByShopId(shopId, page, pageSize);
    }

    /**
     * 店家查看草稿货品信息详情
     * @param shopId
     * @param id
     * @return
     */
    public ProductDraft getProductDraft(Long shopId, Long id) {
        ProductDraft draft = this.productDraftDao.findById(id, shopId);
        return draft;
    }

    /**
     * 货品发布
     * @param shopId 商铺id
     * @param id 审核商品id
     * @param commissionRatio 分账比例（可以为空）
     * @param user 操作用户
     * @return 返回商品的id和名称
     */
    public Product publishProduct(Long shopId, Long id, Integer commissionRatio, UserDto user) {
        log.debug("putGoods: draftProductId = {}", id);
        ProductDraft productDraft = this.productDraftDao.findById(id, shopId);
        TwoTuple<Product, String> retVal = productDraft.publish(commissionRatio, user);
        if (null != retVal.second){
            redisUtil.del(retVal.second);
        }

        // 获取发布后的商品
        Product publishedProduct = retVal.first;

        // 同步商品到 Elasticsearch
        ProductEs productEs = new ProductEs();
        productEs.setId(publishedProduct.getId());
        productEs.setShopId(publishedProduct.getShopId());
        productEs.setName(publishedProduct.getName());
        productEs.setBarcode(publishedProduct.getBarcode());

        ReturnObject result = searchMapper.saveOrUpdateProduct(productEs);


        return retVal.first;
    }

    /**
     * 店家修改需审核的货品信息
     *
     * @param shopId 商铺id
     * @param id 商品id
     * @param user 操作用户
     * @param draft 修改的属性
     */
    public ProductDraft updateProduct(Long shopId, Long id, UserDto user, ProductDraft draft) {
        log.debug("updateProduct: productId = {}, draft = {}", id, draft);
        //查询Product,防止修改其他商铺的商品或商品不存在
        this.productDao.findNoOnsaleById(shopId, id);
        draft.setProductId(id);
        return this.createDraft(shopId, draft, user);
    }
}
