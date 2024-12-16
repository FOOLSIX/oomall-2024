package cn.edu.xmu.oomall.wechatpay.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.controller.vo.*;
import cn.edu.xmu.oomall.wechatpay.mapper.notify.WeChatPayNotifyMapper;
import cn.edu.xmu.oomall.wechatpay.mapper.notify.po.PaymentNotifyPo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author maguoqi
 * @date 2023/12/4
 */
@SpringBootTest(classes = WeChatPayApplication.class)
@AutoConfigureMockMvc
@Transactional
public class WeChatPayControllerTest {

    private final String INTERNAL_V3_PAY = "/internal/v3/pay/partner/transactions/jsapi";
    private final String INTERNAL_V3_GET_PAY_BY_OUTNO = "/internal/v3/pay/partner/transactions/out-trade-no/{out_trade_no}";
    private final String INTERNAL_V3_GET_PAY_BY_TRANSID = "/internal/v3/pay/partner/transactions/id/{transaction_id}";
    private final String INTERNAL_V3_CANCEL_PAY = "/internal/v3/pay/partner/transactions/out-trade-no/{out_trade_no}/close";
    private final String INTERNAL_V3_REFUND = "/internal/v3/refund/domestic/refunds";
    private final String INTERNAL_V3_GET_REFUND_BY_OUTNO = "/internal/v3/refund/domestic/refunds/{out_refund_no}";
    private final String INTERNAL_V3_DIV_PAY = "/internal/v3/profitsharing/orders";
    private final String INTERNAL_V3_CANCEL_DIV_PAY = "/internal/v3/profitsharing/return-orders";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeChatPayNotifyMapper weChatPayNotifyMapper;

