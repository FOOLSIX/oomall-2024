package cn.edu.xmu.oomall.jtexpress.mapper.jpa;


import cn.edu.xmu.oomall.jtexpress.mapper.po.CustomerPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerPoMapper extends JpaRepository<CustomerPo,Long> {
    //根据code获取用户
    Optional<CustomerPo> findByCode(String code);
}
