package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.controller.vo.CustomerVo;
import cn.edu.xmu.oomall.customer.controller.vo.SimpleCustomerVo;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.service.CartService;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;

    /**
     * 买家获得购物车列表
     *
     */
    @GetMapping("/carts")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getCartItems(@LoginUser UserDto userDto,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize){
        try {
            Object cartItemsResponse = cartService.getCartItems(userDto, page, pageSize);

            return new ReturnObject(cartItemsResponse);
        } catch (RuntimeException e) {
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        } catch (Exception e) {
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, "Unexpected error occurred");
        }
    }

    /**
     * 买家将商品加入购物车
     *
     */
//    @PostMapping("/carts")
//    @Transactional(propagation = Propagation.REQUIRED)
//    public ReturnObject postCartItems(  @LoginUser UserDto userDto,
//                                        @RequestBody Map<String, Object> requestBody){
//        try {
//            object cartItemsResponse = cartService.
//        }
//    }
}
