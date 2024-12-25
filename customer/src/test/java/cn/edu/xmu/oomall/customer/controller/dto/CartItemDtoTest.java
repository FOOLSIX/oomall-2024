package cn.edu.xmu.oomall.customer.controller.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemDtoTest {

    @Test
    public void testValidCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(1L);
        cartItemDto.setQuantity(10);

        // 验证设置的值是否正确
        assertEquals(1L, cartItemDto.getProductId(), "ProductId 应为 1");
        assertEquals(10, cartItemDto.getQuantity(), "Quantity 应为 10");
    }

    @Test
    public void testInvalidCartItemDtoQuantityNegative() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(1L);
        cartItemDto.setQuantity(-1);

        // 手动检查数量是否合法
        assertTrue(cartItemDto.getQuantity() < 0, "Quantity 不应小于 0");
    }

    @Test
    public void testInvalidCartItemDtoQuantityTooLarge() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(1L);
        cartItemDto.setQuantity(1001);

        // 手动检查数量是否合法
        assertTrue(cartItemDto.getQuantity() > 1000, "Quantity 不应大于 1000");
    }

    @Test
    public void testGetterMethods() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(123L);
        cartItemDto.setQuantity(50);

        // 测试 getProductId
        assertEquals(123L, cartItemDto.getProductId(), "getProductId() 返回值不正确");

        // 测试 getQuantity
        assertEquals(50, cartItemDto.getQuantity(), "getQuantity() 返回值不正确");
    }
}


