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

    public List<ContractPo> findAllByShopIdOrderByPriorityAsc(Long shopId, Pageable pageable);

    public ContractPo findByShopIdAndLogisticsId(Long shopId, Long logisticsId);

}
