package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.mapper.jpa.CartItemPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.*;
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

    public CartItem build(CartItemPo cartItemPo) {
        CartItem cartItem = CloneFactory.copy(new CartItem(), cartItemPo);
        this.build(cartItem);
        return cartItem;
    }

    private CartItem build(CartItem bo){
        bo.setCartItemDao(this);
        return bo;
    }

    public List<CartItem> getAllCartItem(Long id,Integer page, Integer pageSize) {
        if (id == 0) {
            throw new IllegalArgumentException("findById: id is 0");
        }
        List<CartItem> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id");
        List<CartItemPo> cartItemPoPage = this.cartItemPoMapper.findByCartId(id, pageable);
        ret  = cartItemPoPage.stream().map(po -> CloneFactory.copy(new CartItem(),po)).collect(Collectors.toList());

        return ret;
    }

    public List<CartItem> getAllCartItem(Long id) {
        if (id == 0) {
            throw new IllegalArgumentException("findById: id is 0");
        }
        List<CartItem> ret = new ArrayList<>();
        List<CartItemPo> cartItemPoPage = this.cartItemPoMapper.findByCartId(id);
        ret  = cartItemPoPage.stream().map(po -> CloneFactory.copy(new CartItem(),po)).collect(Collectors.toList());

        return ret;
    }

    public void saveCartItem(CartItem cartItem) {
        CartItemPo cartItemPo = CloneFactory.copy(new CartItemPo(),cartItem);
        cartItemPoMapper.save(cartItemPo);
    }

    public void deleteCartItem(CartItem cartItem){
        CartItemPo cartItemPo = CloneFactory.copy(new CartItemPo(),cartItem);
        cartItemPoMapper.delete(cartItemPo);
    }
}