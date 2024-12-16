//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.shop.mapper;

import cn.edu.xmu.oomall.shop.mapper.po.ShopPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShopPoMapper extends JpaRepository<ShopPo, Long> {
        List<ShopPo> findByCreatorId(Long creatorId, Pageable pageable);
        List<ShopPo> findByNameContainingAndStatusIn(String name, List<Byte>validStatus, Pageable pageable);
}
