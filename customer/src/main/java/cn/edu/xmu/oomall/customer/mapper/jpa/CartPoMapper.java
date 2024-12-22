package cn.edu.xmu.oomall.customer.mapper.jpa;


import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.Optional;

@Repository
public interface CartPoMapper extends JpaRepository<CartPo, Long> {
    Optional<CartPo> findByCreatorId(Long creatorId);

}

