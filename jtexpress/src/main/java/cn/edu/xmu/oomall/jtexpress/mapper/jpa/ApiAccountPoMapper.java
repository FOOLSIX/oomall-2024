package cn.edu.xmu.oomall.jtexpress.mapper.jpa;

import cn.edu.xmu.oomall.jtexpress.mapper.po.ApiAccountPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiAccountPoMapper extends JpaRepository<ApiAccountPo, Long> {
    // 根据 account 查询数据
    Optional<ApiAccountPo> findByAccount(Long account);
}
