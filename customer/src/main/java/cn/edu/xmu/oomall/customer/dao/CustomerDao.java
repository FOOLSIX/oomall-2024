package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.mapper.jpa.CustomerPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class CustomerDao {

    public static final String KEY = "S%d";

    private final RedisUtil redisUtil;

    private final CustomerPoMapper customerPoMapper;

    @Autowired
    public CustomerDao(RedisUtil redisUtil, CustomerPoMapper customerPoMapper) {
        this.redisUtil = redisUtil;
        this.customerPoMapper = customerPoMapper;
    }

    /**
     * 按照id获得对象
     *
     * @param id shop id
     * @return Shop
     */
    public Customer findById(Long id){
        if (id.equals(null)) {
            throw new IllegalArgumentException("findById: id is null");
        }
        Optional<CustomerPo> customerPo = customerPoMapper.findById(id);
        Customer customer = customerPo.map(po -> CloneFactory.copy(new Customer(), po)).orElse(null);
        return customer;
    }


    /**
     * 保存用户状态
     * @param customer
     * @param userDto
     * @return
     * @throws RuntimeException
     */
    public String save(Customer customer, UserDto userDto) throws RuntimeException{
        if (customer.getId().equals(null)){
            throw new IllegalArgumentException("save: customer id is null");
        }
        String key = String.format(KEY, customer.getId());
        CustomerPo po = CloneFactory.copy(new CustomerPo(),customer);
        this.customerPoMapper.save(po);
        return key;
    }
}
