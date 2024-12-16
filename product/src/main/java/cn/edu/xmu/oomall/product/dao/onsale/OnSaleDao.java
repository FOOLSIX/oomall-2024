//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao.onsale;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.activity.ActivityDao;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import cn.edu.xmu.oomall.product.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.product.mapper.jpa.ActivityOnsalePoMapper;
import cn.edu.xmu.oomall.product.mapper.jpa.OnsalePoMapper;
import cn.edu.xmu.oomall.product.mapper.po.OnsalePo;
import cn.edu.xmu.oomall.product.mapper.rocketmq.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Repository
@RefreshScope
@Slf4j
public class OnSaleDao {

    private static final String KEY = "O%d";
    private static final String VALID_KEY = "OV%d";

    @Value("${oomall.onsale.timeout}")
    private int timeout;

    private OnsalePoMapper onsalePoMapper;

    private ActivityDao activityDao;

    private ShopDao shopDao;

    private ProductDao productDao;

    private RedisUtil redisUtil;

    private ActivityOnsalePoMapper activityOnsalePoMapper;

    private OrderMapper orderMapper;

    @Autowired
    @Lazy
    public OnSaleDao(OnsalePoMapper onsalePoMapper, ActivityDao activityDao, ShopDao shopDao, ProductDao productDao, RedisUtil redisUtil, ActivityOnsalePoMapper activityOnsalePoMapper, OrderMapper orderMapper) {
        this.onsalePoMapper = onsalePoMapper;
        this.activityDao = activityDao;
        this.shopDao = shopDao;
        this.productDao = productDao;
        this.redisUtil = redisUtil;
        this.activityOnsalePoMapper = activityOnsalePoMapper;
        this.orderMapper = orderMapper;
    }

