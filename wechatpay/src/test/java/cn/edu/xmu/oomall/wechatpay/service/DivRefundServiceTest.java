package cn.edu.xmu.oomall.wechatpay.service;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.controller.vo.*;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivRefundTrans;
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
public class DivRefundServiceTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private DivRefundService divRefundService;

    @Test
    public void testCreateDivRefundWhenReceiverEmpty() {
        DivRefundTransVo vo = new DivRefundTransVo();
        vo.setSubMchid("1900008XXX");
        vo.setOutOrderNo("22");
        vo.setOutReturnNo("22");
        vo.setReturnMchid("1900007XXX");
        vo.setAmount(10);
        vo.setDescription("用户退款");

        assertThrows(WeChatPayException.class, () -> divRefundService.createDivRefund(new DivRefundTrans(vo)));
    }
}
