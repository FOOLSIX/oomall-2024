package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartItemTest {

    @Mock
    private Cart cart;

    @Mock
    private UserDto userDto;

    @InjectMocks
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCartItemConstructor() {
        // 设置Mock对象的行为
        when(cart.getId()).thenReturn(100L);
        when(userDto.getName()).thenReturn("John Doe");

        cartItem = new CartItem(cart, userDto);

        assertEquals(100L, cartItem.getCreatorId());
        assertEquals("John Doe", cartItem.getCreatorName());
        assertNotNull(cartItem.getGmtCreate());
        assertNotNull(cartItem.getGmtModified());

        assertTrue(cartItem.getGmtCreate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(cartItem.getGmtModified().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    public void testSetters() {
        cartItem.setProductId(123L);
        cartItem.setQuantity(10);

        assertEquals(123L, cartItem.getProductId());
        assertEquals(10, cartItem.getQuantity());
    }

    @Test
    public void testSetGmtCreate() {
        LocalDateTime testTime = LocalDateTime.now();
        cartItem.setGmtCreate(testTime);

        assertEquals(testTime, cartItem.getGmtCreate());
    }

    @Test
    public void testSetGmtModified() {
        LocalDateTime testTime = LocalDateTime.now().plusDays(1);
        cartItem.setGmtModified(testTime);

        assertEquals(testTime, cartItem.getGmtModified());
    }

    @Test
    public void testModifier() {

        cartItem.setModifier(userDto);

        Long id = cartItem.getModifierId();
        assertEquals(userDto.getId(), id);
    }
}
