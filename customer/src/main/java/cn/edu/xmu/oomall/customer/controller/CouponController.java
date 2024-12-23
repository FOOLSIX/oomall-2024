package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
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
public class CouponController {
    // TODO 顾客-优惠券
    private JwtHelper jwtHelper;
    private final CustomerService customerService;

    /**
     * 用户获取优惠券
     *
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
                                   @RequestParam(required = false) Integer pageSize) {
        return new ReturnObject();
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/coupons/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getCouponById(@PathVariable Long id) {
        return new ReturnObject();
    }
}
