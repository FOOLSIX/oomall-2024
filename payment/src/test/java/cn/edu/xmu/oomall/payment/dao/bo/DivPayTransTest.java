package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import cn.edu.xmu.oomall.payment.dao.PayTransDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * @author ych
 * task 2023-dgn1-004
 */
@SpringBootTest(classes = PaymentApplication.class)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class DivPayTransTest {
    @Autowired
    private PayTransDao payTransDao;
    @MockBean
    private RedisUtil redisUtil;

    /**
     * @author ych
     * task 2023-dgn1-004
     */
    @Test
    public void testGetTransGivenRightArgs()
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        DivPayTrans divPayTrans = new DivPayTrans();
        divPayTrans.setShopId(1L);
        divPayTrans.setPayTransId(551L);
        divPayTrans.setAccountId(501L);
        divPayTrans.setPayTransDao(payTransDao);
        PayTrans payTrans = divPayTrans.getTrans();
        assertEquals(payTrans.getAmount(),100L);
    }

}
