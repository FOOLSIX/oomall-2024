package cn.edu.xmu.oomall.sfexpress.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.sfexpress.SfExpressApplication;
import cn.edu.xmu.oomall.sfexpress.controller.dto.*;
import cn.edu.xmu.oomall.sfexpress.controller.vo.*;
import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import cn.edu.xmu.oomall.sfexpress.utils.IdWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = SfExpressApplication.class)
@AutoConfigureMockMvc
@Transactional
public class SfexpressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final IdWorker idWorker = new IdWorker(1L, 1L);

    private static String adminToken;

    private final String SFURL = "/internal/sf/";

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    void createExpressTestWhenSuccess() throws Exception {
        Long id = idWorker.nextId();
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"" + id.toString() + "\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                "{\"contact\": \"发件人\",\"contactType\": \"1\",\"mobile\": \"13812345678\",\"address\": \"发件人地址\", \"city\": \"769\"}," +
                "{\"contact\": \"收件人\",\"contactType\": \"2\",\"mobile\": \"13987654321\",\"address\": \"收件人地址\", \"city\": \"20\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/sf/")
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1000"));
    }

    @Test
    void createExpressTestWhenOrderIdRepeatThenFailed() throws Exception {
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"20231211001\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                "{\"contact\": \"发件人\",\"contactType\": \"1\",\"mobile\": \"13812345678\",\"address\": \"发件人地址\"}," +
                "{\"contact\": \"收件人\",\"contactType\": \"2\",\"mobile\": \"13987654321\",\"address\": \"收件人地址\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E8016.getErrorCodeString()));
    }


    /**
     * @Author 37220222203612
     */
    @Test
    void createExpressTestWhenContactInfoListSizeIllegal() throws Exception {
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"20231211001\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                //"{\"contact\": \"发件人\",\"contactType\": \"1\",\"mobile\": \"13812345678\",\"address\": \"发件人地址\"}," +
                "{\"contact\": \"收件人\",\"contactType\": \"2\",\"mobile\": \"13987654321\",\"address\": \"收件人地址\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E1010.getErrorCodeString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorMsg").value(SFErrorCodeEnum.E1010.getErrorDescAndAdvice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiErrorMsg").value("发件人和收件人信息不能为空"));
    }



    /**
     * @Author 37220222203612
     */
        @Test
    void createExpressTestWhenContactType1AddrBlank() throws Exception {
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"20231211001\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                "{\"contact\": \"发件人\",\"contactType\": \"1\",\"mobile\": \"13812345678\",\"address\": \"\"}," +
                "{\"contact\": \"收件人\",\"contactType\": \"2\",\"mobile\": \"13987654321\",\"address\": \"收件人地址\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiErrorMsg").value("发件人地址不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E1010.getErrorCodeString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorMsg").value(SFErrorCodeEnum.E1010.getErrorDescAndAdvice()));
    }



    /**
     * @Author 37220222203612
     */
    @Test
    void createExpressTestWhenContactType1ContactBlank() throws Exception {
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"20231211001\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                "{\"contact\": \"\",\"contactType\": \"1\",\"mobile\": \"13812345678\",\"address\": \"发件人地址\"}," +
                "{\"contact\": \"收件人\",\"contactType\": \"2\",\"mobile\": \"13987654321\",\"address\": \"收件人地址\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiErrorMsg").value("发件联系人不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E1011.getErrorCodeString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorMsg").value(SFErrorCodeEnum.E1011.getErrorDescAndAdvice()));
    }



    /**
     * @Author 37220222203612
     */
    @Test
    void createExpressTestWhenContactType1MobileBlank() throws Exception {
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"20231211001\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                "{\"contact\": \"发件人\",\"contactType\": \"1\",\"mobile\": \"\",\"address\": \"发件人地址\"}," +
                "{\"contact\": \"收件人\",\"contactType\": \"2\",\"mobile\": \"13987654321\",\"address\": \"收件人地址\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiErrorMsg").value("发件人电话不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E1012.getErrorCodeString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorMsg").value(SFErrorCodeEnum.E1012.getErrorDescAndAdvice()));
    }



    /**
     * @Author 37220222203612
     */
    @Test
    void createExpressTestWhenContactType2AddrBlank() throws Exception {
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"20231211001\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                "{\"contact\": \"发件人\",\"contactType\": \"1\",\"mobile\": \"13812345678\",\"address\": \"发件人地址\"}," +
                "{\"contact\": \"收件人\",\"contactType\": \"2\",\"mobile\": \"13987654321\",\"address\": \"\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiErrorMsg").value("收件人地址不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E1014.getErrorCodeString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorMsg").value(SFErrorCodeEnum.E1014.getErrorDescAndAdvice()));
    }



    /**
     * @Author 37220222203612
     */
    @Test
    void createExpressTestWhenContactType2ContractBlank() throws Exception {
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"20231211001\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                "{\"contact\": \"发件人\",\"contactType\": \"1\",\"mobile\": \"13812345678\",\"address\": \"发件人地址\"}," +
                "{\"contact\": \"\",\"contactType\": \"2\",\"mobile\": \"13987654321\",\"address\": \"收件人地址\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiErrorMsg").value("收件联系人不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E1015.getErrorCodeString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorMsg").value(SFErrorCodeEnum.E1015.getErrorDescAndAdvice()));
    }



    /**
     * @Author 37220222203612
     */
    @Test
    void createExpressTestWhenContactType2MobileBlank() throws Exception {
        String body = "{" +
                "\"partnerID\": \"合作伙伴编码\"," +
                "\"requestID\": \"请求唯一号UUID\"," +
                "\"serviceCode\": \"EXP_RECE_CREATE_ORDER\"," +
                "\"timestamp\": \"调用接口时间戳\"," +
                "\"msgDigest\": \"数字签名\"," +
                "\"msgData\": {" +
                "\"language\": \"zh-CN\"," +
                "\"orderId\": \"20231211001\"," +
                "\"cargoDetails\": [" +
                "{\"name\": \"商品1\"}," +
                "{\"name\": \"商品2\"}" +
                "]," +
                "\"cargoDesc\": \"电子产品\"," +
                "\"contactInfoList\": [" +
                "{\"contact\": \"发件人\",\"contactType\": \"1\",\"mobile\": \"13812345678\",\"address\": \"发件人地址\"}," +
                "{\"contact\": \"收件人\",\"contactType\": \"2\",\"mobile\": \"\",\"address\": \"收件人地址\"}" +
                "]," +
                "\"monthlyCard\": \"SF123456789\"," +
                "\"payMethod\": 1," +
                "\"expressTypeId\": 123" +
                "}" +
                "}";

        System.out.println(mockMvc.getClass());

        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
//                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiErrorMsg").value("收件人电话不能为空"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E1016.getErrorCodeString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorMsg").value(SFErrorCodeEnum.E1016.getErrorDescAndAdvice()));
    }
    @Test
    void searchOrderWhenSuccess() throws Exception {
        PostSearchOrderDTO PostSearchOrderDTO = new PostSearchOrderDTO();
        PostSearchOrderDTO.setOrderId("20231211001");
        SFPostRequestDTO<PostSearchOrderDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostSearchOrderDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_SEARCH_ORDER_RESP");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value("S0000"));
    }

    @Test
    void searchOrderWhenOrderIdNotExist() throws Exception {
        PostSearchOrderDTO PostSearchOrderDTO = new PostSearchOrderDTO();
        PostSearchOrderDTO.setOrderId("11111111");
        SFPostRequestDTO<PostSearchOrderDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostSearchOrderDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_SEARCH_ORDER_RESP");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E8024.getErrorCodeString()));
    }

    @Test
    void searchRouteWhenUseMailNo() throws Exception {
        PostSearchRoutesDTO PostSearchRoutesDTO = new PostSearchRoutesDTO();
        PostSearchRoutesDTO.setTrackingType(1);
        String mailNo = "SF1183896766740172800";
        List<String> trackingNumberList = new ArrayList<>();
        trackingNumberList.add(mailNo);
        PostSearchRoutesDTO.setTrackingNumber(trackingNumberList);
        SFPostRequestDTO<PostSearchRoutesDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostSearchRoutesDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_SEARCH_ROUTES");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value("S0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.msgData.routeResps[0].mailNo").value("SF1183896766740172800"));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.msgData.routeResps[0].routes.length").value(3));
    }

    @Test
    void searchRouteWhenUseOrderId() throws Exception {
        PostSearchRoutesDTO PostSearchRoutesDTO = new PostSearchRoutesDTO();
        PostSearchRoutesDTO.setTrackingType(2);
        String orderId = "1183896766073278464";
        List<String> trackingNumberList = new ArrayList<>();
        trackingNumberList.add(orderId);
        PostSearchRoutesDTO.setTrackingNumber(trackingNumberList);
        SFPostRequestDTO<PostSearchRoutesDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostSearchRoutesDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_SEARCH_ROUTES");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value("S0000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.msgData.routeResps[0].mailNo").value("SF1183896766740172800"));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.msgData.routeResps[0].routes.length").value(3));
    }

    @Test
    void searchRouteWhenMailNoNotExist() throws Exception {
        PostSearchRoutesDTO PostSearchRoutesDTO = new PostSearchRoutesDTO();
        PostSearchRoutesDTO.setTrackingType(1);
        String mailNo = "1231231231";
        List<String> trackingNumberList = new ArrayList<>();
        trackingNumberList.add(mailNo);
        PostSearchRoutesDTO.setTrackingNumber(trackingNumberList);
        SFPostRequestDTO<PostSearchRoutesDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostSearchRoutesDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_SEARCH_ROUTES");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorMsg").value(SFErrorCodeEnum.E8024.getErrorDescAndAdvice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.msgData.routeResps[0].routes.length").value(3));
    }

    @Test
    void updateExpressTest() throws Exception {
        PostUpdateOrderDTO PostUpdateOrderDTO = new PostUpdateOrderDTO();
        PostUpdateOrderDTO.setDealType(1);
        PostUpdateOrderDTO.setOrderId("1183896766073278464");
        PostUpdateOrderDTO.setTotalHeight(10.000);
        PostUpdateOrderDTO.setTotalLength(10.000);
        PostUpdateOrderDTO.setTotalVolume(10.000);
        PostUpdateOrderDTO.setTotalWeight(10.000);
        PostUpdateOrderDTO.setTotalWidth(10.000);
        PostUpdateOrderDTO.setSendStartTm(new Timestamp((new Date()).getTime()));
        PostUpdateOrderDTO.setPickupAppointEndtime(new Timestamp((new Date()).getTime()));
        SFPostRequestDTO<PostUpdateOrderDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostUpdateOrderDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_UPDATE_ORDER");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.msgData.orderId").value("1183896766073278464"));
    }

    @Test
    void updateExpressTestWhenOrderHasBeanCanceled() throws Exception {
        PostUpdateOrderDTO PostUpdateOrderDTO = new PostUpdateOrderDTO();
        PostUpdateOrderDTO.setDealType(1);
        PostUpdateOrderDTO.setOrderId("1183895391419502592");
        PostUpdateOrderDTO.setTotalHeight(10.000);
        PostUpdateOrderDTO.setTotalLength(10.000);
        PostUpdateOrderDTO.setTotalVolume(10.000);
        PostUpdateOrderDTO.setTotalWeight(10.000);
        PostUpdateOrderDTO.setTotalWidth(10.000);
        PostUpdateOrderDTO.setSendStartTm(new Timestamp((new Date()).getTime()));
        PostUpdateOrderDTO.setPickupAppointEndtime(new Timestamp((new Date()).getTime()));
        SFPostRequestDTO<PostUpdateOrderDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostUpdateOrderDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_UPDATE_ORDER");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E8037.getErrorCodeString()));
    }

    @Test
    void updateExpressTestWhenOrderHasBeanChecked() throws Exception {
        PostUpdateOrderDTO PostUpdateOrderDTO = new PostUpdateOrderDTO();
        PostUpdateOrderDTO.setDealType(1);
        PostUpdateOrderDTO.setOrderId("1183893848351838208");
        PostUpdateOrderDTO.setTotalHeight(10.000);
        PostUpdateOrderDTO.setTotalLength(10.000);
        PostUpdateOrderDTO.setTotalVolume(10.000);
        PostUpdateOrderDTO.setTotalWeight(10.000);
        PostUpdateOrderDTO.setTotalWidth(10.000);
        PostUpdateOrderDTO.setSendStartTm(new Timestamp((new Date()).getTime()));
        PostUpdateOrderDTO.setPickupAppointEndtime(new Timestamp((new Date()).getTime()));
        SFPostRequestDTO<PostUpdateOrderDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostUpdateOrderDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_UPDATE_ORDER");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E8252.getErrorCodeString()));
    }

    @Test
    void cancelExpressTest() throws Exception {
        PostUpdateOrderDTO PostUpdateOrderDTO = new PostUpdateOrderDTO();
        PostUpdateOrderDTO.setDealType(2);
        PostUpdateOrderDTO.setOrderId("1183896766073278464");
        SFPostRequestDTO<PostUpdateOrderDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostUpdateOrderDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_UPDATE_ORDER");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.msgData.resStatus").value(2));
    }

    @Test
    void cancelExpressWhenHasBeanChecked() throws Exception {
        PostUpdateOrderDTO PostUpdateOrderDTO = new PostUpdateOrderDTO();
        PostUpdateOrderDTO.setDealType(2);
        PostUpdateOrderDTO.setOrderId("1183893848351838208");
        SFPostRequestDTO<PostUpdateOrderDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostUpdateOrderDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_UPDATE_ORDER");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E8252.getErrorCodeString()));
    }

    @Test
    void cancelExpressWhenHasBeanCanceled() throws Exception {
        PostUpdateOrderDTO PostUpdateOrderDTO = new PostUpdateOrderDTO();
        PostUpdateOrderDTO.setDealType(2);
        PostUpdateOrderDTO.setOrderId("1183895391419502592");
        SFPostRequestDTO<PostUpdateOrderDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(PostUpdateOrderDTO);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("EXP_RECE_UPDATE_ORDER");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.errorCode").value(SFErrorCodeEnum.E8037.getErrorCodeString()));
    }

    @Test
    void printWaybills() throws Exception {
        PostPrintWaybillsDTO printWaybillsVo = new PostPrintWaybillsDTO();
        PostPrintWaybillsDTO.DocumentsDTO documentsDTO = new PostPrintWaybillsDTO.DocumentsDTO();
        documentsDTO.setMasterWaybillNo("SF1183896766740172800");
        List<PostPrintWaybillsDTO.DocumentsDTO> documentsDTOList = new ArrayList<>();
        documentsDTOList.add(documentsDTO);
        printWaybillsVo.setDocuments(documentsDTOList);
        SFPostRequestDTO<PostPrintWaybillsDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(printWaybillsVo);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("COM_RECE_CLOUD_PRINT_WAYBILLS");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultData.success").value(true));
    }

    /**
     * @Author 37220222203612
     */
    @Test
    void ServiceCodeWrong() throws Exception {
        PostPrintWaybillsDTO printWaybillsVo = new PostPrintWaybillsDTO();
        PostPrintWaybillsDTO.DocumentsDTO documentsDTO = new PostPrintWaybillsDTO.DocumentsDTO();
        documentsDTO.setMasterWaybillNo("SF1183896766740172800");
        List<PostPrintWaybillsDTO.DocumentsDTO> documentsDTOList = new ArrayList<>();
        documentsDTOList.add(documentsDTO);
        printWaybillsVo.setDocuments(documentsDTOList);
        SFPostRequestDTO<PostPrintWaybillsDTO> SFPostRequestDTO = new SFPostRequestDTO<>();
        SFPostRequestDTO.setMsgData(printWaybillsVo);
        SFPostRequestDTO.setRequestID(idWorker.nextId().toString());
        SFPostRequestDTO.setServiceCode("DEFAULT");
        SFPostRequestDTO.setTimestamp(String.valueOf(new Timestamp((new Date()).getTime())));
        String body = objectMapper.writeValueAsString(SFPostRequestDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post(SFURL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiResultCode").value("A1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apiErrorMsg").value("服务代码不存在"));
    }
}
