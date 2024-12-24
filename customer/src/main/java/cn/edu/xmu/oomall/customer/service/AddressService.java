package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.customer.controller.vo.AddressVo;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.mapper.jpa.AddressPoMapper;
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
    private final RedisUtil redisUtil;
    private final AddressDao addressDao;

    /**
     * 买家新增地址
     */

    public ReturnObject addAddress(Long id,Integer regionId,String address,String consignee,String mobile){
        Address address1 = new Address();

        address1.setCustomer_id(id);
        address1.setRegion_id(regionId);
        address1.setAddress(address);
        address1.setConsignee(consignee);
        address1.setMobile(mobile);

        addressDao.SaveAddress(address1);
        return new ReturnObject();
    }

    public Object queryAddress(Long customer_id,Integer page, Integer pageSize){
        List<Address> addressList = addressDao.getAlladdress(customer_id,page,pageSize);
//        Integer id;
//        //todo:Region的clonefactory无限爆红，无法构建自定义返回体
//        Integer regionId;
//        String address;
//        String consignee;
//        String mobile;
//        Boolean be_default;
//        if(addressList.size()>0){
//
//        }
        int fromIndex = Math.min((page - 1) * pageSize, addressList.size());
        int toIndex = Math.min(fromIndex + pageSize, addressList.size());

        List<AddressVo> pagedItems = addressList.subList(fromIndex, toIndex).stream()
                .map(AddressVo::new)
                .collect(Collectors.toList());
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("page", page);
        responseMap.put("pageSize", pageSize);
        responseMap.put("list", pagedItems);
        return responseMap;
    }

    public ReturnObject setDefaultAddress(Long id){
        addressDao.setdefault(id);
        return new ReturnObject();
    }

    public ReturnObject changeAddressInfo(Long id,Integer regionId,String address,String consignee,String mobile){
        Address address1 = addressDao.findById(id);
        if(regionId != null){
            address1.setRegion_id(regionId);
        }
        if(address != null){
            address1.setAddress(address);
        }
        if(consignee != null){
            address1.setConsignee(consignee);
        }
        if(mobile != null){
            address1.setMobile(mobile);
        }

        addressDao.SaveAddress(address1);
        return new ReturnObject();
    }

    public ReturnObject deleteAddress(Long id){
        addressDao.DeleteAddress(id);
        return new ReturnObject();
    }
}
