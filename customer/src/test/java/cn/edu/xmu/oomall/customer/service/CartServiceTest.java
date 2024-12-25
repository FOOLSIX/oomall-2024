package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.CartItemDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import  cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartDao cartDao;

    @Mock
    private CartItemDao cartItemDao;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById() {
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        when(cartDao.findById(1L)).thenReturn(mockCart);

        Cart result = cartService.getById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(cartDao, times(1)).findById(1L);
    }

    @Test
    public void testGetCustomerCart() {
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        when(cartDao.findByCreatorId(1L)).thenReturn(mockCart);

        Cart result = cartService.getCustomertCart(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(cartDao, times(1)).findByCreatorId(1L);
    }

    @Test
    public void testGetCartItems() {
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        CartItem item1 = new CartItem();
        item1.setId(101L);
        CartItem item2 = new CartItem();
        item2.setId(102L);

        List<CartItem> mockCartItems = Arrays.asList(item1, item2);
        when(cartItemDao.getAllCartItem(1L, 0, 10)).thenReturn(mockCartItems);

        List<CartItem> result = cartService.getCartItems(mockCart, 0, 10);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(cartItemDao, times(1)).getAllCartItem(1L, 0, 10);
    }

    @Test
    public void testPostCartItem() {
        CartItem mockItem = new CartItem();
        mockItem.setCreatorId(1L);

        Cart mockCart = new Cart();
        mockCart.setId(1L);

        when(cartDao.findByCreatorId(1L)).thenReturn(mockCart);

        cartService.postCartItem(mockItem);

        verify(cartItemDao, times(1)).saveCartItem(mockItem);
        verify(cartDao, times(1)).addquantity(mockCart);
    }

    @Test
    public void testGetAllCartItem() {
        Cart mockCart = new Cart();
        mockCart.setId(1L);

        CartItem item1 = new CartItem();
        item1.setId(101L);
        CartItem item2 = new CartItem();
        item2.setId(102L);

        List<CartItem> mockCartItems = Arrays.asList(item1, item2);
        when(cartItemDao.getAllCartItem(1L)).thenReturn(mockCartItems);

        List<CartItem> result = cartService.getAllCartItem(mockCart);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(cartItemDao, times(1)).getAllCartItem(1L);
    }

    @Test
    public void testDeleteCartItem() {
        CartItem mockItem = new CartItem();
        mockItem.setId(101L);

        cartService.deleteCartItem(mockItem);

        verify(cartItemDao, times(1)).deleteCartItem(mockItem);
    }

    @Test
    public void testGetCartItemById() {
        CartItem mockItem = new CartItem();
        mockItem.setId(1L);
        mockItem.setProductId(101L);
        mockItem.setQuantity(5);

        when(cartItemDao.findById(1L)).thenReturn(mockItem);

        CartItem result = cartService.getCartItemById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(101L, result.getProductId());
        assertEquals(5L, result.getQuantity());

        verify(cartItemDao, times(1)).findById(1L);
    }
}

