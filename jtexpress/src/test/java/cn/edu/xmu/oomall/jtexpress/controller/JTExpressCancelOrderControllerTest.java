package cn.edu.xmu.oomall.jtexpress.controller;

import cn.edu.xmu.oomall.jtexpress.controller.dto.CancelOrderDto;
import cn.edu.xmu.oomall.jtexpress.exception.ReturnError;
import cn.edu.xmu.oomall.jtexpress.jtexpressApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = jtexpressApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class JTExpressCancelOrderControllerTest {
    private final Logger logger = LoggerFactory.getLogger(JTExpressCancelOrderControllerTest.class);

    @Autowired
    private MockMvc mockMvc;
    private final String urlHeader = "/webopenplatformapi/api";
    private final String APIACCOUNT = "178337126125932605";
    private final String DIGEST = "JWI464gyo0JzuiAX9Zizdw==";
    private final String Cancel_Order = urlHeader + "/order/cancelOrder";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCancelOrderGivenUnCompleteOrder() throws Exception {
        CancelOrderDto cancelOrderDto = createCancelOrderVo();
        String bizContent = objectMapper.writeValueAsString(cancelOrderDto);
        logger.debug("{}测试 bizContent：{}", "testCancelOrderGivenUnCompleteOrder", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Cancel_Order)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.SUCCESS.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.billCode", is("JT108912787043904043991")));
    }

    @Test
    void testCancelOrderGivenCompleteOrder() throws Exception {
        CancelOrderDto cancelOrderDto = createCancelOrderVo();
        cancelOrderDto.setTxLogisticId("TEST1111111111");
        String bizContent = objectMapper.writeValueAsString(cancelOrderDto);
        logger.debug("{}测试 bizContent：{}", "testCancelOrderGivenCompleteOrder", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Cancel_Order)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.PICKED_STATUS_NOT_MODIFIABLE.getCode())));
    }

    @Test
    void testCancelOrderGivenCancelOrder() throws Exception {
        CancelOrderDto cancelOrderDto = createCancelOrderVo();
        cancelOrderDto.setTxLogisticId("TEST2222222222");
        String bizContent = objectMapper.writeValueAsString(cancelOrderDto);
        logger.debug("{}测试 bizContent：{}", "testCancelOrderGivenCancelOrder", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Cancel_Order)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.CANCELED_STATUS_NOT_MODIFIABLE.getCode())));
    }

    @Test
    void testCancelOrderGivenNoExistOrder() throws Exception {
        CancelOrderDto cancelOrderDto = createCancelOrderVo();
        cancelOrderDto.setTxLogisticId("TEST2222231232222");
        String bizContent = objectMapper.writeValueAsString(cancelOrderDto);
        logger.debug("{}测试 bizContent：{}", "testCancelOrderGivenNoExistOrder", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Cancel_Order)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.ILLEGAL_PARAMETER.getCode())));
    }

    @Test
    void testCancelOrderGivenInconsistentCustomer() throws Exception {
        CancelOrderDto cancelOrderDto = createCancelOrderVo();
        cancelOrderDto.setCustomerCode("J33331");
        String bizContent = objectMapper.writeValueAsString(cancelOrderDto);
        logger.debug("{}测试 bizContent：{}", "testCancelOrderGivenInconsistentCustomer", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Cancel_Order)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.ILLEGAL_PARAMETER.getCode())));
    }


    private CancelOrderDto createCancelOrderVo() {
        CancelOrderDto cancelOrderDto = new CancelOrderDto();
        cancelOrderDto.setCustomerCode("J0086474299");
        cancelOrderDto.setDigest("qonqb4O1eNr6VCWS07Ieeg==");
        cancelOrderDto.setTxLogisticId("TEST0000000000");
        cancelOrderDto.setOrderType("1");
        cancelOrderDto.setReason("产品订单取消");
        return cancelOrderDto;
    }

}

