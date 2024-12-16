package cn.edu.xmu.oomall.wechatpay.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.controller.vo.*;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivPayTrans;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = WeChatPayApplication.class)
@AutoConfigureMockMvc
@Transactional
public class DivPaymentServiceTest {

    @Autowired
    private DivPaymentService divPaymentService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void testCreateDivPayTransWhenOrderClosed(){
        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("T91232");
        vo.setSubMchid("1900000109");
        vo.setOutOrderNo("OOMALL12132118018");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 10, "分给OOMALL"));
        }});

        assertThrows(WeChatPayException.class, ()->divPaymentService.createDivPayTrans(new DivPayTrans(vo)));
    }

    @Test
    public void testCreateDivPayTransWhenTradeStateFail(){
        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("T94432");
        vo.setSubMchid("1900000109");
        vo.setOutOrderNo("OOMALL91321453318");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 10, "分给OOMALL"));
        }});

        assertThrows(WeChatPayException.class, ()->divPaymentService.createDivPayTrans(new DivPayTrans(vo)));
    }

    @Test
    public void testCreateDivPayTransWhenNotEnough(){
        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("T99732");
        vo.setSubMchid("1900000109");
        vo.setOutOrderNo("OOMALL12177525012");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 200, "分给OOMALL"));
        }});

        assertThrows(WeChatPayException.class, ()->divPaymentService.createDivPayTrans(new DivPayTrans(vo)));
    }

    @Test
    public void testCreateDivPayTransWhenDivAmountError(){
        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("T99732");
        vo.setSubMchid("1900000109");
        vo.setOutOrderNo("OOMALL12177525012");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 0, "分给OOMALL"));
        }});

        assertThrows(WeChatPayException.class, ()->divPaymentService.createDivPayTrans(new DivPayTrans(vo)));
    }

    @Test
    public void testCreateDivPayTransWhenNotDiv(){
        DivPayTransVo vo = new DivPayTransVo();
        vo.setAppid("wx8888888888888888");
        vo.setTransactionId("T95821");
        vo.setSubMchid("1900000109");
        vo.setOutOrderNo("OOMALL91123453318");
        vo.setUnfreezeUnsplit(true);
        vo.setReceivers(new ArrayList<>() {{
            add(new DivReceiverVo("MERCHANT_ID", "1900000109", 10, "分给OOMALL"));
        }});

        divPaymentService.createDivPayTrans(new DivPayTrans(vo));
    }
}
