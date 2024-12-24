package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = OrderTestApplication.class)
@Transactional
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    public void testFindById() {
        Order order = orderDao.findById(1L);
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getOrderSn()).isNotEmpty();
    }

    @Test
    public void testSave() {
        // 假设数据库中已经有 ID 为 1 的订单
        Order order = orderDao.findById(1L);
        assertThat(order).isNotNull();

        log.info("orderSn = {}",order.getOrderSn());
        // 修改订单
        order.setOrderSn("UpdatedOrderSn");
        UserDto userDto = new UserDto(1L, "admin", 0L, 0); // 模拟用户修改操作
        orderDao.save(order, userDto);

        // 重新查询并验证
        Order updatedOrder = orderDao.findById(1L);
        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getOrderSn()).isEqualTo("UpdatedOrderSn");
    }
}
