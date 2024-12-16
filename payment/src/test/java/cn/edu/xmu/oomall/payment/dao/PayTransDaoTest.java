package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author ych
 * task 2023-dgn1-004
 */
@SpringBootTest(classes = PaymentApplication.class)
public class PayTransDaoTest {

    @MockBean
    RedisUtil redisUtil;
    @Autowired
    private PayTransDao payTransDao;


    /**
     * @author ych
     * task 2023-dgn1-004
     * po为空
     */
    @Test
    public void testFindByIdWhenPoIsNull()
    {
        BusinessException ex = new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "支付交易", 111L));
        try {
            payTransDao.findById(1L,111L);
        }catch (BusinessException e) {
            assertEquals(e.getMessage(),ex.getMessage());
        }
    }

    /**
     * @author ych
     * task 2023-dgn1-004
     * 访问超权限
     */
    @Test
    public void testFindByIdWhenAuthorityWrong()
    {
        BusinessException ex = new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "支付交易", 551L, 2L));
        try {
            payTransDao.findById(2L,551L);
        }catch (BusinessException e) {
            assertEquals(e.getMessage(),ex.getMessage());
        }
    }

    /**
     * @author ych
     * task 2023-dgn1-004
     * 查找成功
     */
    @Test
    public void testFindByIdWhenSuccess()
    {
        PayTrans payTrans = payTransDao.findById(1L,551L);
        assertEquals(payTrans.getTransNo(),"12222");
        assertEquals(payTrans.getAccountId(),501L);
    }

    /**
     * @author ych
     * task 2023-dgn1-004
     * transNo等参数为空
     */
    @Test
    public void testRetrieveByAccountIdWhenArgsNull()
    {
        List<Long> accountId = new ArrayList<>();
        accountId.add(501L);
        accountId.add(528L);
        List<PayTrans> payTrans = payTransDao.retrieveByAccountId(accountId,null,null,null,null,1,10);
        assertNotNull(payTrans);
    }

    /**
     * @author ych
     * task 2023-dgn1-004
     * transNo等参数不为空
     */
    @Test
    public void testRetrieveByAccountIdWhenArgsNotNull()
    {
        List<Long> accountId = new ArrayList<>();
        accountId.add(501L);
        List<PayTrans> payTrans = payTransDao.retrieveByAccountId(accountId,"12222",7,null,null,1,10);
        assertNotNull(payTrans);
    }

    /**
     * @author ych
     * task 2023-dgn1-004
     */
    @Test
    public void testRetrieveByChannelIdGivenRightArgs()
    {
        String beginTime = "2023-11-30 06:43:27";
        String endTime = "2022-12-15 14:29:57";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginLocalDateTime = LocalDateTime.parse(beginTime, formatter);
        LocalDateTime endLocalDateTime = LocalDateTime.parse(endTime, formatter);

        List<PayTrans> payTrans = payTransDao.retrieveByChannelId(501L,"2111",1,beginLocalDateTime,endLocalDateTime,1,10);
        assertNotNull(payTrans);
    }

    /**
     * @author ych
     * task 2023-dgn1-004
     * 成功找到
     */
    @Test
    public void testFindByOutNoGivenRightArgs()
    {
        Optional<PayTrans> payTrans = payTransDao.findByOutNo("1603");
        assertTrue(payTrans.isPresent());
    }

}
