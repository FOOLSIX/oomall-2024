//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.openfeign.ExpressDao;
import cn.edu.xmu.oomall.order.mapper.jpa.OrderItemPoMapper;
import cn.edu.xmu.oomall.order.mapper.jpa.OrderPoMapper;
import cn.edu.xmu.oomall.order.mapper.po.OrderPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class OrderDao {

    private OrderPoMapper orderPoMapper;

    private ExpressDao expressDao;

    private OrderItemPoMapper orderItemPoMapper;

    @Autowired
    public OrderDao(OrderPoMapper orderPoMapper, OrderItemPoMapper orderItemPoMapper, ExpressDao expressDao) {
        this.orderPoMapper = orderPoMapper;
        this.orderItemPoMapper = orderItemPoMapper;
        this.expressDao = expressDao;
    }

    private Order build(OrderPo orderPo){
        Order order = CloneFactory.copy(new Order(),orderPo);
        order.setExpressDao(this.expressDao);
        order.setOrderDao(this);
        return order;
    }


//    public void createOrder(Order order){
//        OrderPo orderPo = OrderPo.builder().creatorId(order.getCreatorId()).creatorName(order.getCreatorName()).orderSn(order.getOrderSn()).build();
//        orderPoMapper.save(orderPo);
//
//        order.getOrderItems().stream().forEach(orderItem -> {
//            //TODO: 先要减去货品数量
//
//            OrderItemPo orderItemPo = OrderItemPo.builder().creatorId(orderItem.getCreatorId()).onsaleId(orderItem.getOnsaleId()).quantity(orderItem.getQuantity()).build();
//            orderItemPoMapper.save(orderItemPo);
//        });
//    }

    public List<OrderPo> retrieveOrderList(UserDto user) {
        return orderPoMapper.findByCustomerId(user.getId());
    }

    public Order findById(Long id){
        if (id == -1) {
            log.error("id 为 null");
            throw new IllegalArgumentException("findById: id is null");
        }
        Optional<OrderPo> orderPo = orderPoMapper.findById(id);
        return orderPo.map(this::build).orElse(null);
    }

    public void save(Order order, UserDto user){
        if (order.getId().equals(null)){
            throw new IllegalArgumentException("save: order id is null");
        }
        order.setModifier(user);
        order.setGmtModified(LocalDateTime.now());
        OrderPo orderPo = CloneFactory.copy(new OrderPo(),order);
        this.orderPoMapper.save(orderPo);
    }
}
