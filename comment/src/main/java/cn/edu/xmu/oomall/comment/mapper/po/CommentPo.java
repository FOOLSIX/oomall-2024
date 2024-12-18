package cn.edu.xmu.oomall.comment.mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "comment_comment")
@AllArgsConstructor
@NoArgsConstructor
public class CommentPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long pid;
    Long uid;
    Byte status;
    String content;
    String updateTime;
    Long shopId;
    Long productId;
}
