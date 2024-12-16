package cn.edu.xmu.oomall.shop.mapper;

import cn.edu.xmu.oomall.shop.mapper.po.TemplatePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplatePoMapper extends JpaRepository<TemplatePo, Long>{
    //List<TemplatePo> findByName(String name, Pageable pageable);


    List<TemplatePo> findByShopId(Long shopId, Pageable pageable);

    List<TemplatePo> findByNameAndShopId(String name, Long shopId, Pageable pageable);
}
