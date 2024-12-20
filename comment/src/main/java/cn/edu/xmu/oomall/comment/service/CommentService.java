package cn.edu.xmu.oomall.comment.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.dao.CommentDao;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class CommentService {
    private final static Logger logger = LoggerFactory.getLogger(CommentService.class);
    private final CommentDao commentDao;

    public List<Comment> retrieveReviewedByProductId(Long productId, Integer page, Integer pageSize) {
        return commentDao.retrieveReviewedByProductId(productId, page, pageSize);
    }

    public List<Comment> retrieveCommentsByStatus(Byte status, Integer page, Integer pageSize) {
        return commentDao.retrieveCommentsByStatus(status, page, pageSize);
    }

    public void deleteById(Long id) {

    }

    public void approve(Long id, UserDto userDto) {
        Comment comment = commentDao.findById(id);
        comment.approve(userDto);
    }

    public void ban(Long id, UserDto userDto) {
        Comment comment = commentDao.findById(id);
        comment.ban(userDto);
    }

    public void requestBlock(Long id, UserDto userDto) {
        Comment comment = commentDao.findById(id);
        comment.requestBlock(userDto);
    }

}
