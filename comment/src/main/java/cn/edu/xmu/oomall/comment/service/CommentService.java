package cn.edu.xmu.oomall.comment.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.StatusVo;
import cn.edu.xmu.oomall.comment.dao.CommentDao;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import cn.edu.xmu.oomall.comment.dao.openfeign.OrderDao;
import cn.edu.xmu.oomall.comment.dao.openfeign.bo.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class CommentService {
    private final CommentDao commentDao;
    private final OrderDao orderDao;

    public List<Comment> retrieveReviewedByProductId(Long productId, Integer page, Integer pageSize) {
        return commentDao.retrieveReviewedByProductId(productId, page, pageSize);
    }

    public List<Comment> retrieveCommentsByStatus(Byte status, Integer page, Integer pageSize) {
        return commentDao.retrieveCommentsByStatus(status, page, pageSize);
    }
    
    public List<Comment> retrieveCommentsByUserIdAndStatus(Long userId, Byte status,Integer page, Integer pageSize) {
        return commentDao.retrieveByUidAndStatus(userId, status, page, pageSize);
    }

    public List<StatusVo> retrieveCommentStates() {
        return Comment.STATUSNAMES.keySet().stream().map(key -> new StatusVo(key, Comment.STATUSNAMES.get(key))).collect(Collectors.toList());
    }

    public void createComment(Comment comment, Long orderId, Long productId, UserDto user) {
        Order order = orderDao.findById(orderId);
        order.createComment(comment, productId, user);
    }

    public Comment findCommentById(Long id) {
        return commentDao.findById(id);
    }

    public void deleteById(Long id) {
        commentDao.findById(id).delete();
    }

    public void updateComment(Comment comment, UserDto user) {
        commentDao.findById(comment.getId());
        commentDao.save(comment, user);
    }

    public void approve(Long id, UserDto user) {
        Comment comment = commentDao.findById(id);
        comment.approve(user);
    }

    public void ban(Long id, UserDto user) {
        Comment comment = commentDao.findById(id);
        comment.ban(user);
    }

    public void requestBlock(Long id, Long shopId, UserDto user) {
        Comment comment = commentDao.findById(id);
        comment.requestBlock(shopId, user);
    }

    public void createReply(Long id, Comment comment, Long shopId, UserDto user) {
        Comment parentComment = commentDao.findById(id);
        parentComment.addReplyComment(comment, shopId, user);
    }

    public void createAdditionalComment(Long id, Comment comment, UserDto user) {
        Comment parentComment = commentDao.findById(id);
        parentComment.addAdditionalComment(comment, user);
    }

}
