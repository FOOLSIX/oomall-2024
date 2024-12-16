//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.bo.ProductDraft;
import cn.edu.xmu.oomall.product.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.product.mapper.jpa.ProductDraftPoMapper;
import cn.edu.xmu.oomall.product.mapper.po.ProductDraftPo;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductDraftDao {

    private final ProductDraftPoMapper productDraftPoMapper;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    private final ShopDao shopDao;

    private ProductDraft build(ProductDraftPo po){
        ProductDraft bo= new ProductDraft();
        CloneFactory.copy(bo, po);
        bo=this.build(bo);
        return bo;
    }

    private ProductDraft build(ProductDraft bo){
        bo.setCategoryDao(this.categoryDao);
        bo.setProductDao(this.productDao);
        bo.setShopDao(this.shopDao);
        bo.setProductDraftDao(this);
        return bo;
    }


    /**
     * 插入商品
     * @param productDraft
     * @param user
     * @return
     */
    public ProductDraft insert(ProductDraft productDraft, UserDto user){
        productDraft.setGmtCreate(LocalDateTime.now());
        productDraft.setCreator(user);
        ProductDraftPo productDraftPo = new ProductDraftPo();
        productDraft.setShopId(user.getDepartId());
        CloneFactory.copy(productDraftPo, productDraft);
        productDraft.setId(null);
        ProductDraftPo save = this.productDraftPoMapper.save(productDraftPo);
        ProductDraft draft = CloneFactory.copy(new ProductDraft(), save);
        return draft;
    }

    /**
     * 更新
     * @author wuzhicheng
     * @param productDraft
     * @param user
     * @return
     */
    public void save(ProductDraft productDraft, UserDto user) {
        productDraft.setGmtModified(LocalDateTime.now());
        productDraft.setModifier(user);
        ProductDraftPo productDraftPo = CloneFactory.copy(new ProductDraftPo(), productDraft);
        ProductDraftPo save = this.productDraftPoMapper.save(productDraftPo);
        if(save.getId()==null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(),"草稿商品", productDraft.getId()));
        }
    }

    /**
     * 根据id查询
     * @param id product draft id
     * @param shopId 店铺id
     * @return product draft obj
     */
    public ProductDraft findById(Long id, Long shopId) {
        log.debug("findById: id ={}", id);
        Optional<ProductDraftPo> retObj = this.productDraftPoMapper.findById(id);
        if (retObj.isEmpty() ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "草稿商品", id));
        }else{
            ProductDraftPo po = retObj.get();
            if(!Objects.equals(po.getShopId(), shopId) && !PLATFORM.equals(shopId) ){
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "草稿商品", id, shopId));
            }
            return this.build(po);
        }
    }

    /**
     * 根据id物理删除
     * @author wuzhicheng
     * @param id
     */
    public void delete(Long id) {
        this.productDraftPoMapper.deleteById(id);
    }

    /**
     * 根据shopid查询草稿商品
     * @param shopId
     * @param page
     * @param pageSize
     * @return
     */
    public List<ProductDraft> retrieveProductDraftByShopId(Long shopId, Integer page, Integer pageSize) {
        List<ProductDraft> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.Direction.DESC, "gmtCreate");
        List<ProductDraftPo> pos = null;
        if (PLATFORM.equals(shopId)) {
            Page<ProductDraftPo> pageDraft =  this.productDraftPoMapper.findAll(pageable);
            if (!pageDraft.isEmpty()){
                pos = pageDraft.toList();
            }
        }else{
            pos = this.productDraftPoMapper.findByShopIdEquals(shopId, pageable);
        }
        if(null != pos) {
            ret = pos.stream().map(this::build).collect(Collectors.toList());
            log.debug("bos size:{}", ret.size());
        }
        return ret;
    }

}
