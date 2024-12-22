package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/coupons/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getCouponById(@PathVariable Long id){
        return new ReturnObject();
    }





}
