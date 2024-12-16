package cn.edu.xmu.oomall.ztoexpress.mapper;

import cn.edu.xmu.oomall.ztoexpress.mapper.po.PersoninfoPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersoninfoPoMapper extends JpaRepository<PersoninfoPo,Long>{
}