    @Test
    public void testPay() throws Exception {

        Mockito.when(weChatPayNotifyMapper.notifyPayment(Mockito.anyString(), Mockito.any(PaymentNotifyPo.class))).thenReturn(new ReturnObject());

        PayTransVo vo = new PayTransVo();
        vo.setSpAppid("wx8888888888888888");
        vo.setSpMchid("1230000109");
        vo.setSubMchid("1900000109");
        vo.setDescription("OOMALL-新疆厦带-QQ公仔");
        vo.setAmount(new PayTransVo.PayAmount(100));
        vo.setPayer(new PayTransVo.Payer("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"));
        vo.setNotifyUrl("/notify/payments/wepay");
        vo.setOutTradeNo("OOMALL123456");
        vo.setTimeExpire(LocalDateTime.now().plusMinutes(30));

        this.mvc.perform(post(INTERNAL_V3_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.prepayId", startsWith("P")));
    }

    @Test
    public void testPayWhenPayFail() throws Exception {

        Mockito.when(weChatPayNotifyMapper.notifyPayment(Mockito.anyString(), Mockito.any(PaymentNotifyPo.class))).thenReturn(new ReturnObject());

        PayTransVo vo = new PayTransVo();
        vo.setSpAppid("wx8888888888888888");
        vo.setSpMchid("1230000109");
        vo.setSubMchid("1900000109");
        vo.setDescription("OOMALL-新疆厦带-QQ公仔");
        vo.setAmount(new PayTransVo.PayAmount(9901));
        vo.setPayer(new PayTransVo.Payer("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"));
        vo.setNotifyUrl("/notify/payments/wepay");
        vo.setOutTradeNo("OOMALL123456");
        vo.setTimeExpire(LocalDateTime.now().plusMinutes(30));

        this.mvc.perform(post(INTERNAL_V3_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.prepayId", is(nullValue())));
    }

    @Test
    public void testPayWhenOrderPaid() throws Exception {

        Mockito.when(weChatPayNotifyMapper.notifyPayment(Mockito.anyString(), Mockito.any(PaymentNotifyPo.class))).thenReturn(new ReturnObject());

        PayTransVo vo = new PayTransVo();
        vo.setSpAppid("wx8888888888888888");
        vo.setSpMchid("1230000109");
        vo.setSubMchid("1900000109");
        vo.setDescription("OOMALL-新疆厦带-QQ公仔");
        vo.setAmount(new PayTransVo.PayAmount(100));
        vo.setPayer(new PayTransVo.Payer("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"));
        vo.setNotifyUrl("/notify/payments/wepay");
        vo.setOutTradeNo("OOMALL12177525012");
        vo.setTimeExpire(LocalDateTime.now().plusMinutes(30));

        this.mvc.perform(post(INTERNAL_V3_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("订单已支付")));
    }

    @Test
    public void testPayWhenParamError() throws Exception {

        Mockito.when(weChatPayNotifyMapper.notifyPayment(Mockito.anyString(), Mockito.any(PaymentNotifyPo.class))).thenReturn(new ReturnObject());

        PayTransVo vo = new PayTransVo();
        vo.setSpAppid("wx8888888888888888");
        vo.setSpMchid("1230000109");
        vo.setSubMchid("1900000109");
        vo.setDescription("OOMALL-新疆厦带-QQ公仔");
        vo.setAmount(new PayTransVo.PayAmount(100));
        vo.setPayer(new PayTransVo.Payer("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"));
        vo.setNotifyUrl("/notify/payments/wepay");
        vo.setOutTradeNo("");
        vo.setTimeExpire(LocalDateTime.now().plusMinutes(30));

        this.mvc.perform(post(INTERNAL_V3_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("outTradeNo:must not be blank,")));
    }

    @Test
    public void testPayWhenOutTradeNoUsed() throws Exception {

        Mockito.when(weChatPayNotifyMapper.notifyPayment(Mockito.anyString(), Mockito.any(PaymentNotifyPo.class))).thenReturn(new ReturnObject());

        PayTransVo vo = new PayTransVo();
        vo.setSpAppid("wx8888888888888888");
        vo.setSpMchid("123456");
        vo.setSubMchid("1900000109");
        vo.setDescription("OOMALL-广州厦带-QQ公仔");
        vo.setAmount(new PayTransVo.PayAmount(100));
        vo.setPayer(new PayTransVo.Payer("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"));
        vo.setNotifyUrl("/notify/payments/wepay");
        vo.setOutTradeNo("OOMALL91321453318");
        vo.setTimeExpire(LocalDateTime.now().plusMinutes(30));

        this.mvc.perform(post(INTERNAL_V3_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("商户订单号重复")));
    }

    @Test
    public void testGetOrderByOutNo() throws Exception {
        String responseString = this.mvc.perform(get(INTERNAL_V3_GET_PAY_BY_OUTNO, "OOMALL12177525012")
                        .param("sp_mchid", "1230000109")
                        .param("sub_mchid", "1900000109"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = """
                {"spMchid":"1230000109","subMchid":"1900000109","outTradeNo":"OOMALL12177525012","tradeState":"SUCCESS","tradeStateDesc":"支付成功"}
                """;
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void testGetOrderByOutNoWhenOrderNotExist() throws Exception {

        this.mvc.perform(get(INTERNAL_V3_GET_PAY_BY_OUTNO, "0")
                        .param("sp_mchid", "1230000109")
                        .param("sub_mchid", "1900000109"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("查询的资源不存在")));
    }

    @Test
    public void testGetOrderByTransId() throws Exception {

        String responseString = this.mvc.perform(get(INTERNAL_V3_GET_PAY_BY_TRANSID, "T99732")
                        .param("sp_mchid", "1230000109")
                        .param("sub_mchid", "1900000109"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = """
                {"spMchid":"1230000109","subMchid":"1900000109","outTradeNo":"OOMALL12177525012","tradeState":"SUCCESS","tradeStateDesc":"支付成功"}
                """;
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void testGetOrderByTransIdWhenOrderNotExist() throws Exception {

        this.mvc.perform(get(INTERNAL_V3_GET_PAY_BY_OUTNO, "0")
                        .param("sp_mchid", "1230000109")
                        .param("sub_mchid", "1900000109"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("查询的资源不存在")));
    }

    @Test
    public void testCancelOrder() throws Exception {
        PayTransCancelVo vo = new PayTransCancelVo("1230000109", "1900000109");

        this.mvc.perform(post(INTERNAL_V3_CANCEL_PAY, "OOMALL91321453318")
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCancelOrderWhenOrderNotExist() throws Exception {
        PayTransCancelVo vo = new PayTransCancelVo("1230000109", "1900000109");

        this.mvc.perform(post(INTERNAL_V3_CANCEL_PAY, "0")
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("查询的资源不存在")));
    }

    @Test
    public void testRefund() throws Exception {

        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T99732");
        vo.setAmount(new RefundAmountVo(100, 100));
        vo.setOutRefundNo("OOMALL333444");

        this.mvc.perform(post(INTERNAL_V3_REFUND)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.refundId", startsWith("RF")))
                .andExpect(jsonPath("$.outRefundNo", is("OOMALL333444")))
                .andExpect(jsonPath("$.transactionId", is("T99732")))
                .andExpect(jsonPath("$.outTradeNo", is("OOMALL12177525012")))
                .andExpect(jsonPath("$.userReceivedAccount", is("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o")))
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.amount.total", is(100)))
                .andExpect(jsonPath("$.amount.refund", is(100)));
    }

    @Test
    public void testRefundWhenRefundFail() throws Exception {

        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T95821");
        vo.setAmount(new RefundAmountVo(901, 1000));
        vo.setOutRefundNo("OOMALL330003444");

        this.mvc.perform(post(INTERNAL_V3_REFUND)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.refundId", startsWith("RF")))
                .andExpect(jsonPath("$.outRefundNo", is("OOMALL330003444")))
                .andExpect(jsonPath("$.transactionId", is("T95821")))
                .andExpect(jsonPath("$.outTradeNo", is("OOMALL91123453318")))
                .andExpect(jsonPath("$.userReceivedAccount", is("oUpF1234AJO_M2pxb1Q9zNjWeS6o")))
                .andExpect(jsonPath("$.status", is("ABNORMAL")))
                .andExpect(jsonPath("$.amount.total", is(1000)))
                .andExpect(jsonPath("$.amount.refund", is(901)));
    }

    @Test
    public void testRefundWhenAmountError() throws Exception {

        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T99732");
        vo.setAmount(new RefundAmountVo(120, 100));
        vo.setOutRefundNo("OOMALL333444");

        this.mvc.perform(post(INTERNAL_V3_REFUND)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("退款金额错误")));

    }

    @Test
    public void testRefundWhenOrderNotPaid() throws Exception {

        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T94432");
        vo.setAmount(new RefundAmountVo(100, 100));
        vo.setOutRefundNo("OOMALL333444");

        this.mvc.perform(post(INTERNAL_V3_REFUND)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("对应支付单未成功支付")));

    }

    @Test
    public void testRefundWhenParamError() throws Exception {

        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T99732");
        vo.setAmount(new RefundAmountVo(100, 100));
        vo.setOutRefundNo("");

        this.mvc.perform(post(INTERNAL_V3_REFUND)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("outRefundNo:must not be blank,")));
    }

    @Test
    public void testGetRefund() throws Exception {

        this.mvc.perform(get(INTERNAL_V3_GET_REFUND_BY_OUTNO, "OOMALL12153368018")
                        .param("sub_mchid", "1900000109"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.refundId", is("RF33246")))
                .andExpect(jsonPath("$.outRefundNo", is("OOMALL12153368018")))
                .andExpect(jsonPath("$.transactionId", is("T93232")))
                .andExpect(jsonPath("$.outTradeNo", is("OOMALL23323338018")))
                .andExpect(jsonPath("$.userReceivedAccount", is("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o")))
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.amount.total", is(100)))
                .andExpect(jsonPath("$.amount.refund", is(50)));
    }

    @Test
    public void testGetRefundWhenRefundNotExist() throws Exception {

        this.mvc.perform(get(INTERNAL_V3_GET_REFUND_BY_OUTNO, "0")
                        .param("sub_mchid", "1900000109"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("查询的资源不存在")));
    }

    @Test
    public void testCreateDivPayTrans() throws Exception {

        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("T99732");
        vo.setSubMchid("86693852");
        vo.setOutOrderNo("OOMALL999888");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 40, "分给OOMALL"));
        }});

        this.mvc.perform(post(INTERNAL_V3_DIV_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpectAll(
                        jsonPath("$.transactionId", is("T99732")),
                        jsonPath("$.outOrderNo", is("OOMALL999888")),
                        jsonPath("$.orderId", startsWith("O")),
                        jsonPath("$.state", is("FINISHED")),
                        jsonPath("$.receivers[0].amount", is(40)),
                        jsonPath("$.receivers[0].description", is("分给OOMALL")),
                        jsonPath("$.receivers[0].type", is("MERCHANT_ID")),
                        jsonPath("$.receivers[0].account", is("1900000109")),
                        jsonPath("$.receivers[0].result", is("SUCCESS")),
                        jsonPath("$.receivers[0].detailId", startsWith("D"))
                );
    }

    @Test
    public void testCreateDivPayTransWhenPayTransNotExist() throws Exception {

        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("0");
        vo.setSubMchid("86693852");
        vo.setOutOrderNo("OOMALL999888");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 40, "分给OOMALL"));
        }});

        this.mvc.perform(post(INTERNAL_V3_DIV_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("查询的资源不存在")));
    }

    @Test
    public void testCreateDivPayTransWhenDuplicatedDivPayNo() throws Exception {

        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("T99732");
        vo.setSubMchid("86693852");
        vo.setOutOrderNo("OOMALL20150812125046");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 40, "分给OOMALL"));
        }});

