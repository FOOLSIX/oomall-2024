//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao.activity;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import cn.edu.xmu.oomall.product.dao.bo.*;
import cn.edu.xmu.oomall.product.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.product.mapper.jpa.ActivityOnsalePoMapper;
import cn.edu.xmu.oomall.product.mapper.jpa.ActivityPoMapper;
import cn.edu.xmu.oomall.product.mapper.po.ActivityOnsalePo;
import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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

import static cn.edu.xmu.javaee.core.model.Constants.*;

@Slf4j
@Repository
public class ActivityDao {

    private final static String KEY = "A%d";
    private final String ACTIVITY_LIST_KEY = "ACTIVITYLIST%d";
    @Value("${oomall.activity.timeout}")
    private int timeout;

    private RedisUtil redisUtil;

    private ApplicationContext context;

    private ActivityOnsalePoMapper activityOnsalePoMapper;

    private ActivityPoMapper activityPoMapper;

    private ShopDao shopDao;

    private OnSaleDao onsaleDao;

    @Autowired
    public ActivityDao(RedisUtil redisUtil, ApplicationContext context, ActivityOnsalePoMapper activityOnsalePoMapper,
                       ActivityPoMapper activityPoMapper, ShopDao shopDao, OnSaleDao onsaleDao) {
        this.redisUtil = redisUtil;
        this.context = context;
        this.activityOnsalePoMapper = activityOnsalePoMapper;
        this.activityPoMapper = activityPoMapper;
        this.shopDao = shopDao;
        this.onsaleDao = onsaleDao;
    }

