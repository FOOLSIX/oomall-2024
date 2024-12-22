package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class CustomerService {

    private CustomerDao customerDao;

    /**
     * 封禁顾客
     * @param id
     * @param userDto
     */
    public void banUser(Long id, UserDto userDto){
        Customer customer = this.customerDao.findById(id);
        customer.banUser(userDto);
    }

    /**
     * 解禁顾客
     * @param id
     * @param userDto
     */
    public void releaseUser(Long id, UserDto userDto){
        Customer customer = this.customerDao.findById(id);
        customer.releaseUser(userDto);
    }
}
