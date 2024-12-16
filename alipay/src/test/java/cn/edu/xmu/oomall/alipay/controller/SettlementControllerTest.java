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
class SettlementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String ORDER_SETTLE = "/internal/v3/alipay/trade/order/settle";
    private final String RELATION_BIND = "/internal/v3/alipay/trade/royalty/relation/bind";
    private final String RELATION_UNBIND = "/internal/v3/alipay/trade/royalty/relation/unbind";
    private String authorization = "app_id=oomall123456,app_cert_sn=testShop123456,nonce=nonce,timestamp=%s";

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Test
    void tesCreateDiv() throws Exception {
        CreateDivVo createDivVo = new CreateDivVo();
        createDivVo.setOutRequestNo("123456");
        createDivVo.setReceiverList(new ArrayList<>(){
            {
                add(new RoyaltyEntity(){
                    {
                        setAccount("alipayApp123456");
                        setType("userId");
                        setBindLogInName("alipayOwner");
                    }
                });
            }
        });
        this.mockMvc.perform(
                        post(RELATION_BIND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(createDivVo)))
                .andExpect(status().isOk());
    }

    @Test
    void tesCreateDivWhenReceiverListEmpty() throws Exception {
        CreateDivVo createDivVo = new CreateDivVo();
        createDivVo.setOutRequestNo("123456");
        this.mockMvc.perform(
                        post(RELATION_BIND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(createDivVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("分账收款方列表为空")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tesCreateDivWhenInvalidReceiverType() throws Exception {
        CreateDivVo createDivVo = new CreateDivVo();
        createDivVo.setOutRequestNo("123456");
        createDivVo.setReceiverList(new ArrayList<>(){
            {
                add(new RoyaltyEntity(){
                    {
                        setAccount("alipayApp123456");
                        setType("invalidType");
                        setBindLogInName("alipayOwner");
                    }
                });
            }
        });
        this.mockMvc.perform(
                        post(RELATION_BIND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(createDivVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("分账收款方类型参数非法")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tesCancelDiv() throws Exception {
        CancelDivVo cancelDivVo = new CancelDivVo();
        cancelDivVo.setOutRequestNo("123456");
        cancelDivVo.setReceiverList(new ArrayList<>(){
            {
                add(new RoyaltyEntity(){
                    {
                        setAccount("oomall123456");
                        setType("userId");
                    }
                });
            }
        });
        this.mockMvc.perform(
                        post(RELATION_UNBIND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(cancelDivVo)))
                .andExpect(status().isOk());
    }

    @Test
    void tesCancelDivWhenNoRelation() throws Exception {
        CancelDivVo cancelDivVo = new CancelDivVo();
        cancelDivVo.setOutRequestNo("1569189");
        cancelDivVo.setReceiverList(new ArrayList<>(){
            {
                add(new RoyaltyEntity(){
                    {
                        setAccount("oomall123456");
                        setType("userId");
                        setBindLogInName("oomallOwner");
                    }
                });
            }
        });
        this.mockMvc.perform(
                        post(RELATION_UNBIND)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(cancelDivVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("参数有误")))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void tesPostDivPay() throws Exception {
        PostDivPayVo postDivPayVo = PostDivPayVo.builder()
               .outRequestNo("div123456")
               .tradeNo("1187051287120949248")
               .royaltyParameters(new ArrayList<>(){
                   {
                     add(new OpenApiRoyaltyDetailInfoPojo(){
                         {
                             setAmount(1.0);
                             setTransIn("oomall123456");
                             setTransOut("testShop123456");
                         }
                     });
                 }
               }).build();
        this.mockMvc.perform(
                        post(ORDER_SETTLE)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postDivPayVo)))
                .andExpect(status().isOk());
    }

    @Test
    void tesPostDivPayWhenTradeNotExist() throws Exception {
        PostDivPayVo postDivPayVo = PostDivPayVo.builder()
                .outRequestNo("div123456")
                .tradeNo("1185684678980")
                .royaltyParameters(new ArrayList<>(){
                    {
                        add(new OpenApiRoyaltyDetailInfoPojo(){
                            {
                                setAmount(1.0);
                                setTransIn("oomall123456");
                                setTransOut("testShop123456");
                            }
                        });
                    }
                }).build();
        this.mockMvc.perform(
                        post(ORDER_SETTLE)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postDivPayVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("交易不存在")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tesPostDivPayWhenReceiverInvalid() throws Exception {
        PostDivPayVo postDivPayVo = PostDivPayVo.builder()
                .outRequestNo("div123456")
                .tradeNo("1187051287120949248")
                .royaltyParameters(new ArrayList<>(){
                    {
                        add(new OpenApiRoyaltyDetailInfoPojo(){
                            {
                                setAmount(1.0);
                                setTransIn("notBound123456");
                                setTransOut("testShop123456");
                            }
                        });
                    }
                }).build();
        this.mockMvc.perform(
                        post(ORDER_SETTLE)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postDivPayVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("收入方不在分账关系内")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void tesPostDivPayWhenOutOfAmount() throws Exception {
        PostDivPayVo postDivPayVo = PostDivPayVo.builder()
                .outRequestNo("div123456")
                .tradeNo("1187051287120949248")
                .royaltyParameters(new ArrayList<>(){
                    {
                        add(new OpenApiRoyaltyDetailInfoPojo(){
                            {
                                setAmount(999.0);
                                setTransIn("oomall123456");
                                setTransOut("testShop123456");
                            }
                        });
                    }
                }).build();
        this.mockMvc.perform(
                        post(ORDER_SETTLE)
                                .header("authorization", String.format(authorization, System.currentTimeMillis()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(postDivPayVo)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",is("分账金额超过最大可分账金额")))
                .andDo(MockMvcResultHandlers.print());
    }

}