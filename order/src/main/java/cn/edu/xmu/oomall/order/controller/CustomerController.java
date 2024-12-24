//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.controller.dto.OrderUpdateDto;
import cn.edu.xmu.oomall.order.controller.vo.OrderVo;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.mapper.jpa.SearchMapper;
import cn.edu.xmu.oomall.order.service.OrderService;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import cn.edu.xmu.oomall.order.service.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CustomerController {

    private final OrderService orderService;

    private final SearchMapper searchMapper;

    @Autowired
    public CustomerController(OrderService orderService, SearchMapper searchMapper) {
        this.orderService = orderService;
        this.searchMapper = searchMapper;
    }


//    @PostMapping("/orders")
//    public ReturnObject createOrder(@RequestBody @Validated OrderVo orderVo, @LoginUser UserDto user) {
//        orderService.createOrder(orderVo.getItems().stream().map(item -> OrderItemDto.builder().onsaleId(item.getOnsaleId()).quantity(item.getQuantity()).actId(item.getActId()).couponId(item.getCouponId()).build()).collect(Collectors.toList()),
//                ConsigneeDto.builder().consignee(orderVo.getConsignee()).address(orderVo.getAddress()).regionId(orderVo.getRegionId()).mobile(orderVo.getMobile()).build(),
//                orderVo.getMessage(), user);
//        return new ReturnObject(ReturnNo.CREATED);
//    }

//    @GetMapping("/orders")
//    public ReturnObject testFeignSearch(
//            @RequestParam(value = "itemName") String itemName,
//            @RequestParam(value = "customerId") Long customerId,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        try {
//            // 使用 Feign Client 调用
//            InternalReturnObject<List<Long>> response = searchMapper.searchOrders(itemName, customerId, page, size);
//
//            if (response.getErrno() == ReturnNo.OK.getErrNo()) {
//                return new ReturnObject(ReturnNo.OK, response.getData());
//            } else {
//                throw new BusinessException(ReturnNo.getReturnNoByCode(response.getErrno()), response.getErrmsg());
//            }
//        } catch (Exception e) {
//            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, "Feign 调用失败: " + e.getMessage());
//        }
//    }

    /**
     * 买家更改自己名下订单
     *
     * @param userDto
     * @param id
     * @return
     */
    @PutMapping("/orders/{id}")
    public ReturnObject updateOrder(@LoginUser UserDto userDto,
                                    @PathVariable("id") Long id,
                                    @RequestBody OrderUpdateDto orderUpdateDto) {
        // 修改的参数为空
        if (orderUpdateDto.getAddress() == null) {
            throw new BusinessException(
                    ReturnNo.FIELD_NOTVALID,
                    String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "地址")
            );
        }

        // 检查订单 ID 是否有效，假设 -1 是无效的订单 ID
        if (id == -1 || !orderService.existsOrderById(id)) {
            throw new BusinessException(
                    ReturnNo.FIELD_NOTVALID,
                    String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "订单id")
            );
        }
        this.orderService.updateOrderById(userDto, id, orderUpdateDto);
        return new ReturnObject(ReturnNo.OK);
    }


    /**
     * 买家取消本人名下订单
     *
     * @param userDto
     * @param id
     * @return
     */
    @DeleteMapping("/orders/{id}")
    public ReturnObject cancelOrderById(@LoginUser UserDto userDto,
                                        @PathVariable("id") Long id) {
        if (id == -1 || !orderService.existsOrderById(id)) {
            throw new BusinessException(
                    ReturnNo.FIELD_NOTVALID,
                    String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "订单id")
            );
        }
        this.orderService.cancelOrderById(userDto, id);
        return new ReturnObject(ReturnNo.OK);
    }


}
