package cn.edu.xmu.oomall.alipay.controller;


import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.alipay.AliPayApplication;
import cn.edu.xmu.oomall.alipay.controller.vodto.*;

import cn.edu.xmu.oomall.alipay.service.bo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
class RefundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String TRADE_REFUND = "/internal/v3/alipay/trade/refund";
    private final String REFUND_QUERY = "/internal/v3/alipay/trade/fastpay/refund/query";
    private String authorization = "app_id=oomall123456,app_cert_sn=testShop123456,nonce=nonce,timestamp=%s";

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
    @Test
    void tesRefundWithoutDiv() throws Exception {
        PostRefundVo postRefundVo = new PostRefundVo();
        postRefundVo.setTradeNo("1187051287120949248");
        postRefundVo.setRefundAmount(99.0);
        postRefundVo.setOutRequestNo("out_request_no123");
        this.mockMvc.perform(
                        post(TRADE_REFUND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postRefundVo)))
                .andExpect(status().isOk());
    }

    @Test
    void tesRefundWithoutDivFail() throws Exception {
        PostRefundVo postRefundVo = new PostRefundVo();
        postRefundVo.setTradeNo("1187051287120949248");
        postRefundVo.setRefundAmount(9.01);
        postRefundVo.setOutRequestNo("out_request_no123");
        this.mockMvc.perform(
                        post(TRADE_REFUND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postRefundVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("系统错误")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tesRefundWithoutDivByOutTradeNo() throws Exception {
        PostRefundVo postRefundVo = new PostRefundVo();
        postRefundVo.setOutTradeNo("123456");
        postRefundVo.setRefundAmount(99.0);
        postRefundVo.setOutRequestNo("out_request_no123");
        this.mockMvc.perform(
                        post(TRADE_REFUND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postRefundVo)))
                .andExpect(status().isOk());
    }

    @Test
    void tesRefundWithoutDivWhenTradeNotExist() throws Exception {
        PostRefundVo postRefundVo = new PostRefundVo();
        postRefundVo.setTradeNo("51686216");
        postRefundVo.setRefundAmount(99.0);
        postRefundVo.setOutRequestNo("out_request_no123");
        this.mockMvc.perform(
                        post(TRADE_REFUND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postRefundVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("交易不存在")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tesRefundWithoutDivWhenOutOfAmount() throws Exception {
        PostRefundVo postRefundVo = new PostRefundVo();
        postRefundVo.setTradeNo("1187051287120949248");
        postRefundVo.setRefundAmount(1000000.0);
        postRefundVo.setOutRequestNo("out_request_no123");
        this.mockMvc.perform(
                        post(TRADE_REFUND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postRefundVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("退款金额超限")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tesRefundWithDiv() throws Exception {
        PostRefundVo postRefundVo = new PostRefundVo();
        postRefundVo.setOutTradeNo("123456");
        postRefundVo.setRefundAmount(0.0);
        postRefundVo.setOutRequestNo("out_request_no123");
        postRefundVo.setRefundRoyaltyParameters(new ArrayList<RoyaltyDetailInfoPojo>(){{
            add(RoyaltyDetailInfoPojo.builder().transIn("testShop123456").transOut("oomall123456").amount(1.0).build());
        }});
        this.mockMvc.perform(
                        post(TRADE_REFUND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postRefundVo)))
                .andExpect(status().isOk());
    }


    @Test
    void tesGetRefund() throws Exception {
        GetRefundParam getRefundParam = new GetRefundParam();
        getRefundParam.setOutTradeNo("123456");
        this.mockMvc.perform(
                        post(REFUND_QUERY)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(getRefundParam)))
                .andExpect(status().isOk());
    }

    @Test
    void tesGetRefundWhenTradeNotExist() throws Exception {
        GetRefundParam getRefundParam = new GetRefundParam();
        getRefundParam.setOutTradeNo("1234561896");
        this.mockMvc.perform(
                        post(REFUND_QUERY)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(getRefundParam)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("交易不存在")))
                .andDo(MockMvcResultHandlers.print());
    }
}