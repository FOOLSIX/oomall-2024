package cn.edu.xmu.oomall.payment.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import cn.edu.xmu.oomall.payment.controller.dto.AlipayNotifyDto;
import cn.edu.xmu.oomall.payment.controller.dto.WepayNotifyDto;
import com.alibaba.nacos.common.http.param.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author ych
 * task 2023-dgn1-004
 */

@SpringBootTest(classes = PaymentApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RedisUtil redisUtil;
    private final String ALIPAY_NOTIFY_URL = "/notify/payments/alipay";
    private final String WEPAY_NOTIFY_URL = "/notify/payments/wepay";
    private final String CUSTOMER_GET_CHANNELS="/accounts";

    /**
     * task 2023-dgn1-004
     * @author ych
     * 错误参数
     */
    @Test
    public void testAlipayNotifyGivenWrongArg() throws Exception {
        String AliPayVoJson = "{}";

        Mockito.when(redisUtil.hasKey( Mockito.any())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ALIPAY_NOTIFY_URL)
                        .content(AliPayVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 状态为 TRADE_CLOSED
     */
    @Test
    public void testAlipayNotifyWhenStateIsTRADE_CLOSED() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime localDateTime = LocalDateTime.parse("2018-06-08T10:34:56+08:00", formatter);

        AlipayNotifyDto alipayNotifyDto = new AlipayNotifyDto();
        alipayNotifyDto.setAppId("20214072300007148");
        alipayNotifyDto.setTradeNo("2013112011001004330000121536");
        alipayNotifyDto.setOutTradeNo("6823789339978248");
        alipayNotifyDto.setGmtPayment(localDateTime);
        alipayNotifyDto.setTradeStatus("TRADE_CLOSED");
        alipayNotifyDto.setReceiptAmount(100L);

        String AliPayVoJson = objectMapper.writeValueAsString(alipayNotifyDto);

        Mockito.when(redisUtil.hasKey( Mockito.any())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ALIPAY_NOTIFY_URL)
                        .content(AliPayVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 状态为 TRADE_SUCCESS
     */
    @Test
    public void testAlipayNotifyWhenStateIsTRADE_SUCCESS() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime localDateTime = LocalDateTime.parse("2018-06-08T10:34:56+08:00", formatter);

        AlipayNotifyDto alipayNotifyDto = new AlipayNotifyDto();
        alipayNotifyDto.setAppId("20214072300007148");
        alipayNotifyDto.setTradeNo("2013112011001004330000121536");
        alipayNotifyDto.setOutTradeNo("6823789339978248");
        alipayNotifyDto.setGmtPayment(localDateTime);
        alipayNotifyDto.setTradeStatus("TRADE_SUCCESS");
        alipayNotifyDto.setReceiptAmount(100L);

        String AliPayVoJson = objectMapper.writeValueAsString(alipayNotifyDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ALIPAY_NOTIFY_URL)
                        .content(AliPayVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 其他状态
     */
    @Test
    public void testAlipayNotifyWhenStateIsOtherState() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime localDateTime = LocalDateTime.parse("2018-06-08T10:34:56+08:00", formatter);

        AlipayNotifyDto alipayNotifyDto = new AlipayNotifyDto();
        alipayNotifyDto.setAppId("20214072300007148");
        alipayNotifyDto.setTradeNo("2013112011001004330000121536");
        alipayNotifyDto.setOutTradeNo("6823789339978248");
        alipayNotifyDto.setGmtPayment(localDateTime);
        alipayNotifyDto.setTradeStatus("WAIT_BUYER_PAY");
        alipayNotifyDto.setReceiptAmount(100L);

        String AliPayVoJson = objectMapper.writeValueAsString(alipayNotifyDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ALIPAY_NOTIFY_URL)
                        .content(AliPayVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     */
    @Test
    public void testWepayNotifyGivenRightArgs() throws Exception {

        WepayNotifyDto wepayNotifyDto = new WepayNotifyDto();
        WepayNotifyDto.WePayResource wePayResource = wepayNotifyDto.new WePayResource();
        WepayNotifyDto.WePayResource.Amount amount = wePayResource.new Amount();
        WepayNotifyDto.WePayResource.Payer payer = wePayResource.new Payer();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime localDateTime = LocalDateTime.parse("2018-06-08T10:34:56+08:00", formatter);

        amount.setTotal(100L);
        amount.setPayerTotal(100L);
        payer.setSpOpenId("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");

        wePayResource.setSpAppId("wx8888888888888888");
        wePayResource.setSpMchId("1230000109");
        wePayResource.setSubMchId("1900000109");
        wePayResource.setOutTradeNo("1217752501201407033233368018");
        wePayResource.setTransactionId("1217752501201407033233368018");
        wePayResource.setTradeState("SUCCESS");
        wePayResource.setSuccessTime(localDateTime);
        wePayResource.setAmount(amount);
        wePayResource.setPayer(payer);

        wepayNotifyDto.setId("EV-2018022511223320873");
        wepayNotifyDto.setCreateTime("2015-05-20T13:29:35+08:00");
        wepayNotifyDto.setResource(wePayResource);

        String WePayVoJson = objectMapper.writeValueAsString(wepayNotifyDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(WEPAY_NOTIFY_URL)
                        .content(WePayVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 错误参数
     */
    @Test
    public void testWepayNotifyGivenWrongArgs() throws Exception {

        String wepayNotifyVo = "{}";

        String WePayVoJson = objectMapper.writeValueAsString(wepayNotifyVo);

        this.mockMvc.perform(MockMvcRequestBuilders.post(WEPAY_NOTIFY_URL)
                        .content(WePayVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()));
    }

    @Test
    void testGetAccountsGivenShopId1() throws Exception
    {
        this.mockMvc.perform((MockMvcRequestBuilders.get(CUSTOMER_GET_CHANNELS).
                        param("shopId", "1").
                        param("page","1").
                        param("pageSize","100")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(2)))
                .andExpect(jsonPath("$.data.list[?(@.id == '501')].channel.name", hasItem("微信支付")))
                .andExpect(jsonPath("$.data.list[?(@.id == '528')].channel.name", hasItem("支付宝")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testAccountsGivenShopId9() throws Exception
    {
        this.mockMvc.perform((MockMvcRequestBuilders.get(CUSTOMER_GET_CHANNELS).
                        param("shopId", "9").
                        param("page","1").
                        param("pageSize","100")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(jsonPath("$.data.list[?(@.id == '536')].channel.name", hasItem("支付宝")))
                .andDo(MockMvcResultHandlers.print());
    }
    /**
     * @Author 37220222203612
     */
    @Test
    void testGetRefundState() throws Exception
    {

        this.mockMvc.perform((MockMvcRequestBuilders.get("/refund/states"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andExpect(jsonPath("$.data[0].code", is(0)))
                .andExpect(jsonPath("$.data[0].name", is("待退款")))
                .andExpect(jsonPath("$.data[1].code", is(1)))
                .andExpect(jsonPath("$.data[1].name", is("已退款")))
                .andExpect(jsonPath("$.data[2].code", is(3)))
                .andExpect(jsonPath("$.data[2].name", is("错账")))
                .andExpect(jsonPath("$.data[3].code", is(4)))
                .andExpect(jsonPath("$.data[3].name", is("退款失败")));
    }

    /**
     * @Author 37220222203612
     */
    @Test
    void testGetPaymentState() throws Exception
    {

        this.mockMvc.perform((MockMvcRequestBuilders.get("/payments/states"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(jsonPath("$.data", hasSize(6)))
                .andExpect(jsonPath("$.data[0].code", is(0)))
                .andExpect(jsonPath("$.data[0].name", is("待支付")))
                .andExpect(jsonPath("$.data[1].code", is(1)))
                .andExpect(jsonPath("$.data[1].name", is("已支付")))
                .andExpect(jsonPath("$.data[2].code", is(3)))
                .andExpect(jsonPath("$.data[2].name", is("错账")))
                .andExpect(jsonPath("$.data[3].code", is(4)))
                .andExpect(jsonPath("$.data[3].name", is("支付失败")))
                .andExpect(jsonPath("$.data[4].code", is(5)))
                .andExpect(jsonPath("$.data[4].name", is("取消")))
                .andExpect(jsonPath("$.data[5].code", is(7)))
                .andExpect(jsonPath("$.data[5].name", is("分账")));
    }

    /**
     * @Author 37220222203612
     */
    @Test
    void testGetDivpayState() throws Exception
    {
        this.mockMvc.perform((MockMvcRequestBuilders.get("/divpay/states"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andExpect(jsonPath("$.data[0].code", is(0)))
                .andExpect(jsonPath("$.data[0].name", is("待支付")))
                .andExpect(jsonPath("$.data[1].code", is(1)))
                .andExpect(jsonPath("$.data[1].name", is("已支付")))
                .andExpect(jsonPath("$.data[2].code", is(3)))
                .andExpect(jsonPath("$.data[2].name", is("错账")))
                .andExpect(jsonPath("$.data[3].code", is(4)))
                .andExpect(jsonPath("$.data[3].name", is("支付失败")));
    }

    /**
     * @Author 37220222203612
     */
    @Test
    void testGetDivrefundState() throws Exception
    {
        this.mockMvc.perform((MockMvcRequestBuilders.get("/divrefund/states"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(jsonPath("$.data", hasSize(5)))
                .andExpect(jsonPath("$.data[0].code", is(0)))
                .andExpect(jsonPath("$.data[0].name", is("待支付")))
                .andExpect(jsonPath("$.data[1].code", is(1)))
                .andExpect(jsonPath("$.data[1].name", is("已支付")))
                .andExpect(jsonPath("$.data[2].code", is(3)))
                .andExpect(jsonPath("$.data[2].name", is("错账")))
                .andExpect(jsonPath("$.data[3].code", is(4)))
                .andExpect(jsonPath("$.data[3].name", is("支付失败")))
                .andExpect(jsonPath("$.data[4].code", is(5)))
                .andExpect(jsonPath("$.data[4].name", is("取消")));
    }


    /**
     * @Author 37720222205040
     */
    @Test
    public void testWepayupdatePayment() throws Exception {

        WepayNotifyDto wepayNotifyDto = new WepayNotifyDto();
        WepayNotifyDto.WePayResource wePayResource = wepayNotifyDto.new WePayResource();
        WepayNotifyDto.WePayResource.Amount amount = wePayResource.new Amount();
        WepayNotifyDto.WePayResource.Payer payer = wePayResource.new Payer();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        LocalDateTime localDateTime = LocalDateTime.parse("2018-06-08T10:34:56+08:00", formatter);

        amount.setTotal(100L);
        amount.setPayerTotal(100L);
        payer.setSpOpenId("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");

        wePayResource.setSpAppId("wx8888888888888888");
        wePayResource.setSpMchId("1230000109");
        wePayResource.setSubMchId("1900000109");
        wePayResource.setOutTradeNo("30");
        wePayResource.setTransactionId("1217752501201407033233368018");
        wePayResource.setTradeState("SUCCESS");
        wePayResource.setSuccessTime(localDateTime);
        wePayResource.setAmount(amount);
        wePayResource.setPayer(payer);

        wepayNotifyDto.setId("EV-2018022511223320873");
        wepayNotifyDto.setCreateTime("2015-05-20T13:29:35+08:00");
        wepayNotifyDto.setResource(wePayResource);

        String WePayVoJson = objectMapper.writeValueAsString(wepayNotifyDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(WEPAY_NOTIFY_URL)
                        .content(WePayVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }
}
