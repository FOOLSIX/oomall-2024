package cn.edu.xmu.oomall.jtexpress.controller;


import cn.edu.xmu.oomall.jtexpress.controller.vo.OrderVo;
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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

@SpringBootTest(classes = jtexpressApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class JTExpressGetTracesControllerTest {

    private final Logger logger = LoggerFactory.getLogger(JTExpressGetTracesControllerTest.class);

    @Autowired
    private MockMvc mockMvc;
    private final String urlHeader = "/webopenplatformapi/api";
    private final String APIACCOUNT = "178337126125932605";
    private final String DIGEST = "JWI464gyo0JzuiAX9Zizdw==";
    private final String Get_Traces = urlHeader + "/logistics/trace";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetTracesGivenErrorBizContent() throws Exception {

        String bizContent = "{\"billCodes\":\"\"JT108912787043904043991,JT108912787043904043992\",\"JT1742686983284736054025\"\"}";
        logger.debug("{}测试 bizContent：{}", "testGetTracesGivenErrorBizContent", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Get_Traces)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.ILLEGAL_PARAMETER.getCode())));

    }

    @Test
    void testGetTracesGivenCorrectBillCode() throws Exception {
        Set<String> billCodesSet = new HashSet<>();
        billCodesSet.add("JT1742686983284736054025");
        billCodesSet.add("JT871345108962304099632");
        String bizContent = createBillCodes(billCodesSet);
        logger.debug("{}测试 bizContent：{}", "testGetTracesGivenCorrectBillCode", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Get_Traces)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.SUCCESS.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].details[0].billCode", is("JT1742686983284736054025")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].details[0].billCode", is("JT871345108962304099632")));

    }

    @Test
    void testGetTracesGivenExceed30BillCode() throws Exception {
        Set<String> billCodesSet = new HashSet<>();
        int count = 35;
        for (int i = 0; i < count; i++) {
            long randomNumber = 1000000000000L + (long) new Random().nextLong(8999999999999L);
            String orderCode = String.format("JT%s", randomNumber);
            billCodesSet.add(orderCode);
        }
        String bizContent = createBillCodes(billCodesSet);
        logger.debug("{}测试 bizContent：{}", "testGetTracesGivenExceed30BillCode", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Get_Traces)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.BILL_CODE_EXCEED_30.getCode())));

    }

    @Test
    void testGetTracesGivenCorrectBizContent() throws Exception {
        Set<String> billCodesSet = new HashSet<>();
        billCodesSet.add("JT1742686983284736054025");
        billCodesSet.add("JT871345108962304099632");
        String bizContent = createBillCodes(billCodesSet);
        logger.debug("{}测试 bizContent：{}", "testGetTracesGivenCorrectBizContent", bizContent);
        long timestamp = System.currentTimeMillis();
        this.mockMvc.perform(MockMvcRequestBuilders.post(Get_Traces)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .header("apiAccount", APIACCOUNT)
                        .header("digest", DIGEST)
                        .header("timestamp", timestamp)
                        .param("bizContent", bizContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(ReturnError.SUCCESS.getCode())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].details[0].billCode", is("JT1742686983284736054025")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].details[0].billCode", is("JT871345108962304099632")));

    }

    private String createBillCodes(Set<String> billCodesSet) {

        StringBuilder builder = new StringBuilder();
        for (String billCode : billCodesSet) {
            builder.append(billCode).append(",");
        }
        // 移除末尾的逗号，如果存在billCodes
        if (!builder.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return String.format("{\"billCodes\":\"%s\"}", builder.toString());
    }


}
