package cn.edu.xmu.oomall.comment.mapper.jpa;

import cn.edu.xmu.oomall.comment.mapper.po.CommentPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentMapper extends JpaRepository<CommentPo, Long> {
    List<CommentPo> findByPid(Long Pid);
    @Query("SELECT c FROM CommentPo c WHERE c.productId = :productId AND c.pid = :pid AND c.status IN :statuses")
    List<CommentPo> findByProductIdAndPidAndStatusIn(@Param("productId") Long productId, @Param("pid") Long pid, @Param("statuses") List<Byte> statuses, Pageable pageable);
    List<CommentPo> findByStatus(Byte status, Pageable pageable);
    List<CommentPo> findByUidEqualsAndStatusEquals(Long pid, Byte status, Pageable pageable);
    Optional<CommentPo> findByUidEqualsAndProductIdEquals(Long uid, Long productId);
}
