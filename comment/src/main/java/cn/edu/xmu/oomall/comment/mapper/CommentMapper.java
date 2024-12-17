package cn.edu.xmu.oomall.comment.mapper;

import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends JpaRepository<Comment, Long> {
}
