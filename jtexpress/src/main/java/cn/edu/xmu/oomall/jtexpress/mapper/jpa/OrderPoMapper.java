package cn.edu.xmu.oomall.jtexpress.mapper.jpa;

import cn.edu.xmu.oomall.jtexpress.mapper.po.OrderPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrderPoMapper extends JpaRepository<OrderPo, Long> {
    Optional<OrderPo> findByTxLogisticId(String txLogisticId);
}

