package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.order.controller.vo.SimpleOrderVo;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController {
    private final OrderService orderService;

    @GetMapping("orders/{id}/status")
    public ReturnObject getOrderStatus(@PathVariable Long id) {
        SimpleOrderVo orderVo = new SimpleOrderVo(orderService.findOrderById(id));
        return new ReturnObject(ReturnNo.OK, orderVo);
    }
}
