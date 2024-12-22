package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.controller.vo.CartItemVo;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.mapper.jpa.CartItemPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class CartItemDao {

    private final RedisUtil redisUtil;

    private final CartItemPoMapper cartItemPoMapper;

    @Autowired
    public CartItemDao(RedisUtil redisUtil, CartItemPoMapper carItemPoMapper) {
        this.redisUtil = redisUtil;
        this.cartItemPoMapper = carItemPoMapper;
    }

    public List<CartItem> getCartItemPoByCartId(long id) {
        if (id == 0) {
            throw new IllegalArgumentException("findById: id is 0");
        }

        List<CartItemPo> cartItemPoList = cartItemPoMapper.findByCartId(id);
        if (cartItemPoList == null || cartItemPoList.isEmpty()) {
            return Collections.emptyList();
        }

        List<CartItem> cartItemList = cartItemPoList.stream()
                .map(po -> CloneFactory.copy(new CartItem(), po))
                .collect(Collectors.toList());
        return cartItemList;
    }
    public List<CartItemVo> getCartItemsByPage(Cart cart, int page , int pagesize){

        if (cart == null) {throw new RuntimeException("Cart not found for user.");}

        Pageable pageable = PageRequest.of(page-1, pagesize);

        List<CartItemPo> paginatedItems = cartItemPoMapper.findByCartIdWithPagination(cart.getId(), pageable);;
        List<CartItemVo> ret = paginatedItems.stream()
                .map(item -> CloneFactory.copy(new CartItemVo(), item))
                .collect(Collectors.toList());

        return ret;
    }



}
