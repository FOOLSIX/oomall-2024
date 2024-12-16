package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.mapper.po.WarehousePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
public interface WarehousePoMapper extends JpaRepository<WarehousePo, Long> {
    // public WarehousePo findByIdAndInvalidEquals(Long id, Byte invalid);

}
