package cn.edu.xmu.oomall.sfexpress.mapper;

import cn.edu.xmu.oomall.sfexpress.mapper.po.SfexpressCargoDetailPo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
public interface SfexpressCargoDetailPoMapper extends JpaRepository<SfexpressCargoDetailPo, Long> {
    Optional<List<SfexpressCargoDetailPo>> findByExpressId(Long expressId);
}
