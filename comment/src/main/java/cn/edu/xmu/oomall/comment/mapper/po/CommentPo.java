package cn.edu.xmu.oomall.comment.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "comment_comment")
@AllArgsConstructor
@NoArgsConstructor
@CopyFrom(Comment.class)
public class CommentPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long pid;
    Long uid;
    Byte status;
    String content;
    String createTime;
    Long shopId;
    Long productId;
}