package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.order.OrderTestApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest(classes = OrderTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String adminToken;

    private static String customerToken;

    private static String shopToken;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        customerToken = jwtHelper.createToken(2L, "user1", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(3L, "shop1", 0L, 1, 3600);
    }


    /**
     * 商家确认订单
     *
     * @throws Exception
     */
    @Test
    public void testConfirmOrderValidId() throws Exception {
        Long OrderId = 5L;

        mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/orders/{id}/confirm", 3L, OrderId)
                        .header("authorization", shopToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }


    /**
     * 商家确认订单，传入无效订单id
     *
     * @throws Exception
     */
    @Test
    public void testConfirmOrderInvalidId() throws Exception {
        Long invalidOrderId = -1L;

        mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/orders/{id}/confirm", 1L, invalidOrderId)
                        .header("authorization", shopToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("订单id字段不合法"));
    }


    /**
     * 商家取消订单
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderValidId() throws Exception {
        Long validOrderId = 3L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/orders/{id}", 1L, validOrderId)
                        .header("authorization", shopToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }

    /**
     * 商家取消订单传入无效id
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderInvalidId() throws Exception {
        Long invalidOrderId = -1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/orders/{id}", 1L, invalidOrderId)
                        .header("authorization", shopToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("订单id字段不合法"));
    }


}
