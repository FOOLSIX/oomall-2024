package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnObject;
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

    /**
     * 买家获得购物车列表
     * @param id
     * @param page
     * @param pageSize
     */

    public Object getCartItems(Long id, int page, int pageSize) {
        Cart cart = cartDao.findByCreatorId(id);

        if (cart == null) {
            throw new RuntimeException("Cart not found for user id: ");
        }

        List<CartItem> cartItems = cartItemDao.getAllCartItem(cart.getId(),page,pageSize);
        if (cartItems == null || cartItems.isEmpty()) {
            return Collections.emptyList();
        }

        List<CartItemVo> pagedItems = cartItems.stream()
                .map(item -> CloneFactory.copy(new CartItemVo(), item))
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("page",page);
        responseMap.put("pageSize",pageSize);
        responseMap.put("list", pagedItems);
        responseMap.put("gmtCreate", cart.getGmtCreate());
        responseMap.put("gmtModified", cart.getGmtModified());

        return responseMap;
    }

    /**
     * 买家将商品加入购物车
     * @param id
     * @param requestBody
     */

    public ReturnObject postCartItem(Long id, Map<Long, Long> requestBody) {
        Cart cart = cartDao.findByCreatorId(id);

        if (cart == null) {
            throw new RuntimeException("Cart not found for user id: ");
        }

        Long productId = ((Number) requestBody.values().toArray()[0]).longValue();
        Long quantity = ((Number) requestBody.values().toArray()[1]).longValue();

        CartItem cartItem = new CartItem();
        cartItem.setCreatorId(cart.getId());
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setGmtCreate(LocalDateTime.now());
        cartItem.setGmtModified(LocalDateTime.now());

        cartItemDao.saveCartItem(cartItem);
        return new ReturnObject();
    }

    /**
     * 买家清空购物车
     * @param id
     *
     */
    public ReturnObject emptyCart(Long id) {
        Cart cart = cartDao.findByCreatorId(id);

        if (cart == null) {
            throw new RuntimeException("Cart not found for user id: ");
        }
        List<CartItem> cartItems = cartItemDao.getAllCartItem(cart.getId());
        for (CartItem cartItem : cartItems) {
            cartItemDao.deleteCartItem(cartItem);
        }
        return new ReturnObject();
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
                item.setQuantity(quantity);
                break;
            }
        }

        return new ReturnObject();
    }

    /**
     * 买家修改购物车单个商品的数量或规格
     * @param id
     * @param
     */
    //todo
}
