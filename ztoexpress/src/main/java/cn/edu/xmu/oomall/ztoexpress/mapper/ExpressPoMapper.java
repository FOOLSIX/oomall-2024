package cn.edu.xmu.oomall.ztoexpress.mapper;

import cn.edu.xmu.oomall.ztoexpress.mapper.po.ExpressPo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpressPoMapper extends JpaRepository<ExpressPo,Long>{
    ExpressPo findByBillcode(String bill_code);
    List<ExpressPo> findAllByZtoOrderId(Long orderId);

    /**
     *
     * Author xsy
     */
    @Modifying
    @Query("UPDATE ExpressPo SET expressStatus = :status WHERE id = :id")
    void updateStatus(@Param("status") int status, @Param("id") Long id);
}
