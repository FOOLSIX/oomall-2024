package cn.edu.xmu.oomall.sfexpress.mapper;

import cn.edu.xmu.oomall.sfexpress.mapper.po.SfexpressRoutePo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
public interface SfexpressRoutePoMapper extends JpaRepository<SfexpressRoutePo, Long> {
    Optional<List<SfexpressRoutePo>> findByMailNo(String mailNo);
}
