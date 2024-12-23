package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.controller.vo.CustomerVo;
import cn.edu.xmu.oomall.customer.controller.vo.SimpleCustomerVo;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    // TODO 顾客
    private JwtHelper jwtHelper;
    private final CustomerService customerService;

    /**
     * 买家获得购物车列表
     *
     */
    @GetMapping("/carts")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getCarts(@LoginUser UserDto userDto,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize){
        return new ReturnObject();
    }





}
