package cn.edu.xmu.oomall.customer.mapper.jpa;

import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPoMapper extends JpaRepository<CustomerPo, Long> {

}
