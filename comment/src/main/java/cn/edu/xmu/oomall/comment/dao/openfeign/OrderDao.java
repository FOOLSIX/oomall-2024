package cn.edu.xmu.oomall.comment.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.comment.dao.CommentDao;
import cn.edu.xmu.oomall.comment.dao.openfeign.bo.Order;
import cn.edu.xmu.oomall.comment.mapper.openfeign.OrderMapper;
import cn.edu.xmu.oomall.comment.mapper.openfeign.po.OrderPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderDao {
    private final OrderMapper orderMapper;
    private final CommentDao commentDao;

    public Order findById(Long id) {
        InternalReturnObject<OrderPo> ret = orderMapper.getOrderById(id);
        Order order = CloneFactory.copy(new Order(), ret.getData());
        order.setCommentDao(commentDao);
        return order;
    }
}
