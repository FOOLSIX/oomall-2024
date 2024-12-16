package cn.edu.xmu.oomall.jtexpress.mapper.jpa;

import cn.edu.xmu.oomall.jtexpress.mapper.po.TraceDetailPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface TraceDetailPoMapper extends JpaRepository<TraceDetailPo,Long> {
    // 通过 billcode 查询多个对象
    ArrayList<TraceDetailPo> findByBillCodeAndScanTimeBefore(String billCode, LocalDateTime currentTime);
}
