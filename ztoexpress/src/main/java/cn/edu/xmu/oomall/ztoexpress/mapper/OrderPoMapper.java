package cn.edu.xmu.oomall.ztoexpress.mapper;

import cn.edu.xmu.oomall.ztoexpress.mapper.po.OrderPo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPoMapper extends JpaRepository<OrderPo,Long>{
    @Query(value = "select * from zto_order where partner_order_code = ?1",nativeQuery = true)
    List<OrderPo> findAllByPartnerOrderCode(String partnerOrderCode);
    OrderPo findByOrderCode(String orderCode);
    OrderPo findByIdIs(Long id);
}
