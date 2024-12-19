package cn.edu.xmu.oomall.comment.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.dao.CommentDao;
import cn.edu.xmu.oomall.comment.mapper.po.CommentPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyTo(CommentPo.class)
@CopyFrom(CommentPo.class)
public class Comment extends OOMallObject implements Serializable {

    Long id;
    Long pid;
    Long uid;
    Byte status;
    String content;
    String updateTime;
    Long shopId;
    Long productId;
    List<Comment> relatedComments;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    private CommentDao commentDao;

    @ToString.Exclude
    @JsonIgnore
    public static final Long ROOT_ID = -1L;

    private static final int MAX_ADDITIONAL_COMMENT = 2;

    /**
     * 共三种状态
     */
    //待审
    @ToString.Exclude
    @JsonIgnore
    public static final Byte PENDING = 0;
    //审核通过
    @ToString.Exclude
    @JsonIgnore
    public static final Byte REVIEWED = 1;

    @ToString.Exclude
    @JsonIgnore
    public static final Byte REQUESTING_BLOCK = 2;

    //封禁
    @ToString.Exclude
    @JsonIgnore
    public static final Byte BANNED = 3;

    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> STATUSNAMES = new HashMap<>() {
        {
            put(PENDING, "待审");
            put(REVIEWED, "审核通过");
            put(REQUESTING_BLOCK, "请求屏蔽");
            put(BANNED, "封禁");
        }
    };

    /**
     * 允许的状态迁移
     */
    @JsonIgnore
    @ToString.Exclude
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>() {
        {
            put(PENDING, new HashSet<>() {
                {
                    add(REVIEWED);
                    add(BANNED);
                }
            });
            put(REVIEWED, new HashSet<>() {
                {
                    add(BANNED);
                    add(REQUESTING_BLOCK);
                }
            });
            put(REQUESTING_BLOCK, new HashSet<>() {
                {
                    add(REVIEWED);
                    add(BANNED);
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     * @param status 迁移去的状态
     */
    public boolean allowTransitStatus(Byte status) {
        boolean ret = false;
        assert (!Objects.isNull(this.status)):String.format("评论对象(id=%d)的状态为null",this.getId());
        Set<Byte> allowStatusSet = toStatus.get(this.status);
        if (!Objects.isNull(allowStatusSet)) {
            ret = allowStatusSet.contains(status);
        }
        return ret;
    }

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * 获得相关评论:追评和回复
     * @return 相关评论的列表
     */

    public List<Comment> getRelatedComments() {
        if (Objects.nonNull(commentDao)) {
            this.relatedComments = commentDao.retrieveCommentsByPid(id);
        }
        return relatedComments;
    }

    /**
     * 物理删除评论(以及其相关评论)
     * 限制追评和回复数,递归深度应该不大于3
     */
    public void delete() {
        getRelatedComments().forEach(Comment::delete);
        commentDao.delete(id);
    }

    /**
     * 管理员审核评论
     * @param userDto
     */
    public void approve(UserDto userDto) {
        if (!allowTransitStatus(REVIEWED)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "评论", this.id, STATUSNAMES.get(this.status)));
        }
        setStatus(REVIEWED);
        commentDao.save(this, userDto);
    }

    /**
     * 审核不通过或者封禁评论
     * @param userDto
     */
    public void ban(UserDto userDto) {
        if (!allowTransitStatus(BANNED)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "评论", this.id, STATUSNAMES.get(this.status)));
        }
        setStatus(BANNED);
        setContent("**该评论被封禁**");
        commentDao.save(this, userDto);
    }

    /**
     * 增加追评
     * @param comment
     * @param userDto
     */
    public void addAdditionalComment(Comment comment, UserDto userDto) {
        //是否是该用户自己的评论
        if (!id.equals(userDto.getId())) {
            throw new BusinessException(ReturnNo.COMMENT_OUTSCOPE);
        }
        //追评数是否超过了上限
        if (getRelatedComments().stream()
                .filter(bo -> uid.equals(bo.getUid()))
                .count() >= MAX_ADDITIONAL_COMMENT) {
            throw new BusinessException(ReturnNo.ADDITIONAL_COMMENT_OUTLIMIT);
        }
        comment.setPid(this.id);
        commentDao.insert(comment, userDto);
    }

    /**
     * 增加回复
     * @param comment
     * @param shopId
     * @param userDto
     */
    public void addReplyComment(Comment comment, Long shopId, UserDto userDto) {
        //是否是自己商铺的评论
        if (!this.shopId.equals(shopId)) {
            throw new BusinessException(ReturnNo.COMMENT_OUTSCOPE);
        }
        //评论已有回复则不能再回复(只能修改)
        if (getRelatedComments().stream().anyMatch(bo -> uid.equals(bo.getUid()))) {
            throw new BusinessException(ReturnNo.REPLY_COMMENT_OUTLIMITE);
        }
        comment.setPid(this.id);
        commentDao.insert(comment, userDto);
    }
}
