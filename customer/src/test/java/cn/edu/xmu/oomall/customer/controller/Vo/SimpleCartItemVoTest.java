package cn.edu.xmu.oomall.customer.controller.Vo;

import cn.edu.xmu.oomall.customer.controller.vo.SimpleCartItemVo;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimpleCartItemVoTest {

    @Test
    void testSimpleCartItemVoConstruction() {
        // 创建 CartItem 的 mock 对象
        CartItem mockCartItem = mock(CartItem.class);

        // 设置 mock 对象的返回值
        when(mockCartItem.getId()).thenReturn(1L);
        when(mockCartItem.getProductId()).thenReturn(1001L);
        when(mockCartItem.getQuantity()).thenReturn(5);

        // 使用 mock 对象创建 SimpleCartItemVo
        SimpleCartItemVo simpleCartItemVo = new SimpleCartItemVo(mockCartItem);

        // 验证字段是否正确复制
        assertEquals(1L, simpleCartItemVo.getId());
        assertEquals(1001L, simpleCartItemVo.getProductid());
        assertEquals(5, simpleCartItemVo.getQuantity());
        assertEquals(0, simpleCartItemVo.getPrice()); // 默认值
    }

    @Test
    void testSimpleCartItemVoWithNullCartItem() {
        // 测试 null 输入时的行为
        CartItem nullCartItem = null;

        // 确保构造方法能够正确处理空对象
        assertThrows(NullPointerException.class, () -> new SimpleCartItemVo(nullCartItem));
    }
}

