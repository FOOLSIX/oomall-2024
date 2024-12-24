package cn.edu.xmu.oomall.comment.mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    LocalDateTime updateTime;
    Long shopId;
    Long productId;
}