        this.mvc.perform(post(INTERNAL_V3_DIV_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("商户分账单号重复")));
    }

    @Test
    public void testCreateDivPayTransWhenDivAmountNotEnough() throws Exception {

        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("T99732");
        vo.setSubMchid("86693852");
        vo.setOutOrderNo("OOMALL999888");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 90, "分给OOMALL"));
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 80, "分给OOMALL"));
        }});

        this.mvc.perform(post(INTERNAL_V3_DIV_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("分账金额不足")));
    }

    @Test
    public void testCreateDivRefundTrans() throws Exception {

        DivRefundTransVo vo = new DivRefundTransVo();
        vo.setSubMchid("1900008XXX");
        vo.setOutOrderNo("10");
        vo.setOutReturnNo("10");
        vo.setReturnMchid("1900007XXX");
        vo.setAmount(10);
        vo.setDescription("用户退款");

        this.mvc.perform(post(INTERNAL_V3_CANCEL_DIV_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpectAll(
                        jsonPath("$.subMchid", is("1900008XXX")),
                        jsonPath("$.orderId", is("10")),
                        jsonPath("$.outOrderNo", is("10")),
                        jsonPath("$.outReturnNo", is("10")),
                        jsonPath("$.returnId",startsWith("RT")),
                        jsonPath("$.returnMchid", is("1900007XXX")),
                        jsonPath("$.amount", is(10)),
                        jsonPath("$.result", is("SUCCESS"))
                );
    }

    @Test
    public void testCreateDivRefundTransWhenDuplicatedOutReturnNo() throws Exception {

        DivRefundTransVo vo = new DivRefundTransVo();
        vo.setSubMchid("86693852");
        vo.setOutOrderNo("OOMALL20150812125046");
        vo.setOutReturnNo("OOMALL20190516001");
        vo.setReturnMchid("1900000109");
        vo.setAmount(10);
        vo.setDescription("用户退款");

        this.mvc.perform(post(INTERNAL_V3_CANCEL_DIV_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("商户回退单号重复")));
    }

    @Test
    public void testCreateDivRefundTransWhenDivPayTransNotExist() throws Exception {

        DivRefundTransVo vo = new DivRefundTransVo();
        vo.setSubMchid("86693852");
        vo.setOutOrderNo("0");
        vo.setOutReturnNo("OOMALL1234");
        vo.setReturnMchid("1900000109");
        vo.setAmount(10);
        vo.setDescription("用户退款");

        this.mvc.perform(post(INTERNAL_V3_CANCEL_DIV_PAY)
                        .contentType("application/json;charset=UTF-8")
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errmsg", is("查询的资源不存在")));
    }
}
