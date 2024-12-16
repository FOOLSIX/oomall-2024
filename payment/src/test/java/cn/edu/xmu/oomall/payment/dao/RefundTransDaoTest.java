package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Rui Li
 * task 2023-dgn1-005
 */
@SpringBootTest(classes = PaymentApplication.class)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class RefundTransDaoTest {

    @Autowired
    RefundTransDao refundTransDao;

    /**
     * 不存在该对象
     */
    @Test
    void testFindByIdWhenObjectNotExist() {
        assertThrows(BusinessException.class,()->refundTransDao.findById(0L, 9999L));
    }

    /**
     * 访问权限不足
     */
    @Test
    void testFindByIdGivenWrongToken() {
        assertThrows(BusinessException.class,()->refundTransDao.findById(2L, 611L));
    }

    /**
     * 更新对象类型错误
     */
    @Test
    void testSaveGivenWrongObjectType() {
        PayTrans payTrans = new PayTrans();
        payTrans.setId(12L);
        assertThrows(InvalidDataAccessApiUsageException.class,()->refundTransDao.save(payTrans));
    }

    /**
     * 插入对象类型错误
     */
    @Test
    void testInsertGivenWrongObjectType() {
        PayTrans payTrans = new PayTrans();
        payTrans.setId(12L);
        assertThrows(InvalidDataAccessApiUsageException.class,()->refundTransDao.insert(payTrans));
    }
}
