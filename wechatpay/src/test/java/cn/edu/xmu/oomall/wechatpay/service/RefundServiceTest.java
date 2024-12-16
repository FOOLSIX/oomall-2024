package cn.edu.xmu.oomall.wechatpay.service;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.controller.vo.*;
import cn.edu.xmu.oomall.wechatpay.dao.bo.RefundTrans;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = WeChatPayApplication.class)
@AutoConfigureMockMvc
@Transactional
public class RefundServiceTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RefundService refundService;

    @Test
    public void testCreateRefundWhenRefundAmountError() {
        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T93232");
        vo.setAmount(new RefundAmountVo(0, 100));
        vo.setOutRefundNo("OOMALL12153368018");

        assertThrows(WeChatPayException.class, () -> refundService.createRefund(new RefundTrans(vo)));
    }

    @Test
    public void testCreateRefundWhenOutRefundNoUsed() {
        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T93232");
        vo.setAmount(new RefundAmountVo(100, 100));
        vo.setOutRefundNo("OOMALL12153368018");

        assertThrows(WeChatPayException.class, () -> refundService.createRefund(new RefundTrans(vo)));
    }

    @Test
    public void testCreateRefundWhenResourceNotExists() {
        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T01474");
        vo.setAmount(new RefundAmountVo(100, 100));
        vo.setOutRefundNo("OOMALL123456");

        assertThrows(WeChatPayException.class, () -> refundService.createRefund(new RefundTrans(vo)));
    }

    @Test
    public void testCreateRefundWhenOrderClosed() {
        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T91232");
        vo.setAmount(new RefundAmountVo(100, 100));
        vo.setOutRefundNo("OOMALL123456");

        assertThrows(WeChatPayException.class, () -> refundService.createRefund(new RefundTrans(vo)));
    }

    @Test
    public void testCreateRefundWhenOrderFail() {
        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T94432");
        vo.setAmount(new RefundAmountVo(100, 100));
        vo.setOutRefundNo("OOMALL123456");

        assertThrows(WeChatPayException.class, () -> refundService.createRefund(new RefundTrans(vo)));
    }

    @Test
    public void testCreateRefundWhenInconsistentAmount() {
        RefundTransVo vo = new RefundTransVo();
        vo.setSubMchid("1900000109");
        vo.setTransactionId("T99732");
        vo.setAmount(new RefundAmountVo(90, 90));
        vo.setOutRefundNo("OOMALL123456");

        assertThrows(WeChatPayException.class, () -> refundService.createRefund(new RefundTrans(vo)));
    }

    @Test
    public void testGetRefundWhenNoAuth() {
        assertThrows(WeChatPayException.class, () -> refundService.getRefund("1900000108","OOMALL12153368018"));
    }
}
