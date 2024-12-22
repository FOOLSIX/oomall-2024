package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.controller.vo.CartItemVo;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.CartItemDao;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.edu.xmu.javaee.core.util.CloneFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor

public class CartService {
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    private final RedisUtil redisUtil;

    public Object getCartItems(UserDto userDto, int page, int pageSize) {
        long id = userDto.getId();

        Cart cart = cartDao.findById(id);
        if (cart == null) {
            throw new RuntimeException("Cart not found for user id: " + id);
        }

        List<CartItem> cartItems = cartItemDao.getCartItemPoByCartId(cart.getId());
        if (cartItems == null || cartItems.isEmpty()) {
            return Collections.emptyList();
        }

        int fromIndex = Math.min((page - 1) * pageSize, cartItems.size());
        int toIndex = Math.min(fromIndex + pageSize, cartItems.size());

        List<CartItemVo> pagedItems = cartItems.subList(fromIndex, toIndex).stream()
                .map(item -> {
                    return CloneFactory.copy(new CartItemVo(), item);
                })
                .collect(Collectors.toList());

        return pagedItems;
    }



}
