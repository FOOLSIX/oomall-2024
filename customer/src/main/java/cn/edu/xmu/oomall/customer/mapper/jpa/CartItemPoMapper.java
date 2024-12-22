package cn.edu.xmu.oomall.customer.mapper.jpa;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemPoMapper extends JpaRepository<CartItemPo, Long> {
        List<CartItemPo> findByCartId(Long cartId);
        List<CartItemPo> findByCartIdWithPagination(long cartId, Pageable pageable);
    }
