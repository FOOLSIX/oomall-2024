package cn.edu.xmu.oomall.comment.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.comment.CommentTestApplication;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CommentTestApplication.class)
@Transactional
public class CommentDaoTest {
    @Autowired
    CommentDao commentDao;

    @Test
    public void testFindById() {
        Comment comment = commentDao.findById(10L);
        assertThat(comment).isNotNull();
        assertThat(comment.getPid()).isEqualTo(0L);
        assertThat(comment.getProductId()).isEqualTo(1010L);
        assertThat(comment.getRelatedComments().size()).isEqualTo(0);
    }

    @Test
    public void testFindByIdWithRelatedComment() {
        Comment comment = commentDao.findById(1L);
        assertThat(comment).isNotNull();
        assertThat(comment.getPid()).isEqualTo(0L);
        assertThat(comment.getProductId()).isEqualTo(1001L);
        assertThat(comment.getRelatedComments().size()).isNotZero();
    }

    @Test
    public void testRetrieveReviewedByProductId() {
        List<Comment> comments = commentDao.retrieveReviewedByProductId(1001L, 1, 10);
        assertThat(comments).isNotNull();
        assertThat(comments).allMatch(comment -> comment.getProductId() == 1001L && Objects.equals(comment.getStatus(), Comment.REVIEWED));
    }

    @Test
    public void testRetrieveCommentsByStatus() {
        List<Comment> comments = commentDao.retrieveCommentsByStatus(Comment.PENDING, 1, 10);
        assertThat(comments).isNotNull();
        assertThat(comments).allMatch(comment -> Objects.equals(comment.getStatus(), Comment.PENDING));
    }

    @Test
    public void testSave() {
        Comment comment = commentDao.findById(1L);
        assertThat(comment.getStatus()).isEqualTo(Comment.REVIEWED);
        comment.setStatus(Comment.BANNED);
        commentDao.save(comment, new UserDto(0L, "admin", 0L, 0));
    }

}
