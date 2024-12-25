package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.oomall.customer.controller.dto.AddressDto;
import cn.edu.xmu.oomall.customer.controller.vo.SimpleAddressVo;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j


public class AddressController {
    private final AddressService addressService;
    /**
     * 买家新增地址
     *
     */
    @PostMapping("/address")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject addAddress(@LoginUser UserDto userDto,
                                   @RequestBody @Validated(NewGroup.class) AddressDto vo) {

        Address address = CloneFactory.copy(new Address(),vo);
        addressService.addAddress(address);
        SimpleAddressVo svo = new SimpleAddressVo(address);
        return new ReturnObject(svo);
    }

    /**
     * 买家查询所有已有的地址信息
     *
     */
    @GetMapping("/address")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getAddress(@LoginUser UserDto userDto,
                                   @RequestParam (required = false, defaultValue = "1") Integer page,
                                   @RequestParam (required = false, defaultValue = "10") Integer pageSize) {
        Long id = userDto.getId();
        List<Address> addressList = addressService.getCustomerAddress(id,page,pageSize);
        List<SimpleAddressVo> svo =  addressList.stream().
                map(address -> CloneFactory.copy(new SimpleAddressVo(), address)).collect(Collectors.toList());
        return new ReturnObject(new PageVo<>(svo, page, pageSize));
    }

    /**
     * 买家设置默认地址
     *
     */
    @PutMapping("/addresses/{id}/default")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject setDefaultAddress(@LoginUser UserDto userDto,
                                          @PathVariable Integer id){
        Long Id = id.longValue();
        addressService.setDefaultAddress(Id);

        return new ReturnObject();
    }

    /**
     * 买家修改自己的地址信息
     *
     */
    @PutMapping("/addresses/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject changeAddressInfo(@LoginUser UserDto userDto,
                                          @PathVariable Integer id,
                                          @RequestBody @Validated(NewGroup.class) AddressDto vo) throws Exception {
        Long Id = id.longValue();
        try {
            Address address = addressService.findById(Id);
            if(!Objects.equals(vo.getRegion_id(), address.getRegion_id())){address.setRegion_id(vo.getRegion_id());}
            if(vo.getAddress()!=null){address.setAddress(vo.getAddress());}
            if(vo.getConsignee()!=null){address.setConsignee(vo.getConsignee());}
            if(vo.getMobile()!=null){address.setMobile(vo.getMobile());}

            addressService.addAddress(address);
            SimpleAddressVo svo = new SimpleAddressVo(address);
            return new ReturnObject(svo);
        } catch (Exception e) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST,e.getMessage());
        }
    }

    /**
     * 买家删除地址
     * @param id
     */
    @DeleteMapping("/address/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject deleteAddress(@LoginUser UserDto userDto,
                                      @PathVariable Integer id) {
        Long Id = id.longValue();
        addressService.deleteAddress(Id);
        return new ReturnObject();
    }

}
