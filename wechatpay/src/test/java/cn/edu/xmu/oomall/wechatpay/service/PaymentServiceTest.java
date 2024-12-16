package cn.edu.xmu.oomall.wechatpay.service;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.controller.vo.*;
import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = WeChatPayApplication.class)
@AutoConfigureMockMvc
@Transactional
public class PaymentServiceTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PaymentService paymentService;

    @Test
    public void testCreatePayTransWhenOrderClosed() {
        PayTransVo vo = new PayTransVo();
        vo.setSpAppid("wx8888888888888888");
        vo.setSpMchid("1230000109");
        vo.setSubMchid("1900000109");
        vo.setDescription("OOMALL-上海厦带-QQ公仔");
        vo.setAmount(new PayTransVo.PayAmount(100));
        vo.setPayer(new PayTransVo.Payer("oUpF8123AJO_M2pxb1Q9zNjWeS6o"));
        vo.setNotifyUrl("/notify/payments/wepay");
        vo.setOutTradeNo("OOMALL12132118018");
        vo.setTimeExpire(LocalDateTime.now().plusMinutes(30));

        assertThrows(WeChatPayException.class, () -> paymentService.createPayTrans(new PayTrans(vo)));
    }

    @Test
    public void testClosePayTransWhenOrderClosed() {
        assertThrows(WeChatPayException.class, () -> paymentService.closePayTrans("1230000109","1900000109","OOMALL12132118018"));
    }

    @Test
    public void testClosePayTransWhenOrderPaid() {
        assertThrows(WeChatPayException.class, () -> paymentService.closePayTrans("1230000109","1900000109","OOMALL12177525012"));
    }

    @Test
    public void testClosePayTransWhenNoAuth() {
        assertThrows(WeChatPayException.class, () -> paymentService.closePayTrans("1230000109","1900000108","OOMALL91321453318"));
    }

    @Test
    public void testGetPayTransByOutNoWhenNoAuth() {
        assertThrows(WeChatPayException.class, () -> paymentService.getPayTransByOutNo("1230000109","1900000108","OOMALL91321453318"));
    }

    @Test
    public void testGetPayTransByTransIdWhenResourceNotExists() {
        assertThrows(WeChatPayException.class, () -> paymentService.getPayTransByTransId("1230000109","1900000109","OOMALL123456"));
    }

    @Test
    public void testGetPayTransByTransIdWhenNoAuth() {
        assertThrows(WeChatPayException.class, () -> paymentService.getPayTransByTransId("1230000109","1900000108","T99732"));
    }
}
