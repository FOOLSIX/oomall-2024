package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.mapper.jpa.CartPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartDaoTest {

    @Mock
    private CartPoMapper cartPoMapper;

    @InjectMocks
    private CartDao cartDao;


    @Test
    void testFindById() {
        Long validId = 1L;
        CartPo cartPo = new CartPo();
        cartPo.setId(validId);
        when(cartPoMapper.findById(validId)).thenReturn(Optional.of(cartPo));

        Cart result = cartDao.findById(validId);

        assertNotNull(result, "The result should not be null for a valid id.");
        assertEquals(validId, result.getId(), "The returned Cart id should match the input id.");
        verify(cartPoMapper, times(1)).findByCreatorId(validId);

        try {
            cartDao.findById(null);
        } catch (BusinessException e) {
            assertEquals(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo(), e.getErrno().getErrNo());
            assertEquals("id is null", e.getMessage());
        }

        verify(cartPoMapper, never()).findById(null);
    }

    @Test
    void testFindByCreatorId() {
        Long validId = 1L;
        CartPo cartPo = new CartPo();
        cartPo.setId(validId);
        when(cartPoMapper.findByCreatorId(validId)).thenReturn(Optional.of(cartPo));

        Cart result = cartDao.findByCreatorId(validId);

        assertNotNull(result, "The result should not be null for a valid id.");
        assertEquals(validId, result.getId(), "The returned Cart id should match the input id.");
        verify(cartPoMapper, times(1)).findByCreatorId(validId);

        // 模拟传入 id 为 null 的情况
        Long nullId = null;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> cartDao.findByCreatorId(nullId),
                "Expected an IllegalArgumentException when id is null."
        );

        assertEquals("findByCreatorId: id is null", exception.getMessage());
        verify(cartPoMapper, never()).findByCreatorId(nullId);
    }

    @Test
    void testAddQuantity() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setQuantity(5);

        cartDao.addquantity(cart);

        assertEquals(6, cart.getQuantity(), "The cart quantity should be incremented by 1.");
        verify(cartPoMapper, times(1)).save(any(CartPo.class));
    }

    @Test
    void testReduceQuantity() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setQuantity(5);

        cartDao.reducequantity(cart);

        assertEquals(4, cart.getQuantity(), "The cart quantity should be decremented by 1.");
        verify(cartPoMapper, times(1)).save(any(CartPo.class));
    }
}

