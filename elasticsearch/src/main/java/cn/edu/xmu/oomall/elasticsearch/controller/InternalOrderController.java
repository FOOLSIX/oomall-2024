package cn.edu.xmu.oomall.elasticsearch.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.elasticsearch.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/internal/orders", produces = "application/json;charset=UTF-8")
public class InternalOrderController {

    private final OrderService orderService;

    @GetMapping
    public ReturnObject searchOrders(@RequestParam String itemName,
                                   @RequestParam Long customerId,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) throws IOException {
        List<Long> orderIds = orderService.searchOrderIds(itemName, customerId, page, size);
        return new ReturnObject(ReturnNo.OK, orderIds);
    }
}
