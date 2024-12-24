package cn.edu.xmu.oomall.comment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import cn.edu.xmu.oomall.comment.mapper.jpa.CommentMapper;
import cn.edu.xmu.oomall.comment.mapper.po.CommentPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Comment bo = CloneFactory.copy(new Comment(), po);
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
        comment.setUpdateTime(LocalDateTime.now());
        comment.setStatus(Comment.PENDING);

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
        log.debug("retrieveReviewedByProductId: productId = {}", productId);
        return retrieveByProductIdAndPidAndStatusIn(productId, Comment.ROOT_ID, List.of(Comment.REVIEWED, Comment.REQUESTING_BLOCK), page, pageSize);
    }

    /**
     * 根据状态查找评论
     * @param status
     * @param page
     * @param pageSize
     * @return
     * @throws RuntimeException
     */

    public List<Comment> retrieveCommentsByStatus(Byte status, Integer page, Integer pageSize) throws RuntimeException {
        log.debug("retrieveCommentsByStatus: status = {}", status);
        List<CommentPo> commentPos = commentMapper.findByStatus(status, PageRequest.of(page, pageSize));
        log.debug("retrieveCommentsByStatus: commentPosSize = {}", commentPos.size());
        return commentPos.stream().map(this::build).toList();
    }

    /**
     * 根据productId,pid,状态查找评论
     * @param productId
     * @param pid
     * @param statuses
     * @param page
     * @param pageSize
     * @return
     * @throws RuntimeException
     */
    public List<Comment> retrieveByProductIdAndPidAndStatusIn(Long productId, Long pid, List<Byte> statuses, Integer page, Integer pageSize) throws RuntimeException {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<CommentPo> commentPos = commentMapper.findByProductIdAndPidAndStatusIn(productId, pid, statuses, pageable);
        log.debug("retrieveByProductIdAndStatus: commentPosSize = {}", commentPos.size());
        return commentPos.stream().map(this::build).toList();
    }

    /**
     * 根据用户id和状态查找
     * @param uid
     * @param status
     * @param page
     * @param pageSize
     * @return
     * @throws RuntimeException
     */
    public List<Comment> retrieveByUidAndStatus(Long uid, Byte status, Integer page, Integer pageSize) throws RuntimeException {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        List<CommentPo> commentPos = commentMapper.findByUidEqualsAndStatusEquals(uid, status, pageable);
        log.debug("retrieveByUidAndStatus: commentPosSize = {}", commentPos.size());
        return commentPos.stream().map(this::build).toList();
    }

    /**
     * 根据uid和productId查找评论
     * @param uid
     * @param productId
     * @return
     * @throws RuntimeException
     */
    public Optional<Comment> findByUidAndProductId(Long uid, Long productId) throws RuntimeException {
        Optional<CommentPo> comment = commentMapper.findByUidEqualsAndProductIdEquals(uid, productId);
        return comment.map(this::build);
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

    /**
     * 根据id查找评论
     * @param id
     * @return
     * @throws RuntimeException
     */

    public Comment findById(Long id) throws RuntimeException {
        log.debug("findById: id = {}", id);
        Optional<CommentPo> po = commentMapper.findById(id);
        if (po.isPresent()) {
            return build(po.get());
        }
        throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "评论", id));
    }
}
