package cn.edu.xmu.oomall.region.mapper;

import cn.edu.xmu.oomall.region.mapper.po.RegionPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RegionPoMapper extends JpaRepository<RegionPo, Long> {
    List<RegionPo> findByPidEqualsAndStatusEquals(Long pid, Byte status, Pageable pageable);

    List<RegionPo> findByPid(Long pid, Pageable pageable);
}
