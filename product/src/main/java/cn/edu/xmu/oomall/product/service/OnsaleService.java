//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class OnsaleService {


    private final OnSaleDao onsaleDao;
    private final ProductDao productDao;
    private final RedisUtil redisUtil;

    /**
     * 根据id获得onsale对象
     * @param shopId 店铺id
     * @param id onsale id
     * @return onsale对象
     */
    public OnSale findById(Long shopId, Long id){
        return this.onsaleDao.findById(shopId, id);
    }

    /**
     * 管理员新增商品价格和数量,默认状态为暂停状态
     * @param shopId 商铺id
     * @param productId 商品id
     * @param onsale onsale对象
     * @param user 操作者
     * @return 新onsale对象
     */
    public OnSale insert(Long shopId, Long productId, OnSale onsale, UserDto user) {

        Product product=this.productDao.findNoOnsaleById(shopId, productId);
        onsale.setProductId(productId);
        onsale.setShopId(shopId);
        log.debug("insert: onsale = {}", onsale);
        OnSale newOnsale = product.createOnsale(onsale, user);
        return newOnsale;
    }

    /**
     * 根据商品id得到Onsale集合
     * @param shopId 商铺id
     * @param productId 商品id
     * @param page
     * @param pageSize
     * @return
     */
    public List<OnSale> retrieveByProductId(Long shopId, Long productId, Integer page, Integer pageSize) {
        log.debug("retrieveOnSale: shopId = {}, productId = {}", shopId, productId);
        Product product = this.productDao.findNoOnsaleById(shopId, productId);
        List<OnSale> onsales = this.onsaleDao.retrieveByProductId(productId, page, pageSize);
        return onsales;
    }

    /**
     * 取消商品价格和数量
     * @param shopId 商铺id
     * @param id onsale id
     * @param user 操作者
     * @return
     */
    public void cancel(Long shopId, Long id, UserDto user) {
        OnSale onsale = this.onsaleDao.findById(shopId, id);
        String key = onsale.cancel(LocalDateTime.now(), user);
        redisUtil.del(key);
    }

    /**
     * 删除销售
     * 在send-delete-onsale-topic发送消息看是否存在订单，如果不存在则在reply-del-onsale-topic收到回复去物理删除，如果未收到回复则不删除
     * @modify Rui Li
     * @task 2023-dgn2-007
     */
    public void tryToDel(Long shopId, Long id, UserDto user) {
        OnSale onsale = this.onsaleDao.findById(shopId, id);
        onsaleDao.tryToDel(onsale,user);
    }

    /**
     * 物理删除onsale 对象
     * @param id onsale id
     * @param user 操作者
     */
    public void delete(Long id, UserDto user) {
        this.onsaleDao.delete(id);
    }

    /**
     * 增减quantity
     * @author Ming Qiu
     * @modify Rui Li
     * @task 2023-dgn2-007
     * <p>
     * date: 2022-12-15 10:24
     * @param quantity
     */
    public void incrQuantity(Long onsaleId ,int quantity){
        OnSale onsale = this.onsaleDao.findById(PLATFORM, onsaleId);
        Integer newQuantity = onsale.getQuantity() + quantity;
        if (newQuantity < 0){
            log.error("incrQuantity: onsale id = {} is oversold", onsaleId);
            throw new BusinessException(ReturnNo.GOODS_STOCK_SHORTAGE, String.format(ReturnNo.GOODS_STOCK_SHORTAGE.getMessage(),onsale.getId()));
        }
        // 如果productId和起止时间都为空，save时hasConflictOnsale方法会报错
        OnSale newOnsale = OnSale.builder().id(onsaleId).quantity(newQuantity).productId(onsale.getProductId()).build();
        UserDto platformUser = UserDto.builder().id(1L).departId(PLATFORM).userLevel(1).name("admin").build();
        Product product = this.productDao.findNoOnsaleById(onsale.getShopId(), onsale.getProductId());
        String key = product.updateOnsale(newOnsale, platformUser);
        this.redisUtil.del(key);
    }

    /**
     * 查看活动中的商品（活动中的商品的定义是什么？？？？？）
     *
     */
    public List<Product> getCouponActProduct(Long id, Integer page, Integer pageSize) {
        List<OnSale> onsaleList = this.onsaleDao.retrieveByActId(id, page, pageSize);
        List<Product> productList = onsaleList.stream()
                .map(onsale -> this.productDao.findByOnsale(onsale)).collect(Collectors.toList());
        return productList;
    }




}