    /**
     * 由po对象获得bo对象
     * 主要是设置bo对象中关联的dao对象，并把bo对象存在redis中
     *
     * @param po       po对象
     * @param redisKey redis的key，如果是null就不存redis
     * @return bo对象
     */
    private Activity build(ActivityPo po, Optional<String> redisKey) {
        log.debug("build: po = {}", po);
        ActivityInf inf = this.findActivityDao(po);
        Activity bo = inf.getActivity(po);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, this.timeout));
        this.build(bo);
        log.debug("build: bo = {}", bo);
        return bo;
    }

    /**
     * 设置bo对象关联的dao对象
     *
     * @param bo bo对象
     */
    private Activity build(Activity bo) {
        bo.setShopDao(this.shopDao);
        bo.setOnsaleDao(this.onsaleDao);
        return bo;
    }

    /**
     * 返回Bean对象
     *
     * @param po
     * @return
     * @author Ming Qiu
     * <p>
     * date: 2022-11-22 16:11
     */
    private ActivityInf findActivityDao(ActivityPo po) {
        return (ActivityInf) context.getBean(po.getActClass());
    }

    private ActivityInf findActivityDao(Activity bo) {
        return (ActivityInf) context.getBean(bo.getActClass());
    }

    /**
     * 获得销售对象的活动
     *
     * @param onsaleId
     * @return
     * @author Ming Qiu
     * <p>
     * date: 2022-11-27 16:54
     */
    public List<Activity> retrieveByOnsaleId(Long onsaleId) {
        assert (null != onsaleId) : new IllegalArgumentException();

        Pageable pageable = PageRequest.of(0, MAX_RETURN, Sort.by("gmtCreate").descending());

        String key = String.format(ACTIVITY_LIST_KEY, onsaleId);
        List<Long> actPosId = (ArrayList<Long>) redisUtil.get(key);
        if (actPosId == null) {
            List<ActivityPo> actPos = this.activityPoMapper.findByOnsaleIdEquals(onsaleId, pageable);
            redisUtil.set(key, new ArrayList<>(actPos.stream().map(ActivityPo::getId).toList()), timeout);
            return actPos.stream().map(po -> {
                log.debug("retrieveByOnsaleId: po = {}", po);
                String onSaleKey = String.format(KEY, po.getId());
                Activity bo = this.build(po, Optional.ofNullable(onSaleKey));
                return bo;
            }).collect(Collectors.toList());
        } else {
            List<Activity> g = actPosId.stream().map(i -> {
                String activityKey = String.format(KEY, i);
                Activity activity = (Activity) redisUtil.get(activityKey);

                if (activity != null) {
                    return activity;
                } else {
                    Optional<ActivityPo> activityPo = this.activityPoMapper.findById(i);
                    if (activityPo.isEmpty()) {
                        return null;
                    }
                    log.debug("retrieveByOnsaleId: po = {}", activityPo);
                    return this.build(activityPo.get(), Optional.ofNullable(activityKey));
                }
            }).toList();
            g = g.stream().filter(Objects::nonNull).toList();
            redisUtil.set(key, new ArrayList<>(g.stream().map(Activity::getId).toList()), timeout);
            return g;
        }

    }

    /**
     * 根据id获得对象
     *
     * @param id       活动id
     * @param shopId   商铺id
     * @param actClass 类型名称
     * @return 不同类型的活动对象
     * @author Ming Qiu
     * date: 2022-11-27 18:29
     */
    public <T extends Activity> T findById(Long id, Long shopId, String actClass) throws RuntimeException {
        assert (null != id && null != shopId) : new IllegalArgumentException();
        String key = String.format(KEY, id);
        Activity act = (Activity) redisUtil.get(key);
        if (act != null) {
            if (!shopId.equals(act.getShopId()) && !PLATFORM.equals(shopId)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,
                        String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "活动", id, shopId));
            }
            if (act.getActClass().equals(actClass)) {
                build(act);
                return (T) act;
            } else {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                        String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "活动", id));
            }
        }

        Optional<ActivityPo> ret = this.activityPoMapper.findById(id);
        if (ret.isPresent()) {
            ActivityPo po = ret.get();
            if (!shopId.equals(po.getShopId()) && !PLATFORM.equals(shopId)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,
                        String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "活动", id, shopId));
            }
            if (po.getActClass().equals(actClass)) {
                return (T) this.build(po, Optional.ofNullable(key));
            } else {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                        String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "活动", id));
            }
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "活动", id));
        }
    }

    /**
     * 更新对象：分别对 Activity 和 CouponAct 进行保存操作
     *
     * @param bo   更新的对象属性（其中not null的属性）
     * @param user
     * @param <T>  对象类型
     * @return 需删除的redis key
     * @throws RuntimeException
     */
    public <T extends Activity> String save(T bo, UserDto user) throws RuntimeException {
        if (null == user) {
            throw new IllegalArgumentException("user can not be null");
        }

        String key = String.format(KEY, bo.getId());
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        ActivityPo po = CloneFactory.copy(new ActivityPo(), bo);
        ActivityPo savedPo = this.activityPoMapper.save(po);
        if (savedPo.getId().equals(IDNOTEXIST)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                    String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "活动", bo.getId()));
        }
        log.debug("save:saved po = {}", savedPo);
        if (!Objects.isNull(bo.getObjectId())) {
            ActivityInf inf = this.findActivityDao(bo);
            inf.save(bo);
        }
        log.debug("save:saved bo = {}", bo);
        return key;
    }

    /**
     * 增加活动
     *
     * @param bo   活动对象
     * @param user 用户
     * @return 新增的活动对象
     * @throws RuntimeException
     */
    public <T extends Activity> T insert(T bo, UserDto user) throws RuntimeException {
        if (null == user) {
            throw new IllegalArgumentException("user can not be null");
        }
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());
        ActivityPo po = CloneFactory.copy(new ActivityPo(), bo);
        po.setStatus(CouponAct.NEW); // 对于新增的活动，将status设为NEW
        ActivityInf inf = this.findActivityDao(po);
        String objectId = inf.insert(bo);
        log.debug("insert: objectId = {}", objectId);
        po.setObjectId(objectId);
        po.setId(null);
        ActivityPo newPo = this.activityPoMapper.save(po);
        log.debug("insert: newPo = {}", newPo);
        bo.setId(newPo.getId());
        return bo;
    }

    /**
     * 增加活动与销售的关系
     *
     * @param actId    活动id
     * @param onsaleId 销售id
     * @param user     用户
     * @return 需要删除的key
     * @throws RuntimeException
     */
    public String insertActivityOnsale(Long actId, Long onsaleId, UserDto user) throws RuntimeException {
        if (null == user) {
            throw new IllegalArgumentException("user can not be null when create activity-onsale. ");
        }
        ActivityOnsalePo activityOnsalePo = ActivityOnsalePo.builder().onsaleId(onsaleId).actId(actId)
                .creatorId(user.getId()).creatorName(user.getName()).gmtCreate(LocalDateTime.now()).build();
        log.debug("insertActivityOnsale: activityOnsalePo = {}", activityOnsalePo);
        this.activityOnsalePoMapper.save(activityOnsalePo);
        return String.format(ACTIVITY_LIST_KEY, onsaleId);
    }

    /**
     * 去除活动与销售的关系
     *
     * @param actId    活动id
     * @param onsaleId 销售id
     * @param deleter  用户
     * @return 需删除的redis key
     * @throws RuntimeException
     * @author Fan ninghan
     */
    public String deleteActivityOnsale(Long actId, Long onsaleId, UserDto deleter) throws RuntimeException {
        if (null == deleter) {
            throw new IllegalArgumentException("user can not be null when delete activity-onsale. ");
        }
        this.activityOnsalePoMapper.deleteByActIdEqualsAndOnsaleIdEquals(actId, onsaleId);
        return String.format(ACTIVITY_LIST_KEY, onsaleId);
    }

    /**
     * 根据shopId和productId查询所有有效的活动
     * null则不作为条件查询
     *
     * @param shopId    商铺id
     * @param productId 商品id
     * @param beginTime 活动结束时间段的起
     * @param endTime   活动结束时间段的止
     * @return 分页的查询结果
     */
    public <T extends Activity> List<T> retrieveValidByShopIdAndProductId(Long shopId, Long productId, String actClass,
                                                                          LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) {
        if (null == actClass || page < 0 || pageSize <= 0 || Objects.isNull(beginTime) || Objects.isNull(endTime)) {
            log.error(
                    "retrieveValidByShopIdAndProductId: actClass, page, pageSize, beginTime, endTime can not be null");
            throw new IllegalArgumentException(
                    "ActivityDao.retrieveValidByShopIdAndProductId: page and pageSize can not be zero. and actClass cannot be null");
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<ActivityPo> pos;
        if (null != productId) {
            log.debug("retrieveValidByShopIdAndProductId: productId = {}", productId);
            pos = this.activityPoMapper.findValidByActClassEqualsAndProductIdEquals(actClass, productId, beginTime,
                    endTime, pageable);
        } else {
            if (null != shopId) {
                log.debug("retrieveValidByShopIdAndProductId: shopId = {}", shopId);
                pos = this.activityPoMapper.findValidByActClassEqualsAndShopIdEquals(actClass, shopId, beginTime,
                        endTime, pageable);

            } else {
                log.debug("retrieveValidByShopIdAndProductId: shopId is null");
                pos = this.activityPoMapper.findValidByActClassEquals(actClass, beginTime, endTime, pageable);
            }
        }
        log.debug("retrieveValidByShopIdAndProductId: pos size = {}", pos.size());
        if (pos.size() != 0) {
            List<T> actList = pos.stream().map(po -> {
                T bo = (T) this.build(po, Optional.ofNullable(null));
                log.debug("retrieveValidByShopIdAndProductId: bo = {}", bo);
                return bo;
            }).collect(Collectors.toList());
            return actList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 根据shopId和productId查询所有的活动
     * productId为null则不作为条件查询
     *
     * @param shopId    商铺id
     * @param productId 商品id
     * @param actClass  活动类型
     * @param status    状态
     * @param page      页码
     * @param pageSize  每页数目
     * @return 分页的查询结果
     * #
     */
    public <T extends Activity> List<T> retrieveByShopIdAndProductId(Long shopId, Long productId, String actClass,
                                                                     Integer status, Integer page, Integer pageSize) {
        assert (page > 0 && pageSize > 0) : new IllegalArgumentException();
        log.debug("retrieveByShopIdAndProductId: shopId = {}, productId = {}, page = {}, pageSize= {}", shopId,
                productId, page, pageSize);
        // JPA从第0页开始
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<ActivityPo> pos;
        if (null != productId) {
            log.debug("retrieveByShopIdAndProductId: productId = {}", productId);
            pos = this.activityPoMapper.findByActClassEqualsAndProductIdEqualsAndStatusEquals(actClass, productId,
                    status, pageable);
        } else {
            if (null != shopId) {
                if (PLATFORM != shopId) {
                    if (null != status) {
                        log.debug("retrieveByShopIdAndProductId: actClass & shopId & status");
                        pos = this.activityPoMapper.findByActClassEqualsAndShopIdEqualsAndStatusEquals(actClass, shopId,
                                status, pageable);
                    } else {
                        log.debug("retrieveByShopIdAndProductId: actClass & shopId");
                        pos = this.activityPoMapper.findByActClassEqualsAndShopIdEquals(actClass, shopId, pageable);
                    }
                } else {
                    if (null != status)
                        pos = this.activityPoMapper.findByActClassEqualsAndStatusEquals(actClass, status, pageable);
                    else
                        pos = this.activityPoMapper.findByActClassEquals(actClass, pageable);
                }

            } else {
                log.error("retrieveByShopIdAndProductId: shopId is null");
                pos = new ArrayList<>();
            }
        }
        log.debug("retrieveByShopIdAndProductId: pos size = " + pos.size());
        List<T> actList = pos.stream().map(po -> {
            if (!shopId.equals(po.getShopId()) && !shopId.equals(PLATFORM)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,
                        String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "活动", po.getId(), shopId));
            }
            T bo = (T) this.build(po, Optional.ofNullable(null));
            log.debug("retrieveByShopIdAndProductId: bo = {}", bo);
            return bo;
        }).collect(Collectors.toList());
        log.debug("retrieveByShopIdAndProductId: actList_size = {}", actList.size());
        return actList;
    }
}
