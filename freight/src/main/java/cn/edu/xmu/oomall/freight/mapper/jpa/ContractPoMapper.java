package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.mapper.po.ContractPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 */
@Repository
public interface ContractPoMapper extends JpaRepository<ContractPo, Long> {
    public ContractPo findByIdAndInvalidEquals(Long id, Byte invalid);

    public List<ContractPo> findAllByWarehouseId(Long warehouseId);

    /**
     * 2024-dsg-115
     * 按优先级升序获得与仓库id，shopid对应的所有物流合同
     * @author liboyang
     */
    public List<ContractPo> findAllByShopIdAndWarehouseIdOrderByPriorityAsc(Long shopId, Long warehouseId, Pageable pageable);
}
