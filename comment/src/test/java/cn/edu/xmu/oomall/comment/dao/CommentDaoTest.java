package cn.edu.xmu.oomall.comment.dao;

import cn.edu.xmu.oomall.comment.CommentTestApplication;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CommentTestApplication.class)
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
}
