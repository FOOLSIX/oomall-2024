package cn.edu.xmu.oomall.jtexpress.mapper.jpa;

import cn.edu.xmu.oomall.jtexpress.mapper.po.PersonInfoPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonInfoPoMapper extends JpaRepository<PersonInfoPo, Long> {

}
