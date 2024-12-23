package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.mapper.jpa.CustomerPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Component
@RefreshScope
@RequiredArgsConstructor
public class CustomerDao {

    public static final String KEY = "S%d";

    private final RedisUtil redisUtil;

    private final CustomerPoMapper customerPoMapper;


    /**
     * 构建bo对象
     * @param po
     * @return
     */
    public Customer build(CustomerPo po) {
        Customer bo = CloneFactory.copy(new Customer(), po);
        bo.setCustomerDao(this);
        return bo;
    }

    /**
     * 按照id获得对象
     *
     * @param id shop id
     * @return Shop
     */
    public Customer findById(Long id) {
        if (id == null) {
            log.info("id 为 null");
            throw new IllegalArgumentException("findById: id is null");
        }
        Optional<CustomerPo> customerPo = customerPoMapper.findById(id);
        return customerPo.map(this::build).orElse(null);
    }

    /**
     * 分页获取用户信息
     * @param page
     * @param pageSize
     * @return
     */
    public List<Customer> getAllUsers(Integer page, Integer pageSize) {
        List<Customer> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id");
        List<CustomerPo> customerPos;
        Page<CustomerPo> pageCustomer = this.customerPoMapper.findAll(pageable);
        customerPos = pageCustomer.toList();
        ret = customerPos.stream().map(po -> CloneFactory.copy(new Customer(), po)).collect(Collectors.toList());
        return ret;
    }


    /**
     * 保存用户状态
     *
     * @param customer
     * @param userDto
     * @throws RuntimeException
     */
    public void save(Customer customer, UserDto userDto) throws RuntimeException {
        if (customer.getId() == null) {
            throw new IllegalArgumentException("save: customer id is null");
        }
        customer.setModifier(userDto);
        customer.setGmtModified(LocalDateTime.now());
        CustomerPo po = CloneFactory.copy(new CustomerPo(), customer);
        log.debug("save: po = {}", po);
        this.customerPoMapper.save(po);
    }
}
