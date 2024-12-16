package cn.edu.xmu.oomall.product.mapper.jpa;

import cn.edu.xmu.oomall.product.mapper.po.ProductDraftPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wuzhicheng
 * @create 2022-12-03 23:45
 */
@Repository
public interface ProductDraftPoMapper extends JpaRepository<ProductDraftPo, Long> {
    List<ProductDraftPo> findByShopIdEquals(Long shopId, Pageable pageable);
}
