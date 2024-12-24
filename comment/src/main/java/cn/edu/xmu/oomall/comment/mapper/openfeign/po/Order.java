package cn.edu.xmu.oomall.comment.mapper.openfeign.po;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.dao.CommentDao;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Order {

    private Long id;
    private Byte status;
    private Long shopId;
    List<Long> productIds;
    private Boolean isCompleted;

    private CommentDao commentDao;

    public void createComment(Comment comment, Long productId, UserDto user) {
        //订单完成,有该商品
        if (!isCompleted || productIds.stream().noneMatch(id -> id.equals(productId))) {
            throw new BusinessException(ReturnNo.COMMENT_CANNOT_CREATE);
        }
        //评论过
        Optional<Comment> origin = commentDao.findByUidAndProductId(user.getId(), productId);
        if (origin.isPresent()) {
            throw new BusinessException(ReturnNo.COMMENT_CANNOT_CREATE);
        }
        comment.setPid(Comment.ROOT_ID);
        comment.setUid(user.getId());
        comment.setProductId(productId);
        comment.setShopId(shopId);
        comment.setGmtCreate(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        comment.setStatus(Comment.PENDING);
        commentDao.insert(comment, user);
    }
}
