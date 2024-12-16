package cn.edu.xmu.oomall.sfexpress.mapper;

import cn.edu.xmu.oomall.sfexpress.mapper.po.SfexpressExpressPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
public interface SfexpressExpressPoMapper extends JpaRepository<SfexpressExpressPo, Long> {
    Optional<SfexpressExpressPo> findByOrderId(String orderId);

    Optional<SfexpressExpressPo> findByWaybillNo(String waybillNo);

    @Modifying
    @Query("UPDATE SfexpressExpressPo e SET " +
            "e.totalHeight = :#{#express.totalHeight}, " +
            "e.totalLength = :#{#express.totalLength}, " +
            "e.totalVolume = :#{#express.totalVolume}, " +
            "e.totalWeight = :#{#express.totalWeight}, " +
            "e.totalWidth = :#{#express.totalWidth}, " +
            "e.sendStartTm = :#{#express.sendStartTm}, " +
            "e.pickupAppointEndtime = :#{#express.pickupAppointEndtime}, " +
            "e.status = :#{#express.status} " +
            "WHERE e.orderId = :#{#express.orderId}")
    int updateExpressDetails(@Param("express") SfexpressExpressPo expressPo);
}
