//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.mapper.jpa;

import cn.edu.xmu.oomall.product.mapper.po.ProductPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPoMapper extends JpaRepository<ProductPo, Long> {

    Page<ProductPo> findByNameEqualsAndStatusNot(String name, Byte status, Pageable pageable);

    Page<ProductPo> findByGoodsIdEquals(Long id, Pageable pageable);

    List<ProductPo> findByTemplateIdEquals(Long templateId,Pageable pageable);

    List<ProductPo> findByShopLogisticIdEquals(Long logisticId, Pageable pageable);

    List<ProductPo> findByCategoryIdEquals(Long id, Pageable pageable);
}
