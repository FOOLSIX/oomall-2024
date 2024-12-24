package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.service.CartService;
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
        Object cartItemsResponse = cartService.getCartItems(userDto.getId(),page,pageSize);
        return new ReturnObject(cartItemsResponse);
    }

    /**
     * 买家将商品加入购物车
     *
     */
    @PostMapping("/carts")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject postCartItems(  @LoginUser UserDto userDto,
                                        @RequestBody Map<Long, Long> requestBody){
        Object cartItemsResponse = cartService.postCartItem(userDto.getId(),requestBody);
        return new ReturnObject(cartItemsResponse);
    }

    /**
     * 买家清空购物车
     *
     */
    @DeleteMapping("/carts")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject emptyCart(@LoginUser UserDto userDto){
        Long id = userDto.getId();
        return new ReturnObject(cartService.emptyCart(id));
    }
    /**
     * 买家修改购物车单个商品的数量或规格
     *
     */
    @PutMapping("/carts/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject changeCartInfo (  @LoginUser UserDto userDto,
                                          @PathVariable Long id,
                                          @RequestBody Map<Long, Long> requestBody){
        Object cartItemsResponse = cartService.changeCartInfo(id,requestBody);
        return new ReturnObject(cartItemsResponse);
    }

    /**
     * 买家删除购物车中商品
     *
     */
    //todo

}
