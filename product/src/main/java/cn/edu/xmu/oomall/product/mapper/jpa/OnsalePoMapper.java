//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.mapper.jpa;

import cn.edu.xmu.oomall.product.mapper.po.OnsalePo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OnsalePoMapper extends JpaRepository<OnsalePo, Long> {
    List<OnsalePo> findByProductIdEqualsAndEndTimeAfter(Long productId, LocalDateTime time, Pageable pageable);

    List<OnsalePo> findByProductIdIs(Long productId,Pageable pageable);

    @Query(value = "select o from OnsalePo o where o.productId = :productId and ((o.beginTime >= :beginTime and o.beginTime < :endTime) or (o.endTime > :beginTime and o.endTime <= :endTime) or (o.beginTime <= :beginTime and o.endTime >= :endTime))")
    List<OnsalePo> findOverlap(Long productId, LocalDateTime beginTime, LocalDateTime endTime, Pageable pageable);

    @Query(value = "select onsale from  OnsalePo  onsale JOIN ActivityOnsalePo activityOnsale on onsale.id = activityOnsale.onsaleId where activityOnsale.actId = :activityId")
    List<OnsalePo> findByActIdEquals(Long activityId, Pageable pageable);

    @Query(value = "select onsale from OnsalePo onsale JOIN ActivityOnsalePo activityOnsale on onsale.id = activityOnsale.onsaleId where activityOnsale.actId = :activityId and onsale.productId = :pId")
    OnsalePo findByActIdEqualsAndProductIdEquals(Long activityId, Long pId);
}
