package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.mapper.po.WarehousePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 37220222203558
 * 2024-dsg116
 */
@Repository
public interface WarehousePoMapper extends JpaRepository<WarehousePo, Long> {
    List<WarehousePo> findAllByShopId(Long shopId);
}
