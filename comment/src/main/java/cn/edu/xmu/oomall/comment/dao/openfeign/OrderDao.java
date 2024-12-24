package cn.edu.xmu.oomall.comment.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.comment.dao.CommentDao;
import cn.edu.xmu.oomall.comment.mapper.openfeign.OrderMapper;
import cn.edu.xmu.oomall.comment.mapper.openfeign.po.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderDao {
    private final OrderMapper orderMapper;
    private final CommentDao commentDao;

    public Order findOrderById(Long id) {
        InternalReturnObject<Order> ret = orderMapper.getOrderById(id);
        ret.getData().setCommentDao(commentDao);
        return ret.getData();
    }
}
