package cn.edu.xmu.oomall.freight.dao;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.jpa.WarehouseRegionPoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.WarehouseRegionPo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 37220222203558
 * 2024-dsg116
 */
@Repository
@Slf4j
@RefreshScope
@RequiredArgsConstructor
public class WarehouseRegionDao {

    private final static String KEY = "W%d";

    private final WarehouseRegionPoMapper warehouseRegionPoMapper;

    private final RegionDao regionDao;

    private final RedisUtil redisUtil;

    @Value("3600")
    private int timeout;

    /**
     * @author 37220222203558
     * 2024-dsg116
     */
    public List<Region> findByWarehouseId(Long warehouseId) throws RuntimeException {
        log.debug("findByWarehouseId: warehouseId = {}", warehouseId);
        String key = String.format(KEY, warehouseId);
        List<Long> regionIds = (List<Long>) redisUtil.get(key);
        if (Objects.isNull(regionIds)) {
            List<WarehouseRegionPo> poList = warehouseRegionPoMapper.findAllByWarehouseIdOrderByRegionId(warehouseId);
            List<Region> regionList = new ArrayList<>();
            List<Long> boRegionIds = poList.stream().map(WarehouseRegionPo::getRegionId).collect(Collectors.toList());
            Optional.of(key).ifPresent(buildKey -> redisUtil.set(buildKey, (Serializable) boRegionIds, timeout));
            for (WarehouseRegionPo po : poList) {
                regionList.add(regionDao.findById(po.getRegionId()));
            }
            return regionList;
        }
        else {
            return regionIds.stream()
                    .map(regionDao::findById)
                    .collect(Collectors.toList());
        }
    }

    /**
     * @author 37220222203558
     * 2024-dsg117-通过仓库id和地区id获取仓库配送地区
     */
    public WarehouseRegion findByWarehouseIdAndRegionId(Long warehouseId, Long regionId) throws RuntimeException {
        log.debug("findByWarehouseIdAndRegionId: warehouseId = {}, regionId = {}", warehouseId, regionId);
        String key = String.format(KEY, warehouseId);
        List<Long> regionIds = (List<Long>) redisUtil.get(key);
        if (!Objects.isNull(regionIds)) {
            if(regionIds.contains(regionId)){
                WarehouseRegion warehouseRegion = new WarehouseRegion();
                warehouseRegion.setRegionId(regionId);
                warehouseRegion.setWarehouseId(warehouseId);
                log.debug("findByWarehouseIdAndRegionId: hit in redis key = {}, warehouseRegion = {}", key, warehouseRegion);
                return warehouseRegion;
            }
        }
        List<WarehouseRegionPo> poList = warehouseRegionPoMapper.findAllByWarehouseIdOrderByRegionId(warehouseId);
        List<Long> boRegionIds = poList.stream().map(WarehouseRegionPo::getRegionId).collect(Collectors.toList());
        Optional.of(key).ifPresent(buildKey -> redisUtil.set(buildKey, (Serializable) boRegionIds, timeout));
        if(boRegionIds.contains(regionId)){
            WarehouseRegion warehouseRegion = CloneFactory.copy(new WarehouseRegion(), poList.get(boRegionIds.indexOf(regionId)));
            log.debug("findByWarehouseIdAndRegionId: retrieve from database warehouseRegion = {}", warehouseRegion);
            return warehouseRegion;
        }
        throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "仓库配送地区", warehouseId, regionId));
    }

    /**
     * @author 37220222203558
     * 2024-dsg117-新增仓库配送地区
     */
    public WarehouseRegion insert(WarehouseRegion bo, UserDto user) throws RuntimeException {
        bo.setId(null);
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());
        WarehouseRegionPo po = CloneFactory.copy(new WarehouseRegionPo(), bo);
        log.debug("insert: po = {}", po);
        po = warehouseRegionPoMapper.save(po);
        return CloneFactory.copy(new WarehouseRegion(), po);
    }

    /**
     * @author 37220222203558
     * 2024-dsg117-检查是否重复设置
     */
    public void checkWarehouseRegion(Long warehouseId, List<Long> regionIdList) throws RuntimeException {
        Collections.sort(regionIdList);
        log.debug("checkWarehouseRegionByWarehouseIdAndRegionId: warehouseId = {}, regionId = {}", warehouseId);
        String key = String.format(KEY, warehouseId);
        List<Long> regionIds = (List<Long>) redisUtil.get(key);
        if (!Objects.isNull(regionIds)) {
            if(hasDuplicates(regionIds, regionIdList)) {
                throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSEREGION_EXIST, String.format(ReturnNo.FREIGHT_WAREHOUSEREGION_EXIST.getMessage(), "仓库配送地区", warehouseId));
            }
        }
        List<WarehouseRegionPo> poList = warehouseRegionPoMapper.findAllByWarehouseIdOrderByRegionId(warehouseId);
        List<Long> boRegionIds = poList.stream().map(WarehouseRegionPo::getRegionId).collect(Collectors.toList());
        Optional.of(key).ifPresent(buildKey -> redisUtil.set(buildKey, (Serializable) boRegionIds, timeout));
        if (hasDuplicates(boRegionIds, regionIdList)) {
            throw new BusinessException(ReturnNo.FREIGHT_WAREHOUSEREGION_EXIST, String.format(ReturnNo.FREIGHT_WAREHOUSEREGION_EXIST.getMessage(), "仓库配送地区", warehouseId));
        }
    }

    /**
     * @author 37220222203558
     * 2024-dsg117-修改仓库配送地区
     */
    public String save(WarehouseRegion bo, UserDto user) throws RuntimeException {
        String key = String.format(KEY, bo.getWarehouseId());
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        WarehouseRegionPo po = CloneFactory.copy(new WarehouseRegionPo(), bo);
        log.debug("save: po = {}", po);
        warehouseRegionPoMapper.save(po);
        return key;
    }

    /**
     * @author 37220222203558
     * 2024-dsg116
     */
    public String delete(Long warehouseId, Long regionId){
        log.debug("delete: warehouseId = {}, regionId = {}", warehouseId, regionId);
        return String.format(KEY, warehouseId);
    }

    private boolean hasDuplicates(List<Long> regionList1, List<Long> regionList2) {
        int i = 0;
        int j = 0;
        while (i < regionList1.size() && j < regionList2.size()) {
            if (regionList1.get(i).equals(regionList2.get(j))) {
                return true;
            }
            else if (regionList1.get(i).compareTo(regionList2.get(j)) > 0) {
                j++;
            }
            else {
                i++;
            }
        }
        return false;
    }
}
