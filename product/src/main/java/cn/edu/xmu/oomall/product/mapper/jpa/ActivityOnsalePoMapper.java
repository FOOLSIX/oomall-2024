//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.mapper.jpa;

import cn.edu.xmu.oomall.product.mapper.po.ActivityOnsalePo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ActivityOnsalePoMapper extends JpaRepository<ActivityOnsalePo, Long> {

    List<ActivityOnsalePo> findByActIdEquals(Long actId, Pageable pageable);

    void deleteByActIdEquals(Long actId);

    void deleteByActIdEqualsAndOnsaleIdEquals(Long actId, Long onsaleId);

}