    /**
     * 由po对象获得bo对象
     * 主要是设置波对象中关联的dao对象，并把bo对象存在redis中
     * @param po po对象
     * @param redisKey redis的key，如果是null就不存redis
     * @return bo对象
     */
    private OnSale build(OnsalePo po, Optional<String> redisKey){
        OnSale bo = new OnSale();
        CloneFactory.copy(bo, po);
        this.build(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    /**
     * 设置bo对象关联的dao对象
     * @param bo bo对象
     */
    private OnSale build(OnSale bo){
        bo.setActivityDao(this.activityDao);
        bo.setShopDao(this.shopDao);
        bo.setProductDao(this.productDao);
        bo.setOnSaleDao(this);
        return bo;
    }

    /**
     * 计算过期时间，应该不超过onsale的endtime
     * @author Ming Qiu
     * <p>
     * date: 2022-12-04 19:20
     * @param endTime
     * @return
     */
    private Long getNewTimeout(LocalDateTime endTime) {
        Long diff = Duration.between(LocalDateTime.now(), endTime).toSeconds();
        Long newTimeout = Math.min(this.timeout,  diff);
        return newTimeout;
    }


    /**
     * 获得货品的最近的价格和库存
     *
     * @param productId 商品id
     * @return 规格对象
     */
    public OnSale findLatestValidOnsaleByProductId(Long productId) throws RuntimeException{
        log.debug("findLatestValidOnsale: id ={}",productId);
        String key = String.format(VALID_KEY, productId);

        Integer onsaleId = (Integer) redisUtil.get(key);
        if (onsaleId != null && !onsaleId.equals(OnSale.NOTEXIST.intValue())) {
            try {
                OnSale bo = this.findById(PLATFORM, Long.valueOf(onsaleId));
                build(bo);
                return bo;
            } catch (BusinessException e) {
                if (ReturnNo.RESOURCE_ID_NOTEXIST != e.getErrno()) {
                    throw e;
                }
            }
            return OnSale.builder().id(OnSale.NOTEXIST).build();
        }

        Pageable pageable = PageRequest.of(0, MAX_RETURN, Sort.by("beginTime").ascending());
        LocalDateTime now = LocalDateTime.now();
        List<OnsalePo> retObj = this.onsalePoMapper.findByProductIdEqualsAndEndTimeAfter(productId, now, pageable);
        if (retObj.isEmpty()){
            redisUtil.set(key, OnSale.NOTEXIST, timeout);
            return OnSale.builder().id(OnSale.NOTEXIST).build();
        }else{
            OnsalePo po = retObj.stream().limit(1).collect(Collectors.toList()).get(0);
            OnSale bo =  this.build(po, Optional.ofNullable(null));
            redisUtil.set(key, bo.getId(), getNewTimeout(bo.getEndTime()) );
            return bo;
        }
    }

    /**
     * 用id找对象
     * @author Ming Qiu
     * <p>
     * date: 2022-12-04 8:34
     * @param shopId 店铺id
     * @param id onsale id
     * @return
     * @throws RuntimeException
     */
    public OnSale findById(Long shopId, Long id) throws RuntimeException {
        log.debug("findById: id ={}", id);

        String key = String.format(KEY, id);
        OnSale bo = (OnSale) redisUtil.get(key);
        if (bo != null) {
            if (!Objects.equals(shopId, bo.getShopId()) && !PLATFORM.equals(shopId)){
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,
                        String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "销售", id, shopId));
            }
            build(bo);
            return bo;
        }

        Optional<OnsalePo> retObj = this.onsalePoMapper.findById(id);
        if (retObj.isEmpty() ){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "销售", id));
        }else{
            OnsalePo po = retObj.get();
            if (!shopId.equals(po.getShopId()) && !PLATFORM.equals(shopId)){
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "销售",id, shopId));
            }
            return this.build(po, Optional.of(key));
        }
    }
    /*
     * 保存Onsale到数据库中去
     * @param onsale Onsale对象
     * @param user 操作管理员对象
     * @return new onsale object with id
     */
    public OnSale insert(OnSale onsale, UserDto user) {
        log.debug("insert: onsale={}",onsale);
        onsale.setCreator(user);
        onsale.setGmtCreate(LocalDateTime.now());
        OnsalePo onsalePo = CloneFactory.copy(new OnsalePo(), onsale);
        onsalePo.setId(null);
        OnsalePo newPo = this.onsalePoMapper.save(onsalePo);
        log.debug("insert: newPo={}", newPo);
        onsale.setId(newPo.getId());
        return onsale;
    }



    /**
     * 修改Onsale,保存到数据库
     * @param onsale
     * @param user
     * @return redisKey
     */
    public String save(OnSale onsale, UserDto user) throws BusinessException{
        log.debug("save: onsale={}",onsale);
        onsale.setModifier(user);
        onsale.setGmtModified(LocalDateTime.now());
        OnsalePo onsalePo = CloneFactory.copy(new OnsalePo(), onsale);
        OnsalePo newPo = this.onsalePoMapper.save(onsalePo);
        if (OnSale.NOTEXIST.equals(newPo.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "销售", onsalePo.getId()));
        }
        return String.format(KEY,newPo.getId());
    }

    /**
     * 根据productId 获得Onsale对象集合,按照开始时间倒序
     * @param productId 产品id
     * @param page 页
     * @param pageSize 每页数量
     * @return 查询的onsale对象
     */
    public List<OnSale> retrieveByProductId(Long productId, Integer page, Integer pageSize) {
        Pageable pageable=PageRequest.of(page - 1,pageSize, Sort.by("beginTime").descending());
        List<OnsalePo> retObj = this.onsalePoMapper.findByProductIdIs(productId, pageable);
        return retObj.stream().map(po -> CloneFactory.copy(new OnSale(), po)).collect(Collectors.toList());
    }

    /**
     * 按照活动id返回Onsale
     * @param actId 活动id
     * @param page 页数，从第1页开始
     * @param pageSize 每页数据
     * @return 返回onsale对象
     */
    public List<OnSale> retrieveByActId(Long actId, Integer page, Integer pageSize) throws RuntimeException {
        log.debug("retrieveByActId: actId = {}, page = {}, pageSize = {}",actId, page, pageSize);
        if (null == actId) {
            return null;
        }
        Pageable pageable=PageRequest.of(page - 1,pageSize);
        List<OnsalePo> actOnsalePos = this.onsalePoMapper.findByActIdEquals(actId, pageable);
        return actOnsalePos.stream().map(po -> this.build(po, Optional.empty())).collect(Collectors.toList());
    }

    /**
     * 删除onsale
     * 在send-delete-onsale-topic发送消息看是否存在订单，如果不存在则在reply-del-onsale-topic收到回复去物理删除，如果未收到回复则不删除
     * @author Rui Li
     * @task 2023-dgn2-007
     */
    public void tryToDel(OnSale onSale, UserDto user)
    {
        this.orderMapper.sendDelMessage(onSale,user);
    }

    /**
     * 物理删除onsale对象
     * @param onsaleId
     */
    public void delete(Long onsaleId){
        this.onsalePoMapper.deleteById(onsaleId);
    }

    /**
     * 删除所有相关的onsale
     * @param actId 活动id
     * @throws BusinessException
     */
    public void deleteRelateOnsales(Long actId) {
        this.activityOnsalePoMapper.deleteByActIdEquals(actId);
    }

    /**
     * 根据活动ID和产品Id找到对应的销售
     * @param activityId
     * @param productId
     * @return
     *
     * @author WuTong
     * @task 2023-dgn2-008
     */
    public OnSale findByActIdEqualsAndProductIdEquals(Long activityId, Long productId) {
        OnsalePo onsalePo = this.onsalePoMapper.findByActIdEqualsAndProductIdEquals(activityId, productId);
        if (!Objects.isNull(onsalePo)) {
            String key = String.format(KEY, onsalePo.getId());
            return this.build(onsalePo, Optional.of(key));
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "销售", onsalePo.getId()));
        }
    }
}
