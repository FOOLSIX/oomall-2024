package cn.edu.xmu.oomall.freight.dao;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.mapper.jpa.WarehousePoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.WarehousePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
@Slf4j
@RefreshScope
@RequiredArgsConstructor
public class WarehouseDao {

    private final static String KEY = "W%d";

    private final static String SHOP_KEY = "S%d";

    private final WarehousePoMapper warehousePoMapper;

    private final RedisUtil redisUtil;

    @Value("3600")
    private int timeout;

    public Warehouse findById(Long shopId, Long id) throws RuntimeException {
        Optional<WarehousePo> ret = warehousePoMapper.findById(id);
        if (ret.isPresent()) {
            WarehousePo po = ret.get();
            if(PLATFORM.equals(shopId) || po.getShopId().equals(shopId))
                return CloneFactory.copy(new Warehouse(), po);
            else throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE);
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * @author 37220222203558
     * 2024-dsg116-通过商铺id获取仓库列表
     */
    public List<Warehouse> findByShopId(Long shopId) throws RuntimeException {
        log.debug("findByShopId: shopId = {}", shopId);
        String key = String.format(SHOP_KEY, shopId);
        List<Long> warehouseIds = (List<Long>) redisUtil.get(key);
        if (Objects.isNull(warehouseIds)) {
            List<WarehousePo> poList = warehousePoMapper.findAllByShopId(shopId);
            List<Warehouse> boList = new ArrayList<>();
            List<Long> warehouseIdList = poList.stream().map(WarehousePo::getId).collect(Collectors.toList());
            for (WarehousePo po : poList) {
                boList.add(CloneFactory.copy(new Warehouse(), po));
            }
            Optional.of(key).ifPresent(buildKey -> redisUtil.set(buildKey, (Serializable) warehouseIdList, timeout));
            return boList;
        }
        else {
            List<Warehouse> warehouseList = new ArrayList<>();
            for (Long warehouseId : warehouseIds) {
                Optional<WarehousePo> ret = warehousePoMapper.findById(warehouseId);
                warehouseList.add(CloneFactory.copy(new Warehouse(), ret.get()));
            }
            return warehouseList;
        }
    }


    /**
     * @author 37220222203558
     * 2024-dsg116-新增仓库
     */
    public Warehouse insert(Warehouse bo, UserDto user) throws RuntimeException {
        bo.setId(null);
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());
        WarehousePo po = CloneFactory.copy(new WarehousePo(), bo);
        po.setSenderMobile(bo.getSenderMobile());
        log.debug("insert: po = {}", po);
        po = warehousePoMapper.save(po);
        return CloneFactory.copy(new Warehouse(), po);
    }

    /**
     * @author 37220222203558
     * 2024-dsg116-修改仓库
     */
    public void save(Warehouse bo, UserDto user) throws RuntimeException {
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        WarehousePo po = CloneFactory.copy(new WarehousePo(), bo);
        po.setSenderMobile(bo.getSenderMobile());
        log.debug("save: po = {}", po);
        warehousePoMapper.save(po);
    }
}
