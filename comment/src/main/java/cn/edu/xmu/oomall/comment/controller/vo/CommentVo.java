package cn.edu.xmu.oomall.comment.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(Comment.class)
public class CommentVo {
    Long id;
    Long pid;
    Long uid;
    Byte status;
    String content;
    String createTime;
    Long shopId;
    Long productId;
    List<Comment> relatedComments;
}
