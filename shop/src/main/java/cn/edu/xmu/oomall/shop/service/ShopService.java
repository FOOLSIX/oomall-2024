package cn.edu.xmu.oomall.shop.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.shop.dao.ShopDao;
import cn.edu.xmu.oomall.shop.dao.bo.Shop;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class ShopService {

    private static  final Logger logger = LoggerFactory.getLogger(ShopService.class);

    private final ShopDao shopDao;
    private final RedisUtil redisUtil;

    /**
     * 商铺申请商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public Shop createShop(Shop shop, UserDto user){
        List<Shop> shops = this.shopDao.retrieveByCreatorId(user.getId(), 1, 10);
        if(shops.size()!=0) {
            throw new BusinessException(ReturnNo.SHOP_USER_HASSHOP, String.format(ReturnNo.SHOP_USER_HASSHOP.getMessage(), user.getId()));
        }
        Shop ret = this.shopDao.insert(shop, user);
        return ret;
    }

    /**
     * 商户获得商铺信息
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public Shop findShopById(Long id){
        Shop ret = this.shopDao.findById(id);
        return ret;
    }

    /**
     * 顾客根据名称查询商铺（只返回status为ONLINE和OFFLINE的商铺）
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public List<Shop> findShopByName(String name, Integer page, Integer pageSize) {
        List<Byte> validStatus = List.of(Shop.ONLINE, Shop.OFFLINE);
        List<Shop> ret = this.shopDao.findByNameAndStatusIn(name, validStatus, page, pageSize);
        return ret;
    }

    /**
     * 商户修改商铺信息
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void updateShop(Shop shop, UserDto user){
        Shop oldShop = this.shopDao.findById(shop.getId());
        // 这里应该改成oldShop，因为oldShop中才保存着status
        if(oldShop.getStatus().equals(Shop.ABANDON)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "商铺", shop.getId(), shop.getStatusName()));
        }
        String key = this.shopDao.save(shop, user);
        this.redisUtil.del(key);
    }

    /**
     * 平台管理人员或商户上线商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void onlineShop(Long shopId, UserDto userDto){
        Shop shop = this.shopDao.findById(shopId);
        shop.online(userDto);
    }

    /**
     * 平台管理人员或商户下线商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void offlineShop(Long shopId, UserDto userDto){
        Shop shop = this.shopDao.findById(shopId);
        shop.offline(userDto);
    }

    /**
     * 平台管理人员审核商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void updateShopAudit(Long shopId, UserDto userDto, Boolean conclusion){
        if (conclusion) {
            Shop shop = this.shopDao.findById(shopId);
            shop.approved(userDto);
        }
    }

    /**
     * 平台管理人员或商户关闭商铺（只有处于下线状态的商铺才能关闭）
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void deleteShop(Long id, UserDto userDto){
        Shop shop = this.shopDao.findById(id);
        shop.abandon(userDto);
    }

    /**
     * 平台管理人员查询商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public List<Shop> findAllShops(Byte status, String name, Integer page, Integer pageSize) {
        List<Byte> validStatus;
        if (Objects.isNull(status)) {
            validStatus = List.of(Shop.OFFLINE, Shop.NEW, Shop.ONLINE, Shop.ABANDON);
        } else {
            validStatus = List.of(status);
        }
        List<Shop> shops = this.shopDao.findByNameAndStatusIn(name, validStatus, page, pageSize);
        return shops;
    }

}
