package cn.edu.xmu.oomall.comment.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import cn.edu.xmu.oomall.comment.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RefreshScope
@RequiredArgsConstructor
public class CommentDao {
    private final CommentMapper commentMapper;

    public void build(Comment bo) {
        bo.setCommentDao(this);
    }

    public Comment insert(Comment bo, UserDto user) throws RuntimeException {

        return bo;
    }

    public void deleteByCommentId(Long id) throws RuntimeException {
    }

    public List<Comment> retrieveRelatedCommentsById(Long id) throws RuntimeException {
        List<Comment> relatedComments = new ArrayList<>();
        //TODO
        return relatedComments;
    }
}
