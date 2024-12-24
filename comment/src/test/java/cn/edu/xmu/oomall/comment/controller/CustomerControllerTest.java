package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.comment.CommentTestApplication;
import cn.edu.xmu.oomall.comment.mapper.openfeign.OrderMapper;
import cn.edu.xmu.oomall.comment.mapper.openfeign.po.Order;
import cn.edu.xmu.oomall.comment.mapper.openfeign.po.OrderItem;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = CommentTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderMapper orderMapper;
    private static String customerToken1;
    private static String customerToken2;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        customerToken1 = jwtHelper.createToken(1L, "customer1", 1L, 2, 3600);
        customerToken2 = jwtHelper.createToken(8L, "customer2", 8L, 2, 3600);
    }

    @Test
    public void testRetrieveComments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/1/comments")
                .header("authorization", customerToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[*].uid").value(Matchers.everyItem(Matchers.is(1))));
    }

    @Test
    public void testCreateComment() throws Exception {
        Order order = new Order();
        order.setId(101L);
        order.setShopId(109L);
        order.setStatus(Order.FINISH);
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1009L);
        orderItems.add(item);
        order.setOrderItems(orderItems);

        Mockito.when(orderMapper.getOrderById(101L)).thenReturn(new InternalReturnObject<>(order));

        String body = "{\"content\":\"评论\", \"shopId\":109, \"productId\":1009}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/8/orders/101/products/1009/comment")
                        .header("authorization", customerToken2)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void testCreateCommentOrderStatusNotAllow() throws Exception {
        Order order = new Order();
        order.setId(101L);
        order.setShopId(108L);
        order.setStatus((byte) 0);
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1008L);
        orderItems.add(item);
        order.setOrderItems(orderItems);

        Mockito.when(orderMapper.getOrderById(101L)).thenReturn(new InternalReturnObject<>(order));

        String body = "{\"content\":\"评论\", \"shopId\":108, \"productId\":1008}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/8/orders/101/products/1008/comment")
                        .header("authorization", customerToken2)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COMMENT_CANNOT_CREATE.getErrNo())));
    }

    @Test
    public void testCreateCommentProductNotFound() throws Exception {
        Order order = new Order();
        order.setId(101L);
        order.setShopId(108L);
        order.setStatus(Order.FINISH);
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1001L);
        orderItems.add(item);
        order.setOrderItems(orderItems);

        Mockito.when(orderMapper.getOrderById(101L)).thenReturn(new InternalReturnObject<>(order));

        String body = "{\"content\":\"评论\", \"shopId\":108, \"productId\":99999}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/8/orders/101/products/99999/comment")
                        .header("authorization", customerToken2)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COMMENT_CANNOT_CREATE.getErrNo())));
    }

    @Test
    public void testCreateCommentWithCommented() throws Exception {
        Order order = new Order();
        order.setId(101L);
        order.setShopId(108L);
        order.setStatus(Order.FINISH);
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1008L);
        orderItems.add(item);
        order.setOrderItems(orderItems);

        Mockito.when(orderMapper.getOrderById(101L)).thenReturn(new InternalReturnObject<>(order));

        String body = "{\"content\":\"评论\", \"shopId\":108, \"productId\":1008}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/8/orders/101/products/1008/comment")
                        .header("authorization", customerToken2)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COMMENT_CANNOT_CREATE.getErrNo())));
    }

    @Test
    public void testCreateAdditionalComment() throws Exception {
        String body = "{\"content\":\"追评\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/8/comments/8/additional")
                .header("authorization", customerToken2)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void testCreateAdditionalCommentOutScope() throws Exception {
        String body = "{\"content\":\"追评\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/1/comments/3/additional")
                        .header("authorization", customerToken1)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COMMENT_OUTSCOPE.getErrNo())));
    }

    @Test
    public void testCreateAdditionalCommentOutLimit() throws Exception {
        String body = "{\"content\":\"追评\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/1/comments/1/additional")
                        .header("authorization", customerToken1)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.ADDITIONAL_COMMENT_OUTLIMIT.getErrNo())));
    }
}
