package cn.edu.xmu.oomall.comment.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.comment.dao.CommentDao;
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
public class Comment extends OOMallObject implements Serializable {

    Long id;
    Long pid;
    Long uid;
    Byte status;
    String content;
    String createTime;
    Long shopId;
    Long productId;
    List<Comment> relatedComments;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    private CommentDao commentDao;

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
    //封禁
    @ToString.Exclude
    @JsonIgnore
    public static final Byte BANNED = 2;

    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> STATUSNAMES = new HashMap<>() {
        {
            put(PENDING, "待审");
            put(REVIEWED, "审核通过");
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
        if (Objects.isNull(relatedComments) && Objects.nonNull(commentDao)) {
            this.relatedComments = commentDao.retrieveRelatedCommentsById(id);
        }
        return relatedComments;
    }

}
