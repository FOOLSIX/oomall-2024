package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.customer.controller.dto.AddressDto;
import cn.edu.xmu.oomall.customer.controller.vo.AddressVo;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.mapper.jpa.AddressPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.edu.xmu.javaee.core.util.CloneFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor

public class AddressService {
    private final AddressDao addressDao;

    public Address findById(Long id) throws Exception {return addressDao.findById(id);}

    /**
     * 买家新增地址
     */

    public void addAddress(Address address){addressDao.SaveAddress(address);}

    //
    public List<Address> getCustomerAddress(Long customer_id,Integer page, Integer pageSize){
        List<Address> addressList = addressDao.getAlladdress(customer_id,page,pageSize);
        return addressList;
    }

    public void setDefaultAddress(Long id){addressDao.setdefault(id);}

    public void deleteAddress(Long id){addressDao.DeleteAddress(id);}

}
