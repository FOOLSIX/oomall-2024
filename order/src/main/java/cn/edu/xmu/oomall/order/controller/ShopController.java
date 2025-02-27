package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController /*Restful的Controller对象*/
@Slf4j
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ShopController {

    private final OrderService orderService;

    @Autowired
    public ShopController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 商铺-订单相关API

    /**
     * 商家确认订单
     *
     * @param id
     * @param userDto
     * @return
     */
    @PutMapping("/shops/{shopId}/orders/{id}/confirm")
    @Audit(departName = "shops")
    public ReturnObject confirmOrder(@PathVariable("id") Long id,
                                     @LoginUser UserDto userDto) {
        if (id == -1 || !orderService.existsOrderById(id)) {
            throw new BusinessException(
                    ReturnNo.FIELD_NOTVALID,
                    String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "订单id")
            );
        }
        this.orderService.confirmOrder(id, userDto);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 商家取消订单
     *
     * @param id
     * @param userDto
     * @return
     */
    @DeleteMapping("/shops/{shopId}/orders/{id}")
    @Audit(departName = "shops")
    public ReturnObject cancelOrder(@PathVariable("id") Long id,
                                    @PathVariable("shopId") Long shopId,
                                    @LoginUser UserDto userDto) {
        if (id == -1 || !orderService.existsOrderById(id)) {
            throw new BusinessException(
                    ReturnNo.FIELD_NOTVALID,
                    String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "订单id")
            );
        }
        this.orderService.cancelOrder(id, shopId, userDto);
        return new ReturnObject();
    }
}
