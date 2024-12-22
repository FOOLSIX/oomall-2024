package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class CustomerService {

    private CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    /**
     * 获取指定id的顾客信息
     * @param id
     * @return
     */
    public Customer getUserById(Long id){
        Customer customer = this.customerDao.findById(id);
        return customer;
    }

    public List<Customer> getAllUsers(Integer page,Integer pageSize){
        List<Customer> customers = this.customerDao.getAllUsers(page, pageSize);
        return customers;
    }

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

    /**
     * 删除顾客
     * @param id
     * @param userDto
     */
    public void delUserById(Long id, UserDto userDto){
        Customer customer = this.customerDao.findById(id);
        customer.delUserById(userDto);
    }


}
