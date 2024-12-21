package cn.edu.xmu.oomall.product.mapper.jpa;

import cn.edu.xmu.oomall.product.mapper.po.TemplatePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalTemplateMapper extends JpaRepository<TemplatePo,Long> {
}
