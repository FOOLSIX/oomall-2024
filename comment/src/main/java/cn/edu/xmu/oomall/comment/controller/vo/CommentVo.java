package cn.edu.xmu.oomall.comment.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(Comment.class)
public class CommentVo {
    private Long id;
    private Long pid;
    private Long uid;
    private Byte status;
    private String content;
    private LocalDateTime updateTime;
    private Long shopId;
    private Long productId;
    @CopyFrom.Exclude(Comment.class)
    private List<CommentVo> relatedComments;
    private IdNameTypeVo creator;
    private IdNameTypeVo modifier;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public CommentVo(Comment o) {
        super();
        CloneFactory.copy(this, o);
        this.setCreator(IdNameTypeVo.builder().id(o.getCreatorId()).name(o.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(o.getModifierId()).name(o.getModifierName()).build());
        this.setRelatedComments(o.getRelatedComments().stream().map(CommentVo::new).toList());
    }
}
