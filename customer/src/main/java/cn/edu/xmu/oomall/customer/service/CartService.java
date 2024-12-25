package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.controller.vo.CartItemVo;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.CartItemDao;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.edu.xmu.javaee.core.util.CloneFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
     * 买家修改购物车单个商品的数量或规格
     * @param id
     * @param requestBody
     */

    public ReturnObject changeCartInfo(Long id,Map<Long, Long> requestBody) {
        List<CartItem> cartItems = cartItemDao.getAllCartItem(id);
        Long productId = ((Number) requestBody.values().toArray()[0]).longValue();
        Long quantity = ((Number) requestBody.values().toArray()[1]).longValue();

        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                //item.setQuantity(quantity);
                break;
            }
        }

        return new ReturnObject();
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
