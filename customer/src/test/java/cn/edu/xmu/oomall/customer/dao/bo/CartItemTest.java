package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import cn.edu.xmu.oomall.customer.dao.CartItemDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartItemTest {

    private CartItem cartItem;
//    private CartPo cartPoMock;
//    private CartItemPo cartItemPoMock;
    private CartItemDao cartItemDaoMock;

    @BeforeEach
    void setUp() {
        cartItem = new CartItem();
//        cartPoMock = new CartPo();
        cartItemDaoMock = mock(CartItemDao.class);
    }

//    @Test
//    void testSetAndGetCartId() {
//        CartItem cartItem1 = cartItemDaoMock.build(cartItemPoMock);
//        /////
//        assertEquals(cartPoMock.getId(), cartItem.getCreatorId());
//    }

    @Test
    void testSetAndGetProductId() {
        Long productId = 12345L;
        cartItem.setProductId(productId);
        assertEquals(productId, cartItem.getProductId());
    }

    @Test
    void testSetAndGetPrice() {
        Long price = 99999L;
        cartItem.setPrice(price);
        assertEquals(price, cartItem.getPrice());
    }

    @Test
    void testSetAndGetQuantity() {
        Long quantity = 10L;
        cartItem.setQuantity(quantity);
        assertEquals(quantity, cartItem.getQuantity());
    }

    @Test
    void testSetAndGetGmtCreate() {
        LocalDateTime now = LocalDateTime.now();
        cartItem.setGmtCreate(now);
        assertEquals(now, cartItem.getGmtCreate());
    }

    @Test
    void testSetAndGetGmtModified() {
        LocalDateTime modifiedTime = LocalDateTime.now().plusDays(1);
        cartItem.setGmtModified(modifiedTime);
        assertEquals(modifiedTime, cartItem.getGmtModified());
    }

    @Test
    void testCartItemDaoInjection() {
        cartItem.setCartItemDao(cartItemDaoMock);
        assertEquals(cartItemDaoMock, cartItem.getCartItemDao());
    }

    @Test
    void testToString() {
        cartItem.setProductId(12345L);
        cartItem.setPrice(99999L);
        cartItem.setQuantity(10L);
        String expected = "CartItem(super=OOMallObject(), cart=null, productId=12345, price=99999, quantity=10, cartItemDao=null, gmtCreate=null)";
        assertTrue(cartItem.toString().contains("productId=12345"));
        assertTrue(cartItem.toString().contains("price=99999"));
        assertTrue(cartItem.toString().contains("quantity=10"));
    }
}

