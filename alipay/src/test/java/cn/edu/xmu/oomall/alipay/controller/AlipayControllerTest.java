package cn.edu.xmu.oomall.alipay.controller;

import cn.edu.xmu.oomall.alipay.AliPayApplication;
import cn.edu.xmu.oomall.alipay.controller.vodto.*;

import cn.edu.xmu.oomall.alipay.openFeign.PaymentFeightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author LianShuQuan
 * @date 2023/12/17
 */
@SpringBootTest(classes = AliPayApplication.class)
@AutoConfigureMockMvc
@Transactional
class AlipayControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final String TRADE_WRAP_PAY = "/internal/v3/alipay/trade/wap/pay";
    private final String TRADE_CLOSE = "/internal/v3/alipay/trade/close";
    private final String TRADE_QUERY = "/internal/v3/alipay/trade/query";

    private String authorization = "app_id=oomall123456,app_cert_sn=testShop123456,nonce=nonce,timestamp=%s";

    @MockBean
    private PaymentFeightService paymentFeightService;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    //需要rocketMQ+OpenFeign，所以不进行单元测试
//    @Test
//    void tesPay() throws Exception {
//
////        when(this.rocketMQTemplate.sendOneWay(any(String.class), any(Message.class)))
////                .thenReturn(null);
//        when(this.paymentFeightService.notify(any())).thenReturn(null);
//
//        PostPayVo postPayVo = new PostPayVo();
//        postPayVo.setOutTradeNo("123456789");
//        postPayVo.setSubject("测试商品");
//        postPayVo.setTotalAmount(100.0);
//        postPayVo.setNotifyUrl("http://localhost:8080/api/v1/pay/notify");
//
////        String requestBody = "{\"out_trade_no\":\"123456\",\"subject\":\"测试商品\",\"total_amount\":100.0,\"notify_url\":\"http://localhost:8080/api/v1/pay/notify\"}";
////
////        System.out.println("requestBody:"+requestBody);
//
//        System.out.println("postPayVo:"+objectMapper.writeValueAsString(postPayVo));
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post(TRADE_WRAP_PAY)
//                        .header("authorization", String.format(authorization, System.currentTimeMillis()))
//                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(postPayVo)))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    void tesPayWhenAlreadyPaid() throws Exception {

//        when(this.rocketMQTemplate.sendOneWay(any(String.class), any(Message.class)))
//                .thenReturn(null);
        when(this.paymentFeightService.notify(any())).thenReturn(null);

        PostPayVo postPayVo = new PostPayVo();
        postPayVo.setOutTradeNo("123456");
        postPayVo.setSubject("测试商品");
        postPayVo.setTotalAmount(100.0);
        postPayVo.setNotifyUrl("http://localhost:8080/api/v1/pay/notify");

//        String requestBody = "{\"out_trade_no\":\"123456\",\"subject\":\"测试商品\",\"total_amount\":100.0,\"notify_url\":\"http://localhost:8080/api/v1/pay/notify\"}";
//
//        System.out.println("requestBody:"+requestBody);

        System.out.println("postPayVo:"+objectMapper.writeValueAsString(postPayVo));

        this.mockMvc.perform(MockMvcRequestBuilders.post(TRADE_WRAP_PAY)
                        .header("authorization", String.format(authorization, System.currentTimeMillis()))
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(postPayVo)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("交易已被支付")))
                .andDo(MockMvcResultHandlers.print());
    }


     //需要rocketMQ+OpenFeign，所以不进行单元测试
//    @Test
//    void tesPayWhenFailWithCallBack() throws Exception {
//        when(this.paymentFeightService.notify(any())).thenReturn(null);
//        PostPayVo postPayVo = new PostPayVo();
//        postPayVo.setOutTradeNo("123456789");
//        postPayVo.setSubject("测试商品");
//        postPayVo.setTotalAmount(99.01);
//        postPayVo.setNotifyUrl("http://localhost:8080/api/v1/pay/notify");
//        System.out.println("postPayVo:"+objectMapper.writeValueAsString(postPayVo));
//        this.mockMvc.perform(
//                        post(TRADE_WRAP_PAY)
//                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
//                                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                                .content(objectMapper.writeValueAsString(postPayVo)))
//                .andExpect(status().isOk());
//    }

    @Test
    void tesPayWhenFailWithoutCallBack() throws Exception {
        PostPayVo postPayVo = new PostPayVo();
        postPayVo.setOutTradeNo("123456789");
        postPayVo.setSubject("测试商品");
        postPayVo.setTotalAmount(99.02);
        postPayVo.setNotifyUrl("http://localhost:8080/api/v1/pay/notify");
        System.out.println("postPayVo:"+objectMapper.writeValueAsString(postPayVo));
        this.mockMvc.perform(
                        post(TRADE_WRAP_PAY)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postPayVo)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    // WAIT_BUYER_PAY才能取消订单
    @Test
    void testCancelOrder() throws Exception {
        CancelOrderVo cancelOrderVo = new CancelOrderVo();
        cancelOrderVo.setOutTradeNo("123457");
        System.out.println("cancelOrderVo:"+objectMapper.writeValueAsString(cancelOrderVo));
        this.mockMvc.perform(
                        post(TRADE_CLOSE)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(cancelOrderVo)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void testCancelOrderStateInvalid() throws Exception {
        CancelOrderVo cancelOrderVo = new CancelOrderVo();
        cancelOrderVo.setOutTradeNo("123456");
        System.out.println("cancelOrderVo:"+objectMapper.writeValueAsString(cancelOrderVo));
        this.mockMvc.perform(
                        post(TRADE_CLOSE)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(cancelOrderVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("交易状态不合法")))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    void testCancelOrderWhenPaymentNotExist() throws Exception {
        CancelOrderVo cancelOrderVo = new CancelOrderVo();
            cancelOrderVo.setTradeNo("118568467898");
        System.out.println("cancelOrderVo:"+objectMapper.writeValueAsString(cancelOrderVo));
        this.mockMvc.perform(
                        post(TRADE_CLOSE)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(cancelOrderVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("交易不存在")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testRetrieveOrderByTradeNo() throws Exception {
        GetTransVo getTransVo = new GetTransVo();
        getTransVo.setTradeNo("1187051287120949248");
        this.mockMvc.perform(
                        post(TRADE_QUERY)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(getTransVo)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testRetrieveOrderByOutTradeNo() throws Exception {
        GetTransVo getTransVo = new GetTransVo();
        getTransVo.setOutTradeNo("123456");
        this.mockMvc.perform(
                        post(TRADE_QUERY)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(getTransVo)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testRetrieveOrderWhenNotExist() throws Exception {
        GetTransVo getTransVo = new GetTransVo();
        getTransVo.setOutTradeNo("12345632165");
        this.mockMvc.perform(
                        post(TRADE_QUERY)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(getTransVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("交易不存在")))
                .andDo(MockMvcResultHandlers.print());
    }

}