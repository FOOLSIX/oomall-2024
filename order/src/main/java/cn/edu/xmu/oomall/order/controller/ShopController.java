package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController /*Restful的Controller对象*/
@RequiredArgsConstructor
@Slf4j
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ShopController {

    private OrderService orderService;

    @Autowired
    public ShopController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 商家-订单相关API

    /**
     * 商家确认订单
     * @param id
     * @param userDto
     * @return
     */
    @PutMapping("/shops/{shopId}/orders/{id}/confirm")
    @Audit(departName = "order")
    public ReturnObject confirmOrder(@PathVariable Long id,
                                     @LoginUser UserDto userDto){
        this.orderService.confirmOrder(id,userDto);
        return new ReturnObject(ReturnNo.OK);
    }
}