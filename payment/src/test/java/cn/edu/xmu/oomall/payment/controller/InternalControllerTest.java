package cn.edu.xmu.oomall.payment.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import cn.edu.xmu.oomall.payment.controller.dto.PayTransDto;
import cn.edu.xmu.oomall.payment.mapper.generator.LedgerPoMapper;
import cn.edu.xmu.oomall.payment.mapper.openfeign.AliPayMapper;
import cn.edu.xmu.oomall.payment.mapper.openfeign.WePayMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static cn.edu.xmu.javaee.core.model.Constants.END_TIME;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author ych
 * task 2023-dgn1-004
 */

@SpringBootTest(classes = PaymentApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class InternalControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private WePayMapper wePayMapper;
    @MockBean
    private AliPayMapper aliPayMapper;
    @SpyBean
    private LedgerPoMapper ledgerPoMapper;
    private static String adminToken;
    private final String ADMIN_CREATE_PAYMENT = "/internal/accounts/{id}/payments";
    private final String ADMIN_CANCEL_PAYMENT = "/internal/shops/{shopId}/payments/{id}";
    private final String ADMIN_DIV_PAYMENT = "/internal/channels/{id}/payments/div";
    private final String ADMIN_PAYMENT_ID_CREATE_REFUNDS = "/internal/shops/{shopId}/payments/{id}/refunds";
    private final String ADMIN_REFUNDS_ID = "/internal/shops/{shopId}/refunds/{id}";
    private static String shopToken1;
    private static String shopToken2;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        shopToken1 = jwtHelper.createToken(1L, "shop1", 1L, 1, 3600);
        shopToken2 = jwtHelper.createToken(2L, "shop2", 2L, 1, 3600);
        adminToken = jwtHelper.createToken(0L, "13088admin", 0L, 0, 3600);
    }

    /**
     * 01 创建退款单
     * 快乐路径-未分账
     */
    @Test
    void testCreateRefundGivenNotDivPayment() throws Exception {
        when(redisUtil.set(eq("SC502"), any(), eq(3600))).thenReturn(true);

        String expectedResponse = "{\"trade_no\":\"74\",\"out_trade_no\":\"74\",\"buyer_login_id\":\"user123\",\"refund_fee\":352468.0,\"fund_change\":\"Y\"}\n";

        when(this.aliPayMapper.refund(anyString(), any())).thenReturn(expectedResponse);

        String requestBody = "{\"amount\": 352468, \"divAmount\": 25}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PAYMENT_ID_CREATE_REFUNDS, 2, 920)
                        .header("authorization",  shopToken2)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.transNo", is("74")));
    }

    /**
     * 01 创建退款单
     * 快乐路径-已分账
     */
    @Test
    void testCreateRefundGivenDivPayment() throws Exception {
        when(redisUtil.set(eq("SC501"), any(), eq(3600))).thenReturn(true);

        String refundResponse = "{ \"refund_id\": \"10\", \"out_refund_no\": \"10\", \"transaction_id\": \"789\", \"out_trade_no\": \"012\", \"channel\": \"ORIGINAL\", \"user_received_account\": \"user_account\", \"create_time\": \"2023-01-01T00:00:00\", \"status\": \"SUCCESS\", \"amount\": { \"total\": 2000, \"refund\": 1000, \"payer_total\": 1000, \"payer_refund\": 1000, \"settlement_refund\": 1000, \"settlement_total\": 100, \"discount_refund\": 0, \"currency\": \"CNY\", \"refund_fee\": 0 } }";
        String postDivRefundResponse = "{\"sub_mchid\":\"A1B2C3D4E5F6\",\"order_id\":\"WX1234567890\",\"out_order_no\":\"MERCHANT123456\",\"out_return_no\":\"RETURN987654321\",\"return_id\":\"6666\",\"return_mchild\":\"MERCHANT_CHILD\",\"channel\":\"BALANCE\",\"user_received_account\":\"2333\",\"create_time\":\"2023-11-29T12:30:45\",\"result\":\"SUCCESS\",\"amount\":500,\"finish_time\":\"2023-11-29T13:45:20\"}";

        when(this.wePayMapper.refund(any())).thenReturn(refundResponse);
        when(this.wePayMapper.postDivRefund(any())).thenReturn(postDivRefundResponse);

        String requestBody = "{\"amount\": 1000, \"divAmount\": 500}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PAYMENT_ID_CREATE_REFUNDS, 1, 863)
                        .header("authorization",  shopToken1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.transNo", is("10")));

    }

    /**
     * 01 创建退款单
     * 意外路径-退款总额超过支付单金额
     */
    @Test
    void testCreateRefundGivenWrongAmount() throws Exception {
        when(redisUtil.set(eq("SC501"), any(), eq(3600))).thenReturn(true);

        String expectedResponse = "{ \"refund_id\": \"26\", \"out_refund_no\": \"456\", \"transaction_id\": \"789\", \"out_trade_no\": \"012\", \"channel\": \"ORIGINAL\", \"user_received_account\": \"user_account\", \"create_time\": \"2023-01-01T00:00:00\", \"status\": \"SUCCESS\", \"amount\": { \"total\": 700000, \"refund\": 650575, \"payer_total\": 650575, \"payer_refund\": 650575, \"settlement_refund\": 650575, \"settlement_total\": 650575, \"discount_refund\": 0, \"currency\": \"CNY\", \"refund_fee\": 0 } }";

        when(this.wePayMapper.refund(any())).thenReturn(expectedResponse);

        String requestBody = "{\"amount\": 650575, \"divAmount\": 3000}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PAYMENT_ID_CREATE_REFUNDS, 1, 875)
                        .header("authorization",  shopToken1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.PAY_REFUND_MORE.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("退款金额超过支付对象(id=875)的金额")));
    }

    /**
     * 01 创建退款单
     * 意外路径-分账退款金额超过支付分账金额
     */
    @Test
    void testCreateRefundGivenWrongDivAmount() throws Exception {
        when(redisUtil.set(eq("SC501"), any(), eq(3600))).thenReturn(true);

        String refundResponse = "{ \"refund_id\": \"10\", \"out_refund_no\": \"10\", \"transaction_id\": \"789\", \"out_trade_no\": \"012\", \"channel\": \"ORIGINAL\", \"user_received_account\": \"user_account\", \"create_time\": \"2023-01-01T00:00:00\", \"status\": \"SUCCESS\", \"amount\": { \"total\": 60000, \"refund\": 50000, \"payer_total\": 50000, \"payer_refund\": 50000, \"settlement_refund\": 50000, \"settlement_total\": 50000, \"discount_refund\": 0, \"currency\": \"CNY\", \"refund_fee\": 0 } }";
        String postDivRefundResponse = "{\"sub_mchid\":\"A1B2C3D4E5F6\",\"order_id\":\"WX1234567890\",\"out_order_no\":\"MERCHANT123456\",\"out_return_no\":\"RETURN987654321\",\"return_id\":\"6666\",\"return_mchild\":\"MERCHANT_CHILD\",\"channel\":\"BALANCE\",\"user_received_account\":\"2333\",\"create_time\":\"2023-11-29T12:30:45\",\"result\":\"SUCCESS\",\"amount\":2500,\"finish_time\":\"2023-11-29T13:45:20\"}";

        when(this.wePayMapper.refund(any())).thenReturn(refundResponse);
        when(this.wePayMapper.postDivRefund(any())).thenReturn(postDivRefundResponse);

        String requestBody = "{\"amount\": 50000, \"divAmount\": 2500}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PAYMENT_ID_CREATE_REFUNDS, 1, 878)
                        .header("authorization",  shopToken1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.PAY_DIVREFUND_MORE.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("分账退回金额超过支付对象(id=878)的支付分账金额")));
    }

    /**
     * 01 创建退款单
     * 意外路径-分账退款金额小于退款金额
     */
    @Test
    void testCreateRefundGivenWrongAmountAndWrongDivAmount() throws Exception {
        when(redisUtil.set(eq("SC501"), any(), eq(3600))).thenReturn(true);

        String expectedResponse = "{ \"refund_id\": \"123\", \"out_refund_no\": \"456\", \"transaction_id\": \"789\", \"out_trade_no\": \"012\", \"channel\": \"ORIGINAL\", \"user_received_account\": \"user_account\", \"create_time\": \"2023-01-01T00:00:00\", \"status\": \"SUCCESS\", \"amount\": { \"total\": 100, \"refund\": 50, \"payer_total\": 50, \"payer_refund\": 50, \"settlement_refund\": 50, \"settlement_total\": 100, \"discount_refund\": 0, \"currency\": \"CNY\", \"refund_fee\": 0 } }";

        when(this.wePayMapper.refund(any())).thenReturn(expectedResponse);

        String requestBody = "{\"amount\": 50, \"divAmount\": 100}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PAYMENT_ID_CREATE_REFUNDS, 1, 880)
                        .header("authorization",  shopToken1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FIELD_NOTVALID.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("分账退回金额不能大于退款金额")));
    }

    /**
     * 01 创建退款单
     * 意外路径 支付单不能创建退款单
     */
    @Test
    void testCreateRefundGivenWrongPaymentStatus() throws Exception {
        when(redisUtil.set(eq("SC501"), any(), eq(3600))).thenReturn(true);

        String expectedResponse = "{ \"refund_id\": \"123\", \"out_refund_no\": \"456\", \"transaction_id\": \"789\", \"out_trade_no\": \"012\", \"channel\": \"ORIGINAL\", \"user_received_account\": \"user_account\", \"create_time\": \"2023-01-01T00:00:00\", \"status\": \"SUCCESS\", \"amount\": { \"total\": 100, \"refund\": 50, \"payer_total\": 50, \"payer_refund\": 50, \"settlement_refund\": 50, \"settlement_total\": 100, \"discount_refund\": 0, \"currency\": \"CNY\", \"refund_fee\": 0 } }";

        when(this.wePayMapper.refund(any())).thenReturn(expectedResponse);

        String requestBody = "{\"amount\": 50, \"divAmount\": 25}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PAYMENT_ID_CREATE_REFUNDS, 1, 1056)
                        .header("authorization",  shopToken1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("支付对象对象（id=1056）错账状态禁止此操作")));

    }

    /**
     * 01 创建退款单
     * 意外路径-操作其他商户的资源
     */
    @Test
    void testCreateRefundGivenWrongToken() throws Exception {
        when(redisUtil.set(eq("SC502"), any(), eq(3600))).thenReturn(true);

        String expectedResponse = "{\"trade_no\":\"74\",\"out_trade_no\":\"74\",\"buyer_login_id\":\"user123\",\"refund_fee\":352468.0,\"fund_change\":\"Y\"}\n";

        when(this.aliPayMapper.refund(anyString(), any())).thenReturn(expectedResponse);

        String requestBody = "{\"amount\": 352468, \"divAmount\": 25}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PAYMENT_ID_CREATE_REFUNDS, 2, 920)
                        .header("authorization",  shopToken1)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * 05 获取已经完成的退款单
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
     * 05 获取待退款的退款单
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
     * 05 获取待退款的退款单
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
     * 05 获取待退款的退款单
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
     * @Author 37720222205040
     */
    @Test
    void testLedgerFindByIdIsNull() throws Exception {
        when(aliPayMapper.getRefund(any(), any())).thenReturn("{\"trade_no\":\"134\",\"out_trade_no\":\"3518\",\"out_request_no\":\"3518\",\"total_amount\":216312,\"refund_amount\":216310,\"refund_status\":\"REFUND_SUCCESS\",\"refund_royaltys\":[{\"refund_amount\":216310,\"result_code\":\"SUCCESS\"}]}");
        doReturn(null).when(ledgerPoMapper).selectByPrimaryKey(anyLong());
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_REFUNDS_ID, 0, 1761)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }
    /**
     * task 2023-dgn1-004
     * @author ych
     * 微信创建支付
     */
    @Test
    public void testCreatePaymentWhenChannelIsWePay() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(wePayMapper.pay(Mockito.any())).thenReturn("{\"prepayId\": \"wx201410272009395522657a690389285100\"}");

        PayTransDto payTransDto = new PayTransDto();
        payTransDto.setTimeBegin(END_TIME);
        payTransDto.setTimeExpire(END_TIME.plusHours(2));
        payTransDto.setAmount(100L);
        payTransDto.setDivAmount(50L);
        payTransDto.setOutNo("123456");
        payTransDto.setDescription("这是支付描述");

        String payVoJson = objectMapper.writeValueAsString(payTransDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_PAYMENT,501)
                        .header("authorization", adminToken)
                        .content(payVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.errno").value(1));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 支付宝创建支付
     */
    @Test
    public void testCreatePaymentChannelIsAliPay() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(aliPayMapper.pay(Mockito.anyString(), Mockito.any())).thenReturn("{\"out_trade_no\": \"70501111111S001111119\",\"trade_no\": \"2014112400001000340011111118\",\"total_amount\": \"9\",\"seller_id\": \"2088111111116894\",\"merchant_order_no\": \"20161008001\"}");

        PayTransDto payTransDto = new PayTransDto();
        payTransDto.setTimeBegin(END_TIME);
        payTransDto.setTimeExpire(END_TIME.plusHours(2));
        payTransDto.setAmount(100L);
        payTransDto.setDivAmount(50L);
        payTransDto.setOutNo("123456");
        payTransDto.setDescription("这是支付描述");

        String payVoJson = objectMapper.writeValueAsString(payTransDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_PAYMENT,528)
                        .header("authorization", adminToken)
                        .content(payVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.errno").value(1));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 结束时间不能早于开始时间
     */
    @Test
    public void testCreatePaymentGivenWrongTime() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(wePayMapper.pay(Mockito.any())).thenReturn("{\"prepayId\": \"wx201410272009395522657a690389285100\"}");

        PayTransDto payTransDto = new PayTransDto();
        payTransDto.setTimeBegin(END_TIME);
        payTransDto.setTimeExpire(END_TIME.minusHours(2));
        payTransDto.setAmount(100L);
        payTransDto.setDivAmount(50L);
        payTransDto.setOutNo("123456");
        payTransDto.setDescription("这是支付描述");

        String payVoJson = objectMapper.writeValueAsString(payTransDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_PAYMENT,501)
                        .header("authorization", adminToken)
                        .content(payVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 分账金额不能大于支付金额
     */
    @Test
    public void testCreatePaymentGivenWrongAmount() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(wePayMapper.pay(Mockito.any())).thenReturn("{\"prepayId\": \"wx201410272009395522657a690389285100\"}");

        PayTransDto payTransDto = new PayTransDto();
        payTransDto.setTimeBegin(END_TIME);
        payTransDto.setTimeExpire(END_TIME.plusHours(2));
        payTransDto.setAmount(50L);
        payTransDto.setDivAmount(100L);
        payTransDto.setOutNo("123456");
        payTransDto.setDescription("这是支付描述");

        String payVoJson = objectMapper.writeValueAsString(payTransDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_PAYMENT,501)
                        .header("authorization", adminToken)
                        .content(payVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo()));
    }

    /**
     * @author 37720222205040
     * 对应Amount为INVALID
     */
    @Test
    public void testCreatePaymentAmountIsInvalid() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(wePayMapper.pay(Mockito.any())).thenReturn("{\"prepayId\": \"wx201410272009395522657a690389285100\"}");

        PayTransDto payTransDto = new PayTransDto();
        payTransDto.setTimeBegin(END_TIME);
        payTransDto.setTimeExpire(END_TIME.plusHours(2));
        payTransDto.setAmount(50L);
        payTransDto.setDivAmount(10L);
        payTransDto.setOutNo("123456");
        payTransDto.setDescription("这是支付描述");

        String payVoJson = objectMapper.writeValueAsString(payTransDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_PAYMENT,547)
                        .header("authorization", adminToken)
                        .content(payVoJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.errno").value(ReturnNo.PAY_CHANNEL_INVALID.getErrNo()));
    }
    /**
     * task 2023-dgn1-004
     * @author ych
     * 微信取消支付 成功
     */
    @Test
    public void testCancelPaymentWhenChannelIsWePay() throws Exception {

        Mockito.when(wePayMapper.cancelOrder(Mockito.anyString(), Mockito.any())).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_CANCEL_PAYMENT,1,860)
                        .header("authorization", shopToken1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 微信取消支付 无法取消不符合状态的支付
     */
    @Test
    public void testCancelPaymentWhenChannelIsWePayAndGivenWrongState() throws Exception {

        Mockito.when(wePayMapper.cancelOrder(Mockito.anyString(), Mockito.any())).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_CANCEL_PAYMENT,1,859)
                        .header("authorization", shopToken1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 支付宝取消支付 成功
     */
    @Test
    public void testCancelPaymentWhenChannelIsAliPay() throws Exception {

        String returnStr = "{\"trade_no\": \"2013112111001004500000675971\","
                + "\"out_trade_no\": \"YX_001\"}";

        Mockito.when(aliPayMapper.cancelOrder(Mockito.anyString(), Mockito.any())).thenReturn(returnStr);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_CANCEL_PAYMENT,1,862)
                        .header("authorization", shopToken1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 支付宝取消支付 无法取消已支付的支付交易
     */
    @Test
    public void testCancelPaymentWhenChannelIsAliPayAndGivenWrongState() throws Exception {

        String returnStr = "{\"trade_no\": \"2013112111001004500000675971\","
                + "\"out_trade_no\": \"YX_001\"}";

        Mockito.when(aliPayMapper.cancelOrder(Mockito.anyString(), Mockito.any())).thenReturn(returnStr);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_CANCEL_PAYMENT,2,920)
                        .header("authorization", shopToken2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 微信分账
     */
    @Test
    public void testDivPaymentWhenChannelIsWePay() throws Exception {

        String returnStr = "{\"sub_mchid\": \"1900000109\","
                + "\"transaction_id\": \"4208450740201411110007820472\","
                +"\"out_order_no\": \"P20150806125346\","
                +"\"order_id\": \"3008450740201411110007820472\","
                +"\"state\": \"FINISHED\"}";
        Mockito.when(wePayMapper.postDivPay(Mockito.any())).thenReturn(returnStr);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_DIV_PAYMENT,501)
                        .param("endTime","2022-12-01T01:30:00.000-05:00")
                        .header("authorization", adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }

    /**
     * task 2023-dgn1-004
     * @author ych
     * 支付宝分账
     */
    @Test
    public void testDivPaymentWhenChannelIsAliPay() throws Exception {

        String returnStr = "{\"trade_no\": \"2015070921001004130000127422\","
                + "\"settle_no\": \"20210718002530070036530006474222\"}";

        Mockito.when(aliPayMapper.postDivPay(Mockito.anyString() ,Mockito.any())).thenReturn(returnStr);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_DIV_PAYMENT,502)
                        .param("endTime","2022-12-01T01:30:00.000-05:00")
                        .header("authorization", adminToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }
}