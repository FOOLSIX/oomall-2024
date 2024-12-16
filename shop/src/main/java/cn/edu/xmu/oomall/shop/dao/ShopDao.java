//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.shop.dao.bo.Shop;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.shop.mapper.ShopPoMapper;
import cn.edu.xmu.oomall.shop.mapper.po.ShopPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品Repository
 */
@Repository
@Slf4j
public class ShopDao {

    public static final String KEY = "S%d";

    @Value("${oomall.shop.shop.timeout}")
    private long timeout;

    private final RedisUtil redisUtil;

    private final ShopPoMapper shopPoMapper;

    private final RegionDao regionDao;

    private final TemplateDao templateDao;

    private final SnowFlakeIdWorker snowFlakeIdWorker;

    @Autowired
    @Lazy
    public ShopDao(RedisUtil redisUtil, ShopPoMapper shopPoMapper, RegionDao regionDao, TemplateDao templateDao, SnowFlakeIdWorker snowFlakeIdWorker) {
        this.redisUtil = redisUtil;
        this.shopPoMapper = shopPoMapper;
        this.regionDao = regionDao;
        this.templateDao = templateDao;
        this.snowFlakeIdWorker = snowFlakeIdWorker;
    }



    /**
     * 获得bo对象
     * @author Ming Qiu
     * <p>
     * date: 2022-11-20 11:46
     * @param po
     * @param redisKey
     * @return
     */
    private Shop build(ShopPo po, String redisKey){
        Shop ret = CloneFactory.copy(new Shop(), po);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.build(ret);
        return ret;
    }

    /**
     * 把bo中设置dao
     * @author Ming Qiu
     * <p>
     * date: 2022-11-20 11:46
     * @param bo
     */
    private Shop build(Shop bo){
        bo.setShopDao(this);
        bo.setRegionDao(this.regionDao);
        bo.setTemplateDao(this.templateDao);
        return bo;
    }

    /**
    * 按照id获得对象
    *
    * @param id shop id
    * @return Shop
    */
    public Shop findById(Long id){
        if (id.equals(null)) {
            throw new IllegalArgumentException("findById: id is null");
        }
        log.debug("findObjById: id = {}",id);
        String key = String.format(KEY, id);
        Shop shop = (Shop) redisUtil.get(key);
        if (!Objects.isNull(shop)) {
            shop = this.build(shop);
        } else {
            Optional<ShopPo> ret = this.shopPoMapper.findById(id);
            if (ret.isPresent()){
                shop = this.build(ret.get(), key);
            }else{
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺", id));
            }
        }
        return shop;
    }

    /**
     * 按照name和status获取商铺信息
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public List<Shop> findByNameAndStatusIn(String name, List<Byte> validStatus, Integer page, Integer pageSize) throws RuntimeException {
        List<Shop> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize);
        List<ShopPo> pos =  this.shopPoMapper.findByNameContainingAndStatusIn(name, validStatus, pageable);
        ret = pos.stream().map(po -> CloneFactory.copy(new Shop(),po)).collect(Collectors.toList());
        log.info("bos size:{}", ret.size());
        return ret;
    }

    /**
    * 按照创建者找到对象
    *
    * @param creatorId
    * @return List<Shop>
    */
    public List<Shop> retrieveByCreatorId(Long creatorId, Integer page, Integer pageSize) throws RuntimeException{
        List<Shop> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize);
        List<ShopPo> pos =  this.shopPoMapper.findByCreatorId(creatorId, pageable);
        ret = pos.stream().map(po -> CloneFactory.copy(new Shop(),po)).collect(Collectors.toList());
        log.info("bos size:{}", ret.size());
        return ret;
    }

    /**
     * 插入商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public Shop insert(Shop shop, UserDto userDto) throws RuntimeException{
        shop.setCreator(userDto);
        shop.setGmtCreate(LocalDateTime.now());
        shop.setStatus(Shop.NEW);
        ShopPo po = CloneFactory.copy(new ShopPo(), shop);
        po.setId(snowFlakeIdWorker.nextId());
        log.debug("insert: po = {}", po);
        this.shopPoMapper.save(po);
        shop.setId(po.getId());
        return shop;
    }

    /**
     * 商铺修改商铺信息
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public String save(Shop shop, UserDto userDto) throws RuntimeException{
        if (shop.getId().equals(null)){
            throw new IllegalArgumentException("save: shop id is null");
        }
        String key = String.format(KEY, shop.getId());
        shop.setModifier(userDto);
        shop.setGmtModified(LocalDateTime.now());
        ShopPo po = CloneFactory.copy(new ShopPo(),shop);
        log.debug("save: po = {}", po);
        this.shopPoMapper.save(po);
        return key;
    }
}
