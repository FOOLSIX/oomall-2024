package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }


    @Test
    void deleteCartItem_ShouldRemoveCartItem() throws Exception {
        Long cartItemId = 1L;
        CartItem cartItem = new CartItem();

        when(cartService.getCartItemById(cartItemId)).thenReturn(cartItem);

        mockMvc.perform(delete("/carts/{id}", cartItemId))
                .andExpect(status().isOk());

        verify(cartService).getCartItemById(cartItemId);
        verify(cartService).deleteCartItem(cartItem);
    }
}

