package cn.edu.xmu.oomall.freight.mapper.jpa;

import cn.edu.xmu.oomall.freight.mapper.po.UndeliverablePo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
public interface UndeliverablePoMapper extends JpaRepository<UndeliverablePo, Long> {

    UndeliverablePo findByRegionIdAndLogisticsId(Long regionId, Long shopLogisticsId);

    List<UndeliverablePo> findAllByLogisticsId(Long shopLogisticsId, Pageable pageable);
}
