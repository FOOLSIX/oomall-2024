package cn.edu.xmu.oomall.customer.mapper.jpa;

import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CartItemPoMapper extends JpaRepository<CartItemPo, Long> {
        List<CartItemPo> findByCartId(Long cartId);
        List<CartItemPo> findByCartId(Long cartId, Pageable pageable);
    }
