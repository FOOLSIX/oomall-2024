package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.oomall.customer.controller.dto.CartItemDto;
import cn.edu.xmu.oomall.customer.controller.vo.SimpleCartItemVo;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.service.CartService;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
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
                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        Long id = userDto.getId();
        Cart cart = cartService.getCustomertCart(id);
        List<CartItem> cartItemList = cartService.getCartItems(cart,page,pageSize);
        List<SimpleCartItemVo> svo = cartItemList.stream().
                map(items -> CloneFactory.copy(new SimpleCartItemVo(), items)).collect(Collectors.toList());
        return new ReturnObject(new PageVo<>(svo, page, pageSize));
    }

    /**
     * 买家将商品加入购物车
     *
     */
    @PostMapping("/carts")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject postCartItems(  @LoginUser UserDto userDto,
                                        @RequestBody @Validated(NewGroup.class) CartItemDto vo){
        Long id = userDto.getId();
        Cart cart = cartService.getCustomertCart(id);
        CartItem cartItem = new CartItem(cart,userDto);
        CartItem po = CloneFactory.copy(cartItem,vo);
        po.setCreatorId(cart.getId());
        po.setCreatorName(userDto.getName());
        LocalDateTime now = LocalDateTime.now();
        po.setGmtCreate(now);
        po.setGmtModified(now);

        cartService.postCartItem(po);

        return new ReturnObject();
    }

    /**
     * 买家清空购物车
     *
     */
    @DeleteMapping("/carts")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject emptyCart(@LoginUser UserDto userDto){
        Long id = userDto.getId();
        Cart cart = cartService.getCustomertCart(id);
        List<CartItem> cartItemList = cartService.getAllCartItem(cart);
        for (CartItem cartItem : cartItemList) {
            cartService.deleteCartItem(cartItem);
        }
        return new ReturnObject();
    }
    /**
     * 买家修改购物车单个商品的数量或规格
     *
     */
    @PutMapping("/carts/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject changeCartInfo (  @LoginUser UserDto userDto,
                                          @PathVariable Long id,
                                          @RequestBody @Validated(NewGroup.class) CartItemDto vo){
        CartItem cartItem = cartService.getCartItemById(id);
        CartItem po = CloneFactory.copy(cartItem,vo);
        cartService.postCartItem(po);
        return new ReturnObject();
    }

    /**
     * 买家删除购物车中商品
     *
     */
    @DeleteMapping("/carts/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject deleteCartItem ( @PathVariable Long id,
                                         @LoginUser UserDto userDto){
        CartItem cartItem = cartService.getCartItemById(id);
        cartService.deleteCartItem(cartItem);
        return new ReturnObject();
    }
}
