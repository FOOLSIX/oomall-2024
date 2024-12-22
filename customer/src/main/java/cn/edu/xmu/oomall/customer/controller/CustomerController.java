package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private JwtHelper jwtHelper;
    private final CustomerService customerService;


    /**
     * 用户获取优惠券
     * @param userDto
     * @param used
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/coupons")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getCoupons(@LoginUser UserDto userDto,
                                   @RequestParam(required = false) Integer used,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer pageSize){
        return new ReturnObject();
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/coupons/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getCouponById(@PathVariable Long id){
        return new ReturnObject();
    }

    /**
     * 封禁顾客
     *
     * @param id
     * @param did
     * @param userDto
     * @return
     */
    @PutMapping("/shops/{did}/customers/{id}/ban")
    @Audit(departName = "customers")
    public ReturnObject banUser(@PathVariable Long id,
                                @PathVariable Long did,
                                @LoginUser UserDto userDto){
        if (!did.equals(0)){
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        this.customerService.banUser(id,userDto);
        return new ReturnObject();
    }

    /**
     * 解禁顾客
     *
     * @param id
     * @param did
     * @param userDto
     * @return
     */
    @PutMapping("/shops/{did}/customers/{id}/release")
    @Audit(departName = "customers")
    public ReturnObject releaseUser(@PathVariable Long id,
                                @PathVariable Long did,
                                @LoginUser UserDto userDto){
        if (!did.equals(0)){
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        this.customerService.releaseUser(id,userDto);
        return new ReturnObject();
    }



}
