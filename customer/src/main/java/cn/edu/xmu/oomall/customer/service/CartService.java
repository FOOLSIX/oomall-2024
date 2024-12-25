package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.CartItemDao;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor

public class CartService {
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    private final RedisUtil redisUtil;

    public Cart getById(Long id) {
        return cartDao.findById(id);
    }

    public Cart getCustomertCart(Long id) {
        return cartDao.findByCreatorId(id);
    }

    /**
     * 买家获得购物车列表
     * @param cart
     * @param page
     * @param pageSize
     */

    public List<CartItem> getCartItems(Cart cart, int page, int pageSize) {
        List<CartItem> cartItems = cartItemDao.getAllCartItem(cart.getId(),page,pageSize);
        return cartItems;
    }

    public CartItem getCartItemById(Long id) {
        return cartItemDao.findById(id);
    }
    /**
     * 买家将商品加入购物车
     * @param cartitem
     *
     */

    public void postCartItem(CartItem cartitem) {
        cartItemDao.saveCartItem(cartitem);
        Cart cart = cartDao.findByCreatorId(cartitem.getCreatorId());
        cartDao.addquantity(cart);
    }

    /**
     * 买家清空购物车
     * @param cart
     *
     */
    public List<CartItem> getAllCartItem(Cart cart) {

        List<CartItem> cartItems = cartItemDao.getAllCartItem(cart.getId());
        return cartItems;
    }

    /**
     * 删除购物车内容
     * @param cartItem
     *
     */

    public void deleteCartItem(CartItem cartItem) {
        cartItemDao.deleteCartItem(cartItem);
    }
}
