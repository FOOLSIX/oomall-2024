package cn.edu.xmu.oomall.wechatpay.dao;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = WeChatPayApplication.class)
@AutoConfigureMockMvc
@Transactional
public class RefundDaoTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RefundDao refundDao;

    @Test
    public void testGetRefundsByOutTradeNo() {
        refundDao.getRefundsByOutTradeNo("OOMALL23323338018");
    }
}
