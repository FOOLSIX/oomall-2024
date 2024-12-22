package cn.edu.xmu.oomall.comment.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyTo(Comment.class)
public class CommentDto {
    Long id;
    @NotNull(message = "评论不能为空", groups = {NewGroup.class})
    String content;
    Long shopId;
    Long productId;
}
