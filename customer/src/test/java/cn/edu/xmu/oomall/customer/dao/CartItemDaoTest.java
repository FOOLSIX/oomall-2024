package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.mapper.jpa.CartItemPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CartItemDaoTest {

    @Mock
    private CartItemPoMapper cartItemPoMapper;

    @InjectMocks
    private CartItemDao cartItemDao;

    public CartItemDaoTest() {
        MockitoAnnotations.openMocks(this); // 初始化 Mock 对象
    }

    @Test
    void testFindById() {
        Long validId = 1L;
        CartItemPo cartItemPo = new CartItemPo();
        cartItemPo.setId(validId);
        when(cartItemPoMapper.findById(validId)).thenReturn(Optional.of(cartItemPo));

        CartItem result = cartItemDao.findById(validId);

        assertNotNull(result, "The result should not be null for a valid id.");
        assertEquals(validId, result.getId(), "The returned Cart id should match the input id.");
        verify(cartItemPoMapper, times(1)).findByCartId(validId);

        try {
            cartItemDao.findById(null);
        } catch (BusinessException e) {
            assertEquals(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo(), e.getErrno().getErrNo());
            assertEquals("id is null", e.getMessage());
        }

        verify(cartItemPoMapper, never()).findById(null);
    }


    @Test
    void testGetAllCartItem_WithPaging() {
        Long cartId = 1L;
        int page = 1;
        int pageSize = 2;

        CartItemPo po1 = new CartItemPo();
        CartItemPo po2 = new CartItemPo();
        List<CartItemPo> mockCartItemPoList = List.of(po1, po2);

        when(cartItemPoMapper.findByCartId(eq(cartId), any(Pageable.class)))
                .thenReturn(mockCartItemPoList);
        List<CartItem> result = cartItemDao.getAllCartItem(cartId, page, pageSize);

        assertNotNull(result, "返回结果不应为 null");
        assertEquals(2, result.size(), "返回的 CartItem 列表大小不匹配");
        verify(cartItemPoMapper, times(1)).findByCartId(eq(cartId), any(Pageable.class));
    }

    @Test
    void testGetAllCartItem_NoPaging() {
        Long cartId = 1L;
        CartItemPo po1 = new CartItemPo();
        CartItemPo po2 = new CartItemPo();
        List<CartItemPo> mockCartItemPoList = List.of(po1, po2);

        when(cartItemPoMapper.findByCartId(cartId)).thenReturn(mockCartItemPoList);
        List<CartItem> result = cartItemDao.getAllCartItem(cartId);

        assertNotNull(result, "返回结果不应为 null");
        assertEquals(2, result.size(), "返回的 CartItem 列表大小不匹配");
        verify(cartItemPoMapper, times(1)).findByCartId(cartId);
    }

    @Test
    void testGetAllCartItem_InvalidId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartItemDao.getAllCartItem(0L);
        });

        assertEquals("findById: id is 0", exception.getMessage(), "异常消息不匹配");
        verify(cartItemPoMapper, never()).findByCartId(any());
    }

    @Test
    void testSaveCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        CartItemPo mockCartItemPo = new CartItemPo();

        when(CloneFactory.copy(any(CartItemPo.class), eq(cartItem))).thenReturn(mockCartItemPo);
        doNothing().when(cartItemPoMapper).save(any(CartItemPo.class));

        cartItemDao.saveCartItem(cartItem);

        verify(cartItemPoMapper, times(1)).save(mockCartItemPo);
    }

    @Test
    void testDeleteCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        CartItemPo mockCartItemPo = new CartItemPo();

        when(CloneFactory.copy(any(CartItemPo.class), eq(cartItem))).thenReturn(mockCartItemPo);
        doNothing().when(cartItemPoMapper).delete(any(CartItemPo.class));

        cartItemDao.deleteCartItem(cartItem);

        verify(cartItemPoMapper, times(1)).delete(mockCartItemPo);
    }
}
