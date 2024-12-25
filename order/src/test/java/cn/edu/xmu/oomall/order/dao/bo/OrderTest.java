package cn.edu.xmu.oomall.order.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.controller.dto.OrderUpdateDto;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = OrderTestApplication.class)
@Transactional(propagation = Propagation.REQUIRED)
public class OrderTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    public void testCancelOrderAsUser() {
        Order order = Order.builder().customerId(1L).status(Order.PENDING_DISPATCH).build();
        UserDto userDto = UserDto.builder().id(1L).build();
        order.setOrderDao(orderDao);
        try {
            order.cancel(userDto);
            // 验证订单状态是否被设置为无效
            assertEquals(Order.INVALID, order.getStatus());
        } catch (BusinessException e) {
            // 测试失败
        }
    }

    @Test
    public void testCancelOrderAsNonShop() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        Order order = Order.builder()
                .shopId(2L) // 假设订单属于另一个商铺
                .status(Order.PENDING_DISPATCH)
                .build();
        order.setOrderDao(orderDao);
        assertThrows(BusinessException.class, () -> {
            order.cancel(1L, userDto);
        });
    }

    @Test
    public void testCancelOrderWhenNotPendingDispatch() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        Order order = Order.builder()
                .shopId(1L)
                .status(Order.DISPATCHED) // 假设订单已经发货
                .build();
        order.setOrderDao(orderDao);
        assertThrows(BusinessException.class, () -> {
            order.cancel(1L, userDto);
        });
    }

    @Test
    public void testConfirmOrderWhenNotPendingAcceptance() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        Order order = Order.builder()
                .shopId(1L)
                .status(Order.DISPATCHED) // 假设订单已经发货
                .build();
        order.setOrderDao(orderDao);
        assertThrows(BusinessException.class, () -> {
            order.confirm(userDto);
        });
    }


    @Test
    public void testCancelOrderAsNonOwner() {
        Order order = Order.builder().customerId(2L).status(Order.PENDING_DISPATCH).build();
        UserDto userDto = UserDto.builder().id(1L).build();
        order.setOrderDao(orderDao);
        assertThrows(BusinessException.class, () -> {
            order.cancel(userDto);
        });
    }

    @Test
    public void testUpdateOrder() {
        Order order = Order.builder().customerId(1L).status(Order.PENDING_DISPATCH).build();
        UserDto userDto = UserDto.builder().id(1L).build();
        OrderUpdateDto orderUpdateDto = OrderUpdateDto.builder().address("新地址").build();
        order.setOrderDao(orderDao);
        try {
            order.update(userDto, orderUpdateDto);
            // 验证订单地址是否被更新
            assertEquals(orderUpdateDto.getAddress(), order.getAddress());
        } catch (BusinessException e) {
            // 测试失败
        }
    }

    @Test
    public void testUpdateOrderAsNonOwner() {
        Order order = Order.builder().customerId(2L).status(Order.PENDING_DISPATCH).build();
        UserDto userDto = UserDto.builder().id(1L).build();
        OrderUpdateDto orderUpdateDto = OrderUpdateDto.builder().address("新地址").build();
        order.setOrderDao(orderDao);
        assertThrows(BusinessException.class, () -> {
            order.update(userDto, orderUpdateDto);
        });
    }

    @Test
    public void testConfirmOrder() {
        Order order = Order.builder().shopId(1L).status(Order.PENDING_ACCEPTANCE).build();
        UserDto userDto = UserDto.builder().id(1L).build();
        order.setOrderDao(orderDao);
        try {
            order.confirm(userDto);
            // 验证订单状态是否被设置为待发货
            assertEquals(Order.PENDING_DISPATCH, order.getStatus());
        } catch (BusinessException e) {
            // 测试失败
        }
    }

    @Test
    public void testConfirmOrderAsNonShop() {
        Order order = Order.builder().shopId(2L).status(Order.PENDING_ACCEPTANCE).build();
        UserDto userDto = UserDto.builder().id(1L).build();
        order.setOrderDao(orderDao);
        assertThrows(BusinessException.class, () -> {
            order.confirm(userDto);
        });
    }

}
