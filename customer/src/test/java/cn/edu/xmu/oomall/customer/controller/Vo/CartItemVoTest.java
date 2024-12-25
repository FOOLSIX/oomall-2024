package cn.edu.xmu.oomall.customer.controller.Vo;

import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.customer.controller.vo.CartItemVo;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemVoTest {

    private CartItem cartItem;
    private CartItemVo cartItemVo;

    @BeforeEach
    void setUp() {
        // 创建一个模拟的 CartItem 对象
        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCreatorId(2L);
        cartItem.setCreatorName("Admin");
        cartItem.setProductId(101L);
        cartItem.setModifierId(3L);
        cartItem.setModifierName("Editor");
//        cartItem.setPrice(500);
        cartItem.setQuantity(2);
        cartItem.setGmtCreate(LocalDateTime.of(2023, 12, 25, 10, 30));
        cartItem.setGmtModified(LocalDateTime.of(2023, 12, 26, 12, 15));

        cartItemVo = new CartItemVo(cartItem);
    }

    @Test
    void testConstructor() {
        assertEquals(1L, cartItemVo.getId());
        assertEquals(2L, cartItemVo.getCreatorId());
        assertEquals("Admin", cartItemVo.getCreator().getName());
        assertEquals(101L, cartItemVo.getProductId());
        assertEquals(3L, cartItemVo.getModifier().getId());
        assertEquals("Editor", cartItemVo.getModifier().getName());
//        assertEquals(500, cartItemVo.getPrice());
        assertEquals(2, cartItemVo.getQuantity());
        assertEquals(LocalDateTime.of(2023, 12, 25, 10, 30), cartItemVo.getGmtCreate());
        assertEquals(LocalDateTime.of(2023, 12, 26, 12, 15), cartItemVo.getGmtModified());
    }

    @Test
    void testSettersAndGetters() {
        cartItemVo.setId(2L);
        assertEquals(2L, cartItemVo.getId());

        cartItemVo.setPrice(1000);
        assertEquals(1000, cartItemVo.getPrice());

        cartItemVo.setQuantity(5);
        assertEquals(5, cartItemVo.getQuantity());

        cartItemVo.setGmtCreate(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), cartItemVo.getGmtCreate());

        cartItemVo.setGmtModified(LocalDateTime.of(2024, 1, 2, 12, 0));
        assertEquals(LocalDateTime.of(2024, 1, 2, 12, 0), cartItemVo.getGmtModified());
    }

    @Test
    void testCreatorAndModifier() {
        IdNameTypeVo newCreator = IdNameTypeVo.builder().id(10L).name("New Admin").build();
        cartItemVo.setCreator(newCreator);
        assertEquals(newCreator, cartItemVo.getCreator());

        IdNameTypeVo newModifier = IdNameTypeVo.builder().id(20L).name("New Editor").build();
        cartItemVo.setModifier(newModifier);
        assertEquals(newModifier, cartItemVo.getModifier());
    }

    @Test
    void testProduct() {
        IdNameTypeVo newProduct = IdNameTypeVo.builder().id(200L).name("New Product").build();
        cartItemVo.setProduct(newProduct);
        assertEquals(newProduct, cartItemVo.getProduct());
    }
}

