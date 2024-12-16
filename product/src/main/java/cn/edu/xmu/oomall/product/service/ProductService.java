//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.BloomFilter;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.bo.*;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class ProductService {



    private final ProductDao productDao;
    private final RedisUtil redisUtil;
    private final SnowFlakeIdWorker snowFlakeIdWorker;
    private final OnSaleDao onSaleDao;

    /**
     * 根据id获得product对象
     *
     * @param shopId 商铺id
     * @param id product id
     * @return prodcuct对象
     * @throws BusinessException
     */
    public Product findProductById(Long shopId, Long id) throws BusinessException {
        log.debug("findProductById: id = {}", id);
        String key = BloomFilter.PRETECT_FILTERS.get("ProductId");
        if (redisUtil.bfExist(key, id)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "产品", id));
        }
        Product bo = null;
        try {
            bo = this.productDao.findValidById(shopId, id);
        } catch (BusinessException e) {
            if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()) {
                redisUtil.bfAdd(key, id);
            }
            throw e;
        }

        return bo;
    }

    /**
     * 查找有效的商品
     * 采用逻辑分页的方式，前提是少部分数据会被滤去
     * @param shopId
     * @param barCode
     * @param page
     * @param pageSize
     * @return
     * @author wuzhicheng
     */
    public List<Product> retrieveValidProducts(Long shopId, String barCode, String name, Integer page, Integer pageSize) {
        log.debug("retrieveProducts: shopId = {}, barCode = {}, name = {}", shopId, barCode, name);
        //实际读取的数据, 页数越靠后放大系数越小，前5页会放大超过2倍，
        int actualPageSize = page * pageSize * (1 + 5/page);
        List<Product> productList = this.productDao.retrieveByShopIdAndBarCodeAndName(shopId, barCode, name, 1, actualPageSize).stream().filter(o -> Product.ONSHELF == o.getStatus()).collect(Collectors.toList());
        int initialSize = productList.size();
        if (0 == initialSize ){
            //没有查到则直接返回
            return productList;
        }else if (page * pageSize <= initialSize){
            //查到的数据大于返回的数据的位置
            return productList.subList((page - 1) * pageSize, page * pageSize);
        }else {
            //查到的数据小于返回的数据的位置，则放大两倍再查一次
            productList = this.productDao.retrieveByShopIdAndBarCodeAndName(shopId, barCode, name, 1, actualPageSize * 2).stream().filter(o -> Product.ONSHELF == o.getStatus()).collect(Collectors.toList());
            if ((page - 1) * pageSize > productList.size()){
                //查到数据依然小于返回的位置
                return new ArrayList<>();
            }else {
                //计算位置和返回数据中的小值
                int endIndex = Math.min(page * pageSize, productList.size());
                return productList.subList((page - 1) * pageSize, endIndex);
            }
        }
    }

    /**
     * 店家修改货品信息
     *
     * @param shopId 商铺id
     * @param id 商品id
     * @param user 操作用户
     * @param product 修改的属性
     */
    public void updateProduct(Long shopId, Long id, UserDto user, Product product) {
        log.debug("updateProduct: productId = {}, product = {}", id, product);
        //查询Product,防止修改其他商铺的商品或商品不存在
        this.productDao.findNoOnsaleById(shopId, id);
        product.setId(id);
        String key = this.productDao.save(product, user);
        redisUtil.del(key);
    }


    /**
     * 查询商品的运费模板
     *
     * @param shopId
     * @param id
     * @return
     * @author wuzhicheng
     */
    public Template getProductTemplate(Long shopId, Long id) {
        log.debug("getProductTemplate: productId = {}", id);
        Product productById = this.productDao.findValidById(shopId, id);

        if (!Objects.equals(productById.getShopId(), shopId) && shopId != PLATFORM) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商品", id, shopId));
        }
        return productById.getTemplate();
    }

    /**
     * 店家查看运费模板用到的商品
     *
     * @param shopId
     * @param id
     * @param page
     * @param pageSize
     * @return
     * @author wuzhicheng
     */
    public List<Product> getTemplateProduct(Long shopId, Long id, Integer page, Integer pageSize) {
        log.debug("getTemplateProduct: templateId = {}", id);

        return this.productDao.retrieveProductByTemplateId(shopId,id, page, pageSize);
    }
    /**
     * 店家查看使用特殊物流的商品
     *
     * @param shopId
     * @param id
     * @param page
     * @param pageSize
     * @return
     * @author wuzhicheng
     */
    public List<Product> getLogisticsProduct(Long shopId, Long id, Integer page, Integer pageSize){
        log.debug("getLogisticsProduct: Logistics Id = {}", id);

        List<Product> productList = this.productDao.retrieveProductByLogisticsIdAndShopId(shopId,id, page,pageSize);

        return productList;

    }


    /**
     * 管理员解禁商品
     *
     * @param productId 商品id
     * @param user 操作者
     * @return
     */
    public void allowProduct(Long productId, UserDto user) {
        Product product = this.productDao.findNoOnsaleById(PLATFORM, productId);
        product.allow();
        String key = this.productDao.save(product, user);
        redisUtil.del(key);
    }

    /**
     * 管理员禁售商品
     *
     * @param productId 商品Id
     * @param user 操作者
     * @return
     */
    public void prohibitProduct(Long productId, UserDto user) {
        Product product = this.productDao.findNoOnsaleById(PLATFORM, productId);
        product.ban();
        String key = this.productDao.save(product, user);
        redisUtil.del(key);
    }

    /**
     * 将两个商品设置为相关
     *
     * @param shopId 商铺id
     * @param id 第一个商品 id
     * @param productId 第二个商品id
     * @param user 操作者
     * @return
     */
    public void relateProductId(Long shopId, Long id, Long productId, UserDto user) {
        Product product1 = this.productDao.findNoOnsaleById(shopId, id);
        Product product2 = this.productDao.findNoOnsaleById(shopId,productId);

        Long goodsId1 = null;
        Long goodsId2 = null;
        List<String> keyList = new ArrayList<>(2);
        if (Product.NO_RELATE_PRODUCT == product1.getGoodsId() && Product.NO_RELATE_PRODUCT == product2.getGoodsId()) {
            //两个都需要更新
            goodsId1 = snowFlakeIdWorker.nextId();
            goodsId2 = goodsId1;
        } else if (Product.NO_RELATE_PRODUCT == product1.getGoodsId()) {
            //更新前面的
            goodsId1 = product2.getGoodsId();
        } else {
            //更新后面的
            goodsId2 = product1.getGoodsId();
        }

        Product updateProduct = new Product();
        if (null != goodsId1) {
            updateProduct.setId(id);
            updateProduct.setGoodsId(goodsId1);
            String key = this.productDao.save(updateProduct, user);
            keyList.add(key);
        }
        if (null != goodsId2) {
            updateProduct.setId(productId);
            updateProduct.setGoodsId(goodsId2);
            String key = this.productDao.save(updateProduct, user);
            keyList.add(key);
        }
        this.redisUtil.del(keyList.toArray(new String[keyList.size()]));
    }

    /**
     * 取消两个商品的相关
     *
     * @param shopId 商铺id
     * @param id 第一个商品 id
     * @param user 操作者
     * @return
     */
    public void delRelateProduct(Long shopId, Long id, UserDto user) {
        this.productDao.findNoOnsaleById(shopId, id);

        Product product2 = new Product();
        product2.setId(id);
        product2.setGoodsId(Product.NO_RELATE_PRODUCT);
        String key2 = this.productDao.save(product2, user);
        redisUtil.del(key2);
    }


    /**
     * 删除商品的运费模板
     *
     * @param templateId
     */
    public void removeTemplateId(Long shopId, Long templateId, UserDto userDto) {
        List<Product> productList = this.productDao.retrieveProductByTemplateId(shopId, templateId,0,MAX_RETURN);
        List<String> keys = productList.stream().map(o -> {
            Product updateProduct = new Product();
            updateProduct.setId(o.getId());
            updateProduct.setTemplateId(Product.NO_TEMPLATE);
            return this.productDao.save(o, userDto);
        }).collect(Collectors.toList());
        this.redisUtil.del(keys.toArray(new String[keys.size()]));
    }

    /**
     * 获取某个商品的历史销售信息
     *
     * @param onsaleId onsaleid
     * @return 商品对象
     */
    public Product findByOnsaleId(Long onsaleId) throws BusinessException {
        log.debug("findByOnsaleId: onsaleId = {}", onsaleId);
        String key = BloomFilter.PRETECT_FILTERS.get("OnsaleId");

        if (redisUtil.bfExist(key, onsaleId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "产品",onsaleId ));
        }
        Product product = null;
        try {
            OnSale onsale = this.onSaleDao.findById(PLATFORM, onsaleId);
            product = this.productDao.findByOnsale(onsale);
        } catch (BusinessException e) {
            if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()) {
                redisUtil.bfAdd(key, onsaleId);
            }
            throw e;
        }
        return product;
    }

    /**
     * 查看Category下的商品
     *
     * @param id
     * @param page
     * @param pageSize
     * @return
     * @author wuzhicheng
     */
    public List<Product> getCategoryProduct( Long id, Integer page, Integer pageSize){
        log.debug("getLogisticsProduct: Logistics Id = {}", id);

        return this.productDao.retrieveProductByCategory(id,page,pageSize);
    }
}
