//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.controller.dto.OrderInfoDto;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.activity.ActivityDao;
import cn.edu.xmu.oomall.product.dao.bo.*;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import cn.edu.xmu.oomall.product.model.strategy.Item;
import lombok.RequiredArgsConstructor;
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
public class CouponActService {


    private final ActivityDao activityDao;
    private final ProductDao productDao;
    private final OnSaleDao onsaleDao;
    private final RedisUtil redisUtil;

    /**
     * 新建己方优惠活动
     *
     * @param couponAct 优惠活动
     * @param creator 操作者
     */
    public CouponAct addCouponactivity(CouponAct couponAct, UserDto creator) {
        return this.activityDao.insert(couponAct, creator);
    }

    /**
     * 返回店铺的所有状态优惠活动列表
     *
     * @param shopId    商户Id
     * @param productId 商品id
     * @param status 状态
     * @param page      页码
     * @param pageSize  每页数目
     */
    @Transactional
    public List<CouponAct> retrieveByShopIdAndProductId(Long shopId, Long productId, Integer status, Integer page, Integer pageSize) {
        //通过productId查询活动对象
        return this.activityDao.retrieveByShopIdAndProductId(shopId, productId,CouponAct.ACTCLASS, status, page, pageSize);
    }

    /**
     * 根据id返回优惠活动
     *
     * @param shopId 商户Id
     * @param id     活动Id
     * @return
     */
    @Transactional
    public CouponAct findById(Long shopId, Long id) throws BusinessException {
        return this.activityDao.findById(id, shopId, CouponAct.ACTCLASS);
    }

    /**
     * 修改己方某优惠活动
     *
     * @param shopId 商户Id
     * @param id     活动Id
     */
    public void updateCouponActivityById(Long shopId, Long id, CouponAct couponAct, UserDto modifier) {

        Activity activity = this.activityDao.findById(id, shopId, CouponAct.ACTCLASS);
        couponAct.setId(id);
        couponAct.setObjectId(activity.getObjectId());
        couponAct.setActClass("couponActDao");

        String key = this.activityDao.save(couponAct, modifier);
        this.redisUtil.del(key);
    }

    /**
     * 取消己方某优惠活动：取消优惠活动并未修改Activity，修改onsale
     *
     * @param shopId 商户Id
     * @param id     活动Id
     *
     */
    public void cancelCouponAct(Long shopId, Long id){

        CouponAct act = this.activityDao.findById(id, shopId, CouponAct.ACTCLASS);
        act.cancel();
    }

    /**
     * 计算商品优惠价格
     * 2023-12-09
     * @author yuhao shi
     * dgn2-010-syh
     * *@param id 活动Id
     */
    public List<Item> cacuCoupon(Long id, List<OrderInfoDto> orderInfoDtoList){
        Activity activity=this.activityDao.findById(id, PLATFORM, CouponAct.ACTCLASS);
        List<Item> itemList= orderInfoDtoList.stream().map(obj ->{
                OnSale onSale = this.onsaleDao.findById(PLATFORM, obj.getOnsaleId());
                Product product=this.productDao.findByOnsale(onSale);
                Item item= CloneFactory.copy(new Item(), product);
                item.setQuantity(obj.getQuantity());
                item.setOnsaleId(obj.getOnsaleId());
                item.setCouponActivityId(activity.getId());
                return item;
        }).collect(Collectors.toList());

        BaseCouponDiscount baseCouponDiscount= ((CouponAct) activity).getStrategy();
        baseCouponDiscount.compute(itemList);

        return itemList;
    }

    /**
     * 查询优惠活动
     * @param shopId 商铺id
     * @param productId 产品id
     * @param beginTime 晚于结束时间
     * @param endTime 早于结束时间
     * @param page 页
     * @param pageSize 每页数据
     * @return 优惠活动
     */
    public List<CouponAct> retrieveValidByShopIdAndProductIdAndTime(Long shopId, Long productId, LocalDateTime beginTime,LocalDateTime endTime,Integer page,Integer pageSize){
        return this.activityDao.retrieveValidByShopIdAndProductId(shopId, productId, CouponAct.ACTCLASS, beginTime, endTime, page, pageSize);
    }

    /**
     * 查询活动中的商品
     * @param id 活动id
     * @return 商品
     */
    public List<Product> retrieveProduct(Long id){
        CouponAct act = this.activityDao.findById(id, PLATFORM, CouponAct.ACTCLASS);
        List<Product> productList = act.getOnsaleList().stream().map(onsale -> this.productDao.findByOnsale(onsale)).collect(Collectors.toList());
        return productList;
    }

    /**
     * 发布活动
     * @param id 活动id
     * @param user 操作者
     */
    public void publishCouponAct(Long id, UserDto user){
        CouponAct act = this.activityDao.findById(id, PLATFORM, CouponAct.ACTCLASS);
        CouponAct updateAct = act.publish();
        String key = this.activityDao.save(updateAct, user);
        this.redisUtil.del(key);
    }

    /**
     * 管理员将优惠活动加到销售上
     * 注：在dao层实现了对于错误情况的处理，此处无需处理act和onsale
     *
     * @param shopId 商户Id
     * @param actId     活动Id
     * @param onsaleId  销售Id
     * @param creator   操作者
     *
     */
    public void addCouponActToOnsale(Long shopId, Long actId, Long onsaleId, UserDto creator) {
        CouponAct act = this.activityDao.findById(actId, shopId, CouponAct.ACTCLASS);
        OnSale onsale = this.onsaleDao.findById(shopId, onsaleId);
        this.activityDao.insertActivityOnsale(actId, onsaleId, creator);
    }

    /**
     * 管理员将优惠活动从销售上移除
     *
     *
     */
    public void delCouponActFromOnsale(Long shopId, Long actId, Long onsaleId, UserDto deleter) {
        CouponAct act = this.activityDao.findById(actId, shopId, CouponAct.ACTCLASS);
        OnSale onsale = this.onsaleDao.findById(shopId, onsaleId);
        this.activityDao.deleteActivityOnsale(actId, onsaleId, deleter);
    }
}