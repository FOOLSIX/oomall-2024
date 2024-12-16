package cn.edu.xmu.oomall.wechatpay.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.controller.vo.PayTransVo;
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
public class PaymentDaoTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PaymentDao paymentDao;

    @Test
    public void testUpdatePayTransWhenResourceNotExists() {
        PayTransVo vo = new PayTransVo();
        vo.setSpAppid("wx8888888888888888");
        vo.setSpMchid("1230000109");
        vo.setSubMchid("1900000109");
        vo.setDescription("OOMALL-上海厦带-QQ公仔");
        vo.setAmount(new PayTransVo.PayAmount(100));
        vo.setPayer(new PayTransVo.Payer("oUpF8123AJO_M2pxb1Q9zNjWeS6o"));
        vo.setNotifyUrl("/notify/payments/wepay");
        vo.setOutTradeNo("OOMALL123456");
        vo.setTimeExpire(LocalDateTime.now().plusMinutes(30));

        assertThrows(BusinessException.class, () -> paymentDao.updatePayTrans(new PayTrans(vo)));
    }
}
