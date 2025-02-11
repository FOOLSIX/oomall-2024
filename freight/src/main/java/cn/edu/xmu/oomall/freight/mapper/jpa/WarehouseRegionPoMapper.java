package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.mapper.po.WarehouseRegionPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 37220222203558
 * 2024-dsg116
 */
@Repository
public interface WarehouseRegionPoMapper extends JpaRepository<WarehouseRegionPo, Long> {
    List<WarehouseRegionPo> findAllByWarehouseIdOrderByRegionId(Long warehouseId);

    WarehouseRegionPo save(WarehouseRegionPo po);
}
