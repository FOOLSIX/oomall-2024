package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
