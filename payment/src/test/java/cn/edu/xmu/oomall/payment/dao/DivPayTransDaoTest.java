package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * @author ych
 * task 2023-dgn1-004
 */
@SpringBootTest(classes = PaymentApplication.class)
public class DivPayTransDaoTest {
    @Autowired
    private DivPayTransDao divPayTransDao;

    @Test
    public void testFindByIdWhenShopIdIsNull()
    {
        assertThrows(BusinessException.class,()->divPayTransDao.findById(null,1L));
    }

    @Test
    public void testFindByIdGivenWrongId()
    {
        assertThrows(BusinessException.class,()->divPayTransDao.findById(1L,0L));
    }

    @Test
    public void testFindByIdGivenWrongAuthority()
    {
        assertThrows(BusinessException.class,()->divPayTransDao.findById(2L,1L));
    }
}
