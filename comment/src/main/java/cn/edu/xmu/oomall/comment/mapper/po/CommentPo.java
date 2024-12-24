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
    private Long id;
    private Long pid;
    private Long uid;
    private Byte status;
    private String content;
    private LocalDateTime updateTime;
    private Long shopId;
    private Long productId;
    private Long creatorId;
    private String creatorName;
    private Long modifierId;
    private String modifierName;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
