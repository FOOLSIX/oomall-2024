package cn.edu.xmu.oomall.order.controller;


import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.order.OrderApplicationTests;
import cn.edu.xmu.oomall.order.controller.dto.OrderUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest(classes = OrderApplicationTests.class)
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {
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
        shopToken = jwtHelper.createToken(3L,"shop1",0L,1,3600);
    }

    /**
     * 测试顾客更改订单
     *
     * @throws Exception
     */
    @Test
    public void testUpdateOrder() throws Exception {
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setAddress("UpdatedAddress");  // 设置需要更新的订单号

        this.mockMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", 1L)  // 替换为实际的订单 ID
                        .header("authorization", customerToken)  // 添加认证 token
                        .contentType(MediaType.APPLICATION_JSON_VALUE)  // 设置请求类型
                        .content(objectMapper.writeValueAsString(orderUpdateDto)))  // 将订单更新数据转为 JSON 字符串
                .andExpect(MockMvcResultMatchers.status().isOk())  // 断言返回的状态码为 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))  // 确保返回成功状态
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("成功"));  // 确保返回消息正确
    }

    /**
     * 测试顾客更改订单时传入空数据
     *
     * @throws Exception
     */
    @Test
    public void testUpdateOrderWhenNull() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", 2L)  // 替换为实际的订单 ID
                        .header("authorization", customerToken)  // 添加认证 token
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}"))  // 发送空的订单更新数据
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // 确保返回状态码为 400
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()))  // 检查错误码
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("地址字段不合法"));  // 检查错误信息
    }

    /**
     * 测试顾客更改订单时传入订单id不合法
     *
     * @throws Exception
     */
    @Test
    public void testUpdateOrderWhenIdIsNull() throws Exception {
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setAddress("UpdatedAddress");  // 设置需要更新的订单号

        this.mockMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", -1L)  // 替换为实际的订单 ID
                        .header("authorization", customerToken)  // 添加认证 token
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderUpdateDto)))  // 将订单更新数据转为 JSON 字符串
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // 确保返回状态码为 400
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()))  // 检查错误码
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("订单id字段不合法"));  // 检查错误信息
    }

    /**
     * 测试顾客取消订单
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/orders/{id}", 2L)  // 替换为实际的订单 ID
                        .header("authorization", customerToken)  // 添加认证 token
                        .contentType(MediaType.APPLICATION_JSON_VALUE))  // 设置请求类型
                .andExpect(MockMvcResultMatchers.status().isOk())  // 断言返回的状态码为 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))  // 确保返回成功状态
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("成功"));  // 确保返回消息正确
    }


    /**
     * 测试顾客取消订单时传入无效订单 ID
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderByIdWhenNull() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/orders/{id}", -1L)  // 使用无效的订单 ID
                        .header("authorization", customerToken)  // 添加认证 token
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // 确保返回状态码为 400
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()))  // 检查错误码
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("订单id字段不合法"));  // 检查错误信息
    }


}
