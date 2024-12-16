//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.mapper.jpa;

import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityPoMapper extends JpaRepository<ActivityPo, Long> {

    @Query(value = "select DISTINCT a from ActivityPo a join ActivityOnsalePo b on a.id = b.actId where b.onsaleId = :onsaleId")
    List<ActivityPo> findByOnsaleIdEquals(Long onsaleId, Pageable pageable);

    //Pageable 存在bug count出错
    //@Query(value = "select a.id as id, a.shop_id as shop_id, name, act_class, a.creator_id as creator_id, a.creator_name as creator_name, a.modifier_id as creator_name, a.modifier_name as creator_name, a.gmt_create as gmt_create, a.gmt_modified as gmt_modified from goods_activity a inner join goods_activity_onsale b on a.id = b.act_id inner join goods_onsale c on c.id = b.onsale_id  where c.product_id = :productId and a.act_class = :actClass ", nativeQuery = true)
    @Query(value = "select DISTINCT act from ActivityPo act JOIN ActivityOnsalePo actOnsale ON act.id = actOnsale.actId JOIN OnsalePo onsale ON actOnsale.onsaleId = onsale.id WHERE onsale.productId=:productId and act.actClass=:actClass and act.status=:status")
    List<ActivityPo> findByActClassEqualsAndProductIdEqualsAndStatusEquals(String actClass, Long productId, Integer status, Pageable pageable);

    @Query(value = "select DISTINCT a from ActivityPo a join ActivityOnsalePo b on a.id = b.actId join OnsalePo c on c.id = b.onsaleId  where c.productId = :productId and a.actClass = :actClass and c.endTime >= :beginTime and c.endTime <= :endTime and a.status = 1")
    List<ActivityPo> findValidByActClassEqualsAndProductIdEquals(String actClass, Long productId, LocalDateTime beginTime, LocalDateTime endTime, Pageable pageable);

    @Query(value = "select DISTINCT a from ActivityPo a where a.shopId = :shopId and a.actClass = :actClass and a.status = :status")
    List<ActivityPo> findByActClassEqualsAndShopIdEqualsAndStatusEquals(String actClass,Long shopId, Integer status, Pageable pageable);

    @Query(value = "select DISTINCT a from ActivityPo a join ActivityOnsalePo b on a.id = b.actId join OnsalePo c on c.id = b.onsaleId where a.shopId = :shopId and a.actClass = :actClass and c.endTime >= :beginTime and c.endTime <= :endTime and a.status = 1")
    List<ActivityPo> findValidByActClassEqualsAndShopIdEquals(String actClass, Long shopId, LocalDateTime beginTime, LocalDateTime endTime, Pageable pageable);

    @Query(value = "select DISTINCT a from ActivityPo a join ActivityOnsalePo b on a.id = b.actId join OnsalePo c on c.id = b.onsaleId where a.actClass = :actClass and c.endTime >= :beginTime and c.endTime <= :endTime and a.status = 1")
    List<ActivityPo> findValidByActClassEquals(String actClass, LocalDateTime beginTime, LocalDateTime endTime, Pageable pageable);

    @Query(value = "select DISTINCT a from ActivityPo a where a.actClass = :actClass and a.status = :status")
    List<ActivityPo> findByActClassEqualsAndStatusEquals(String actClass, Integer status, Pageable pageable);

    @Query(value = "select DISTINCT a from ActivityPo a where a.shopId = :shopId and a.actClass = :actClass")
    List<ActivityPo> findByActClassEqualsAndShopIdEquals(String actClass, Long shopId, Pageable pageable);

    @Query(value = "select DISTINCT a from ActivityPo a where a.actClass = :actClass")
    List<ActivityPo> findByActClassEquals(String actClass, Pageable pageable);
}
