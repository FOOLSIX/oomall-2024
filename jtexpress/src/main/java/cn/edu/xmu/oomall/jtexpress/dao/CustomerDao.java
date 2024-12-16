package cn.edu.xmu.oomall.jtexpress.dao;

import cn.edu.xmu.oomall.jtexpress.mapper.jpa.CustomerPoMapper;
import cn.edu.xmu.oomall.jtexpress.mapper.po.CustomerPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RefreshScope
public class CustomerDao {
    @Autowired
    private CustomerPoMapper customerPoMapper;

    public Boolean checkByCode(String customerCode) {
        Optional<CustomerPo> customerPo=customerPoMapper.findByCode(customerCode);
        return customerPo.isPresent();
    }

}
