package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
                            @RequestBody Map<String, Object> requestBody) {
        try {
            // todo:处理userDto是否存在

            Integer regionId = (Integer) requestBody.get("regionId");
            String address = (String) requestBody.get("address");
            String consignee = (String) requestBody.get("consignee");
            String mobile = (String) requestBody.get("mobile");

            if (regionId == null || address == null || consignee == null || mobile == null) {

            }
            ReturnObject addressResponse = addressService.addAddress(userDto.getId(), regionId, address, consignee, mobile);
            return addressResponse;

        } catch (ClassCastException e) {
            return new ReturnObject();
        }
    }

    /**
     * 买家查询所有已有的地址信息
     *
     */
    @GetMapping("/address")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getAddress(@LoginUser UserDto userDto,
                                   @RequestParam Integer page,
                                   @RequestParam Integer pageSize) {
        Object addressResponse = addressService.queryAddress(userDto.getId(), page, pageSize);
        ReturnObject returnObject = new ReturnObject(addressResponse);
        return returnObject;
    }

    /**
     * 买家设置默认地址
     *
     */
    @PutMapping("/addresses/{id}/default")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject setDefaultAddress(@LoginUser UserDto userDto,
                                          @PathVariable Integer id){

        ReturnObject addressResponse = addressService.setDefaultAddress(id.longValue());
        return addressResponse;
    }

    /**
     * 买家修改自己的地址信息
     *
     */
    @PutMapping("/addresses/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject changeAddressInfo(@LoginUser UserDto userDto,
                                          @PathVariable Integer id,
                                          @RequestBody Map<String, Object> requestBody) {
        Long Id = id.longValue();
        Integer regionId = (Integer) requestBody.get("regionId");
        String address = (String) requestBody.get("address");
        String consignee = (String) requestBody.get("consignee");
        String mobile = (String) requestBody.get("mobile");
        ReturnObject addressResponse = addressService.changeAddressInfo(Id, regionId, address, consignee, mobile);
        return addressResponse;
    }

    /**
     * 买家删除地址
     *
     */
    @DeleteMapping("/address/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject deleteAddress(@LoginUser UserDto userDto,
                                      @PathVariable Integer id) {
        Long Id = id.longValue();
        ReturnObject addressResponse = addressService.deleteAddress(Id);
        return addressResponse;
    }


}
