package cn.edu.xmu.oomall.ztoexpress.mapper;

import cn.edu.xmu.oomall.ztoexpress.mapper.po.SitePo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitePoMapper extends JpaRepository<SitePo,Long> {
    SitePo findByIdIs(Long id);
}
