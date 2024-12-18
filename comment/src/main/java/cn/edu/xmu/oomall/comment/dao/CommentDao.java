package cn.edu.xmu.oomall.comment.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import cn.edu.xmu.oomall.comment.mapper.CommentMapper;
import cn.edu.xmu.oomall.comment.mapper.po.CommentPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RefreshScope
@RequiredArgsConstructor
@Slf4j
public class CommentDao {
    private final CommentMapper commentMapper;

    /**
     * 从po构建bo
     * @param po
     * @return
     */

    public Comment build(CommentPo po) {
        Comment bo = new Comment();
        CloneFactory.copy(bo, po);
        bo.setCommentDao(this);
        return bo;
    }

    /**
     * 增加评论
     * @param comment
     * @param user
     * @throws RuntimeException
     */
    public void insert(Comment comment, UserDto user) throws RuntimeException {
        comment.setCreator(user);
        comment.setGmtCreate(LocalDateTime.now());

        CommentPo po = CloneFactory.copy(new CommentPo(), comment);
        log.debug("insert: po = {}", po);
        commentMapper.save(po);
    }

    /**
     * 修改评论
     * @param comment
     * @param userDto
     * @throws RuntimeException
     */
    public void save(Comment comment, UserDto userDto) throws RuntimeException {
        comment.setModifier(userDto);
        comment.setGmtModified(LocalDateTime.now());

        CommentPo po = CloneFactory.copy(new CommentPo(), comment);
        log.debug("save: po = {}", po);
        commentMapper.save(po);
    }

    /**
     * 查找某商品有效评论
     * @param productId
     * @param page
     * @param pageSize
     * @return
     * @throws RuntimeException
     */

    public List<Comment> retrieveReviewedByProductId(Long productId, Integer page, Integer pageSize) throws RuntimeException {
        return retrieveByProductIdAndStatus(productId, Comment.REVIEWED, page, pageSize);
    }

    /**
     * 根据ProductId和状态查找评论
     * @param productId
     * @param status
     * @param page
     * @param pageSize
     * @return
     * @throws RuntimeException
     */
    public List<Comment> retrieveByProductIdAndStatus(Long productId, Byte status, Integer page, Integer pageSize) throws RuntimeException {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<CommentPo> commentPos = commentMapper.findByProductIdEqualsAndStatusEquals(productId, status, pageable);
        log.debug("retrieveByProductIdAndStatus: commentPosSize = {}", commentPos.size());
        return commentPos.stream().map(this::build).toList();
    }

    /**
     * 根据id删除评论
     * @param id
     * @throws RuntimeException
     */
    public void delete(Long id) throws RuntimeException {
        log.debug("deleteById: id = {}",id);
        commentMapper.deleteById(id);
    }

    /**
     * 根据父id收集评论
     * @param id
     * @return
     * @throws RuntimeException
     */

    public List<Comment> retrieveCommentsByPid(Long id) throws RuntimeException {
        List<CommentPo> commentPos = commentMapper.findByPid(id);
        log.debug("retrieveCommentsByPid: commentPosSize = {}", commentPos.size());
        return commentPos.stream().map(this::build).toList();
    }
}
