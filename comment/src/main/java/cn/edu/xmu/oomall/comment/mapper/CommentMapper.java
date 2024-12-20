package cn.edu.xmu.oomall.comment.mapper;

import cn.edu.xmu.oomall.comment.mapper.po.CommentPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends JpaRepository<CommentPo, Long> {
    List<CommentPo> findByProductIdEqualsAndStatusEquals(Long productId, Byte status, Pageable pageable);
    List<CommentPo> findByPid(Long Pid);
    List<CommentPo> findByStatus(Byte status, Pageable pageable);
}
