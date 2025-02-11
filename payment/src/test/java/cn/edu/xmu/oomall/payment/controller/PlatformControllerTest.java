package cn.edu.xmu.oomall.payment.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import cn.edu.xmu.oomall.payment.dao.ChannelDao;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import cn.edu.xmu.oomall.payment.dao.bo.RefundTrans;
import cn.edu.xmu.oomall.payment.mapper.generator.PayTransPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.RefundTransPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.po.PayTransPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPo;
import cn.edu.xmu.oomall.payment.mapper.openfeign.AliPayMapper;
import cn.edu.xmu.oomall.payment.mapper.openfeign.WePayMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ych
 * task 2023-dgn1-004
 */

@SpringBootTest(classes = PaymentApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class PlatformControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AliPayMapper aliPayMapper;
    @SpyBean
    private PayTransPoMapper payTransPoMapper;
    @SpyBean
    private RefundTransPoMapper refundTransPoMapper;
    @MockBean
    private WePayMapper wePayMapper;
    private static String adminToken;
    private static String shopToken;
    private static String shop1Token;
    private static String shop2Token;
    private static String shop8Token;
    private static String shop73Token;
    private final String ADMIN_VALID_CHANNELS="/platforms/{shopId}/channels/{id}/valid";
    private final String ADMIN_INVALID_CHANNELS="/platforms/{shopId}/channels/{id}/invalid";
    private final String ADMIN_GET_ALL_ACCOUNTS ="/shops/{shopId}/accounts";
    private final String ADMIN_GET_ACCOUNTS ="/shops/{shopId}/accounts/{id}";
    private final String ADMIN_ADD_ACCOUNTS ="/shops/{shopId}/channels/{id}/accounts";
    private final String ADMIN_DEL_ACCOUNTS ="/shops/{shopId}/accounts/{id}";
    private final String SHOP_VALID_CHANNELS="/shops/{shopId}/accounts/{id}/valid";
    private final String SHOP_INVALID_CHANNELS ="/shops/{shopId}/accounts/{id}/invalid";
    private final String ADMIN_RETRIEVE_PAYMENT = "/shops/{shopId}/channels/{id}/payments";
    private final String ADMIN_GET_PAYMENT = "/shops/{shopId}/payments/{id}";
    private final String ADMIN_ADJUST_PAYMENT = "/shops/{shopId}/payments/{id}";
    private final String ADMIN_REFUNDS_ID = "/shops/{shopId}/refunds/{id}";
    private final String ADMIN_CHANNELS_ID_REFUNDS = "/shops/{shopId}/channels/{id}/refunds";

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
        shopToken = jwtHelper.createToken(1L, "shop1", 1L, 1, 3600);
        shop8Token = jwtHelper.createToken(14L, "shop8", 8L, 1, 3600);
        shop1Token = jwtHelper.createToken(1L, "shop1", 1L, 1, 3600);
        shop2Token = jwtHelper.createToken(1L, "shop2", 2L, 1, 3600);
        shop73Token = jwtHelper.createToken(1L, "shop72", 73L, 1, 3600);
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 管理员查
     */
    @Test
    public void retrievePaymentsWhenUserIsAdmin() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_RETRIEVE_PAYMENT+"?beginTime=2022-11-05T12:30:45", 0, 501)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.list").isNotEmpty());
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * shop查
     */
    @Test
    public void testRetrievePaymentsWhenUserIsShop() throws Exception {

        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_RETRIEVE_PAYMENT+"?beginTime=2022-11-05T12:30:45", 1, 501)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.list").isNotEmpty());
    }

    /**
     * 不存在该渠道
     * @author ych
     * @throws Exception
     */
    @Test
    public void testRetrievePayments1() throws Exception {

        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_RETRIEVE_PAYMENT+"?beginTime=2022-11-05T12:30:45", 0, 500)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    /**
     * 没有该商铺渠道
     * @throws Exception
     */
    @Test
    public void testRetrievePayments2() throws Exception {

        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_RETRIEVE_PAYMENT+"?beginTime=2022-11-05T12:30:45", 2, 501)
                        .header("authorization", shop2Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 类型不是新建的
     */
    @Test
    public void testGetPaymentWhenStateNotEqualsNew()throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_PAYMENT, 0, 551)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.amount").value(100L));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 状态为0（新建） TransNo空 （微信）
     */
    @Test
    public void testGetPaymentWhenStateEqualsNewAndTransNoIsNull()throws Exception{
        String returnStr = "{\"sp_appid\": \"wxdace645e0bc2cXXX\","
                + "\"sp_mchid\": \"1900007XXX\","
                +"\"sub_mchid\": \"1900008XXX\","
                +"\"out_trade_no\": \"b3682ea011c547a49e8d7cc93107b71c\","
                +"\"transaction_id\": \"4200000985202103031441826014\","
                +"\"trade_type\": \"JSAPI\","
                +"\"trade_state\": \"SUCCESS\","
                +"\"trade_state_desc\": \"支付成功\","
                +"\"success_time\": \"2021-03-03T15:27:14+08:00\","
                +"\"transRetPayer\": {\"sp_openid\":\"o4GgauMQHaUO8ujCGIXNKATQlXXX\"},"
                +"\"transRetAmount\":{\"total\":\"2\",\"payer_total\":\"2\"}" +
                "}";
        Mockito.when(wePayMapper.getOrderByOutNo(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(returnStr);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_PAYMENT, 0, 860)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.amount").value(5308L));
    }

    /**
     * @author 37720222205040
     */
    @Test
    public void testGetPaymentWhenCheckTransNoIsNull()throws Exception{
        PayTransPo payTransPo=new PayTransPo();
        payTransPo.setId(860L);
        payTransPo.setCreatorId(1L);
        payTransPo.setCreatorName("admin111");
        payTransPo.setOutNo("2");
        payTransPo.setAmount(5308L);
        payTransPo.setStatus(PayTrans.SUCCESS);
        payTransPo.setShopId(1L);
        payTransPo.setAccountId(501L);
        payTransPo.setDivAmount(53L);
        payTransPo.setInRefund((byte) 0);
        String returnStr = "{\"sp_appid\": \"wxdace645e0bc2cXXX\","
                + "\"sp_mchid\": \"1900007XXX\","
                +"\"sub_mchid\": \"1900008XXX\","
                +"\"out_trade_no\": \"b3682ea011c547a49e8d7cc93107b71c\","
                +"\"transaction_id\": \"4200000985202103031441826014\","
                +"\"trade_type\": \"JSAPI\","
                +"\"trade_state\": \"SUCCESS\","
                +"\"trade_state_desc\": \"支付成功\","
                +"\"success_time\": \"2021-03-03T15:27:14+08:00\","
                +"\"transRetPayer\": {\"sp_openid\":\"o4GgauMQHaUO8ujCGIXNKATQlXXX\"},"
                +"\"transRetAmount\":{\"total\":\"2\",\"payer_total\":\"2\"}" +
                "}";
        doReturn(payTransPo).when(payTransPoMapper).selectByPrimaryKey(860L);
        Mockito.when(wePayMapper.getOrderByOutNo(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(returnStr);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_PAYMENT, 0, 860)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.amount").value(5308L));
    }
    /**
     * task 2023-dgn1-004
     * @author ych
     * 状态为0（新建） TransNo不空 （支付宝）
     */
    @Test
    public void testGetPaymentWhenStateEqualsNewAndTransNoIsNotNull()throws Exception{
        String returnStr = "{\"trade_no\": \"2013112011001004330000121536\","
                + "\"out_trade_no\": \"6823789339978248\","
                +"\"buyer_logon_id\": \"159****5620\","
                +"\"trade_status\": \"TRADE_CLOSED\","
                +"\"total_amount\": \"88\"}";

        Mockito.when(aliPayMapper.retrieveOrder(Mockito.anyString(), Mockito.any())).thenReturn(returnStr);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_PAYMENT, 0, 862)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.amount").value(25944L));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 只有平台管理员可调账
     */
    @Test
    public void testAdjustPaymentWhenUserIsNotAdmin()throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_ADJUST_PAYMENT, 1, 551)
                        .header("authorization", shopToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 只有错账可调账
     */
    @Test
    public void testAdjustPaymentWhenStateIsNotWrong()throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_ADJUST_PAYMENT, 0, 551)
                        .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     */
    @Test
    public void testAdjustPaymentGivenRightArgs()throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_ADJUST_PAYMENT, 0, 1058)
                        .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }


    /**
     * 02  查询退款单
     * 快乐路径-管理员-查询列表
     */
    @Test
    void testRetrieveRefundsGivenManagerTokenWithoutTranNo() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_CHANNELS_ID_REFUNDS, 0, 501)
                        .header("authorization", adminToken)
                        .param("status", "0")
                        .param("beginTime", "2022-11-05T12:30:45")
                        .param("endTime","2022-11-30T12:30:45")
                        .param("page", "1")
                        .param("pageSize", "100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '501')].outNo", hasItem("501")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '597')].outNo", hasItem("597")));
    }


    /**
     * 02  查询退款单
     * 快乐路径-管理员-查询指定退款单
     */
    @Test
    void testRetrieveRefundsGivenManagerTokenWithTranNo() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_CHANNELS_ID_REFUNDS, 0, 502)
                        .header("authorization",  adminToken)
                        .param("transNo","3502")
                        .param("beginTime", "2022-11-05T12:30:45")
                        .param("status","1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1758')].outNo", hasItem("3502")));
    }

    /**
     * 02  查询退款单
     * 快乐路径-商户-查询列表
     */
    @Test
    void testRetrieveRefundsGivenShopTokenWithoutTransNo() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_CHANNELS_ID_REFUNDS, 1, 501)
                        .header("authorization", shopToken)
                        .param("status","0")
                        .param("beginTime", "2022-11-05T12:30:45")
                        .param("endTime","2023-11-30T12:30:45")
                        .param("page","1")
                        .param("pageSize","100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '501')].outNo", hasItem("501")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '597')].outNo", hasItem("597")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '598')].outNo", hasItem("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '599')].outNo", hasItem("4")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '611')].outNo", hasItem("62")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '613')].outNo", hasItem("134")));
    }

    /**
     * 02  查询退款单
     * 快乐路径-商户-查询指定退款单
     */
    @Test
    void testRetrieveRefundsGivenShopTokenWithTransNo() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_CHANNELS_ID_REFUNDS, 1, 502)
                        .header("authorization", shopToken)
                        .param("transNo","3533")
                        .param("status","1")
                        .param("beginTime", "2022-11-05T12:30:45")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1766')].outNo", hasItem("3533")));
    }

    /**
     * 02  查询退款单
     * 意外路径-商户-错误的支付渠道
     */
    @Test
    void testRetrieveRefundsGivenShopTokenWithWrongChannelId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_CHANNELS_ID_REFUNDS, 1, 86000)
                        .header("authorization", shop1Token)
                        .param("transNo","3533")
                        .param("status","1")
                        .param("beginTime", "2022-11-05T12:30:45")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.PAY_CHANNEL_INVALID.getErrNo())));
    }

    /**
     * 03 调账
     * 快乐路径
     */
    @Test
    void testJustRefund() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REFUNDS_ID, 0, 617)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * 03 调账
     * 意外路径: 退款单状态不为”错账“
     */
    @Test
    void testJustRefundGivenWrongStatus() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REFUNDS_ID, 0, 1761)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("退款交易对象（id=1761）已退款状态禁止此操作")));

    }

    /**
     * 03 调账
     * 意外路径: 非管理员访问
     */
    @Test
    void testJustRefundGivenWrongToken() throws Exception {
         this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REFUNDS_ID, 1, 1761)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("退款单对象(id=1761)超出商铺（id = 1）的操作范围")));

    }
    /**
     * 04 获取已经完成的退款单
     * 快乐路径
     * 修正用例,为mock的AliPayMapper添加返回值
     * @Author 37220222203851
     */
    @Test
    void testGetRefundGivenStatusSuccess() throws Exception {
        when(redisUtil.set(eq("SC501"), any(), eq(3600))).thenReturn(true);
        when(redisUtil.set(eq("SC502"), any(), eq(3600))).thenReturn(true);
        when(aliPayMapper.getRefund(any(), any())).thenReturn("{\"trade_no\":\"134\",\"out_trade_no\":\"3518\",\"out_request_no\":\"3518\",\"total_amount\":216312,\"refund_amount\":216310,\"refund_status\":\"REFUND_SUCCESS\",\"refund_royaltys\":[{\"refund_amount\":216310,\"result_code\":\"SUCCESS\"}]}");
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_REFUNDS_ID, 0, 1761)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.outNo", is("3518")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", is(216312)));

    }

    /**
     * @Author 37720222205040
     */
    @Test
    void testGetRefundWhenCheckOutNoIsNull() throws Exception {
        RefundTransPo refundTransPo=new RefundTransPo();
        refundTransPo.setId(1761L);
        refundTransPo.setCreatorId(1L);
        refundTransPo.setCreatorName("admin");
        refundTransPo.setOutNo(null);
        refundTransPo.setStatus(RefundTrans.SUCCESS);
        refundTransPo.setTransNo("3518");
        refundTransPo.setAmount(216312L);
        refundTransPo.setPayTransId(3381L);
        refundTransPo.setDivAmount(10816L);
        refundTransPo.setShopId(2L);
        refundTransPo.setAccountId(529L);
        doReturn(refundTransPo).when(refundTransPoMapper).selectByPrimaryKey(1761L);
        when(aliPayMapper.getRefund(any(), any())).thenReturn("{\"trade_no\":\"134\",\"out_trade_no\":\"3518\",\"out_request_no\":\"3518\",\"total_amount\":216312,\"refund_amount\":216310,\"refund_status\":\"REFUND_SUCCESS\",\"refund_royaltys\":[{\"refund_amount\":216310,\"result_code\":\"SUCCESS\"}]}");
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_REFUNDS_ID, 0, 1761)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.INCONSISTENT_DATA.getErrNo())));
    }

    /**
     * 04 获取待退款的退款单
     * 快乐路径-微信端与本地的status相同
     */
    @Test
    void testGetRefundGivenStatusNewAndWechatStatusNew() throws Exception {

        String responseBody = "{\"refund_id\":\"134\",\"out_refund_no\":\"134\",\"transaction_id\":\"613\",\"out_trade_no\":\"666\",\"channel\":\"ORIGINAL\",\"user_received_account\":\"TingLans@oomall.com\",\"success_time\":[2023,11,29,9,29,12,33395800],\"create_time\":[2023,11,29,9,29,12,33395800],\"status\":\"PROCESSING\",\"refundRetAmount\":{\"total\":80280,\"refund\":80280,\"payer_total\":80280,\"payer_refund\":80280,\"settlement_refund\":80280,\"settlement_total\":80280,\"discount_refund\":10,\"currency\":\"CNY\",\"refundRetFrom\":{\"account\":\"AVAILABLE\",\"amount\":10436}}}";

        when(wePayMapper.getRefund(eq("134"), anyString())).thenReturn(responseBody);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_REFUNDS_ID, 0, 613)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.outNo", is("134")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", is(80280)));
    }

    /**
     * 04 获取待退款的退款单
     * 快乐路径-微信端与本地的status不同，对账正确
     */
    @Test
    void testGetRefundGivenStatusNewAndWechatStatusSuccess() throws Exception {

        String responseBody = "{\"refund_id\":\"134\",\"out_refund_no\":\"134\",\"transaction_id\":\"613\",\"out_trade_no\":\"666\",\"channel\":\"ORIGINAL\",\"user_received_account\":\"TingLans@oomall.com\",\"success_time\":[2023,11,29,8,58,8,429467300],\"create_time\":[2023,11,29,8,58,8,429467300],\"status\":\"SUCCESS\",\"refundRetAmount\":{\"total\":80280,\"refund\":80280,\"payer_total\":80280,\"payer_refund\":80280,\"settlement_refund\":80280,\"settlement_total\":80280,\"discount_refund\":80280,\"currency\":\"CNY\",\"refundRetFrom\":{\"account\":\"AVAILABLE\",\"amount\":10436}}}";

        when(wePayMapper.getRefund(eq("134"), anyString())).thenReturn(responseBody);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_REFUNDS_ID, 0, 613)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.outNo", is("134")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", is(80280)));
    }

       /**
     * 04 获取待退款的退款单
     * 意外路径-微信端与本地的status不同，错账
     */
    @Test
    void testGetRefundGivenStatusNewAndWrongAmount() throws Exception {

        String responseBody = "{\"refund_id\":\"134\",\"out_refund_no\":\"134\",\"transaction_id\":\"613\",\"out_trade_no\":\"666\",\"channel\":\"ORIGINAL\",\"user_received_account\":\"TingLans@oomall.com\",\"success_time\":[2023,11,29,8,58,45,751174800],\"create_time\":[2023,11,29,8,58,45,751174800],\"status\":\"SUCCESS\",\"refundRetAmount\":{\"total\":1000,\"refund\":500,\"payer_total\":1000,\"payer_refund\":500,\"settlement_refund\":490,\"settlement_total\":800,\"discount_refund\":10,\"currency\":\"CNY\",\"refundRetFrom\":{\"account\":\"AVAILABLE\",\"amount\":500}}}";

        when(wePayMapper.getRefund(eq("134"), anyString())).thenReturn(responseBody);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_REFUNDS_ID, 0, 613)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.outNo", is("134")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", is(80280)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ledger.transNo", is("134"))); // 对账失败则会产生一个台账
    }

    /**
     * @author huangzian
     * 2023-dgn1-006
     * 有效支付渠道 超出权限范围
     */
    @Test
    void testUpdateChannelValidWhenIdOutScope() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_VALID_CHANNELS, 1,501)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 有效支付渠道 成功路径
     */
    @Test
    void updateChannelValid() throws Exception
    {
        //先无效
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_INVALID_CHANNELS, 0,501)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_VALID_CHANNELS, 0,501)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * @author huangzian
     * 2023-dgn1-006
     * 无效支付渠道
     */
    @Test
    void updateChannelInValid() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_INVALID_CHANNELS, 0,501)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * @author huangzian
     * 2023-dgn1-006
     * 无效支付渠道
     */
    @Test
    void testUpdateChannelInValidWhenIdOutScope() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_INVALID_CHANNELS, 1,501)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     *获得商铺所有的支付渠道
     * @author huangzian
     * 2023-dgn1-006
     */
    @Test
    void retrieveAccount() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_ALL_ACCOUNTS, 1)
                        .header("authorization", adminToken)
                        .param("page","0")
                        .param("pageSize","10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '501')].subMchid",hasItem("1900008XXX")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '501')].status",hasItem(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '528')].subMchid",hasItem("198881")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '528')].status",hasItem(0)));
    }

    /**
     * @author huangzian
     * 2023-dgn1-006
     *获得商铺支付渠道 成功路径 redis为空
     */
    @Test
    void testRetrieveAccountsWhenRedisNotHasKey() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_ACCOUNTS, 1,501)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.channel.name",is("微信支付")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.channel.beginTime",is("2022-05-02T18:49:48")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.creator.name",is("admin111")));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 签约店铺支付渠道 微信签约成功
     */
    @Test
    void createWepayAccounts() throws Exception
    {
        String body = "{\"subMchid\": \"hza123\"}";
        String ret="{\"sub_mchid\": \"hza123\", \"type\": \"MERCHANT_ID\", " +
                "\"account\": \"1900007XXX\"}";
        Mockito.when(wePayMapper.createDiv(Mockito.any())).thenReturn(ret);
        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_ADD_ACCOUNTS, 73,501)
                        .header("authorization", shop73Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status",is(1)));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 签约店铺支付渠道 支付宝签约成功
     */
    @Test
    void createAlipayAccount() throws Exception
    {
        String body = "{\"subMchid\": \"hza123\"}";
        String ret="{\"result_code\":\"SUCCESS\"}";
        Mockito.when(aliPayMapper.createDiv(Mockito.anyString(),Mockito.any())).thenReturn(ret);
        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_ADD_ACCOUNTS, 73,502)
                        .header("authorization", shop73Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status",is(1)));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 微信签约失败：接口调用错误
     */
    @Test
    void testCreateWePayAccountsWhenInvokeApiError() throws Exception
    {
        String body = "{\"subMchid\": \"hza123\"}";
        String ret="{\"code\": \"403\",\"message\": \"参数错误\", " +
                "\"detail\": {\"field\": \"/amount/currency\",\"value\": \"XYZ\"," +
                "\"issue\": \"Currency code is invalid\", \"location\" :\"body\"}}";
        Mockito.when(wePayMapper.createDiv(Mockito.any())).thenReturn(ret);
        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_ADD_ACCOUNTS, 2,501)
                        .header("authorization", shop2Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.PAY_INVOKEAPI_ERROR.getErrNo())));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 支付宝签约失败：接口调用错误
     */
    @Test
    void testCreateAliPayAccountsWhenInvokeApiError() throws Exception
    {
        String body = "{\"subMchid\": \"hza123\"}";
        String ret = "{\"code\": \"403\",\"message\": \"支付宝错误\"," +
                "\"details\": [{\"field\": \"field_name\",\"value\": \"value_passed\",\"location\": \"field_location\",\"issue\": \"problem_with_field\",\"description\": \"Error description.\"}]}";
        Mockito.when(aliPayMapper.createDiv(Mockito.anyString(),Mockito.any())).thenReturn(ret);
        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_ADD_ACCOUNTS, 73,502)
                        .header("authorization", shop73Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.PAY_INVOKEAPI_ERROR.getErrNo())));
    }

    /**
     * @author huangzian
     * 2023-dgn1-006
     * 无法解约当前有效的
     */
    @Test
    void delPayAccountGivenValid() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_DEL_ACCOUNTS, 1,501)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 解约微信成功
     */
    @Test
    void delWePayAccount() throws Exception
    {
        String ret="{\"sub_mchid\": \"hza123\", \"type\": \"MERCHANT_ID\", " +
                "\"account\": \"1900007XXX\"}";
        Mockito.when(wePayMapper.cancelDiv(Mockito.any())).thenReturn(ret);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_DEL_ACCOUNTS, 8,545)
                        .header("authorization", shop8Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 解约微信失败，调用接口错误
     */
    @Test
    void testDelWePayAccountsWhenInvokeApiError() throws Exception
    {
        String ret="{\"code\": \"403\",\"message\": \"参数错误\", " +
                "\"detail\": {\"field\": \"/amount/currency\",\"value\": \"XYZ\"," +
                "\"issue\": \"Currency code is invalid\", \"location\" :\"body\"}}";
        Mockito.when(wePayMapper.cancelDiv(Mockito.any())).thenReturn(ret);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_DEL_ACCOUNTS, 8,545)
                        .header("authorization", shop8Token))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.PAY_INVOKEAPI_ERROR.getErrNo())));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 解约支付宝成功
     */
    @Test
    void delAliPayAccount() throws Exception
    {
        //数据库里没有可以解约的支付宝（即无效的），所以先进行无效操作
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_INVALID_CHANNELS, 1,528)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

        String ret="{\"result_code\":\"SUCCESS\"}";
        Mockito.when(aliPayMapper.cancelDiv(Mockito.anyString(), Mockito.any())).thenReturn(ret);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_DEL_ACCOUNTS, 1,528)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 解约支付宝失败，调用接口错误
     */
    @Test
    void testDelAliPayAccountsWhenInvokeApiError() throws Exception
    {
        //数据库里没有可以解约的支付宝（即无效的），所以先进行无效操作
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_INVALID_CHANNELS, 1,528)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

        String ret = "{\"code\": \"403\",\"message\": \"支付宝错误\"," +
                "\"details\": [{\"field\": \"field_name\",\"value\": \"value_passed\",\"location\": \"field_location\"," +
                "\"issue\": \"problem_with_field\",\"description\": \"Error description.\"}]}";
        Mockito.when(aliPayMapper.cancelDiv(Mockito.anyString(), Mockito.any())).thenReturn(ret);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_DEL_ACCOUNTS, 1,528)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.PAY_INVOKEAPI_ERROR.getErrNo())));
    }

    /**
     * @author huangzian
     * 2023-dgn1-006
     *有效商铺的支付渠道 成功路径
     */
    @Test
    void updateAccountValid() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_VALID_CHANNELS, 8,545)
                        .header("authorization", shop8Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 本来就有效了
     */
    @Test
    void testUpdateAccountValidGivenValidAccount() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_VALID_CHANNELS, 1,501)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

    /**
     * @author huangzian
     * 2023-dgn1-006
     *无效商铺的支付渠道 成功路径
     */
    @Test
    void updateAccountInvalid() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_INVALID_CHANNELS, 1,501)
                        .header("authorization", shop1Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 本来就无效
     */
    @Test
    void testUpdateAccountInvalidGivenInvalidAccount() throws Exception
    {
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_INVALID_CHANNELS, 8,545)
                        .header("authorization", shop8Token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

    /**
    *查找支付对账无误-微信
    *@Author 37220222203851
    */
    @Test
    public void testGetPaymentWhenSuccessWithWePay() throws Exception{
        String respond = "{\"sp_appid\": \"wxdace645e0bc2cXXX\","
                + "\"sp_mchid\": \"1900007XXX\","
                +"\"sub_mchid\": \"1900008XXX\","
                +"\"out_trade_no\": \"b3682ea011c547a49e8d7cc93107b71c\","
                +"\"transaction_id\": \"4200000985202103031441826014\","
                +"\"trade_type\": \"JSAPI\","
                +"\"trade_state\": \"SUCCESS\","
                +"\"trade_state_desc\": \"支付成功\","
                +"\"success_time\": \"2022-12-17T11:04:56+08:00\","
                +"\"transRetPayer\": {\"sp_openid\":\"o4GgauMQHaUO8ujCGIXNKATQlXXX\"},"
                +"\"transRetAmount\":{\"total\":\"1000\",\"payer_total\":\"1000\"}" +
                "}";
        Mockito.when(wePayMapper.getOrderByTransId(eq("768"),Mockito.any(),Mockito.any())).thenReturn(respond);
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_PAYMENT, 0, 768)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.amount").value(1000L));
    }

    /**
    *查找支付对账无误-支付宝
    *@Author 37220222203851
    */
    @Test
    public void testGetPaymentWhenSuccessWithAliPay() throws Exception {
        String returnStr = "{\"trade_no\": \"2013112011001004330000121536\","
                + "\"out_trade_no\": \"6823789339978248\","
                +"\"buyer_logon_id\": \"159****5620\","
                +"\"trade_status\": \"TRADE_SUCCESS\","
                +"\"total_amount\": \"4462.06\"}";

        Mockito.when(aliPayMapper.retrieveOrder(Mockito.anyString(), Mockito.any())).thenReturn(returnStr);
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_PAYMENT, 0, 3898)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.amount").value(446206L));
    }

    /**
    *查找支付，错账
    *@Author 37220222203851
    */
    @Test
    public void testGetPaymentWhenSuccessAndWrongAmount() throws Exception {
        String returnStr = "{\"trade_no\": \"2013112011001004330000121536\","
                + "\"out_trade_no\": \"6823789339978248\","
                +"\"buyer_logon_id\": \"159****5620\","
                +"\"trade_status\": \"TRADE_SUCCESS\","
                +"\"total_amount\": \"4462.07\"}";

        Mockito.when(aliPayMapper.retrieveOrder(Mockito.anyString(), Mockito.any())).thenReturn(returnStr);
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_GET_PAYMENT, 0, 3898)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.amount").value(446206L))
                .andExpect(jsonPath("$.data.status").value(PayTrans.WRONG.intValue()));
    }

    /**
    *查询退款,无误
    *@Author 37220222203851
    */
    @Test
    void testGetRefundWhenSuccessWithWePay() throws Exception {

        String responseBody = "{\"refund_id\":\"140\"," +
                "\"out_refund_no\":\"140\"," +
                "\"transaction_id\":\"614\"," +
                "\"out_trade_no\":\"666\"," +
                "\"channel\":\"ORIGINAL\"," +
                "\"user_received_account\":\"TingLans@oomall.com\"," +
                "\"success_time\":[2023,11,29,9,29,12,33395800]," +
                "\"create_time\":[2023,11,29,9,29,12,33395800]," +
                "\"status\":\"PROCESSING\"," +
                "\"refundRetAmount\":{\"total\":228795,\"refund\":228795,\"payer_total\":228795,\"payer_refund\":228795,\"settlement_refund\":228795,\"settlement_total\":228795,\"discount_refund\":10,\"currency\":\"CNY\",\"refundRetFrom\":{\"account\":\"AVAILABLE\",\"amount\":10436}}}";

        when(wePayMapper.getRefund(eq("140"), anyString())).thenReturn(responseBody);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_REFUNDS_ID, 1, 614)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.outNo", is("140")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.amount", is(228795)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status", is(RefundTrans.SUCCESS.intValue())));
    }


}
