package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 * @author ych
 * task 2023-dgn1-004
 */
@SpringBootTest(classes = PaymentApplication.class)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PayTransTest {

    /**
     * @author ych
     * task 2023-dgn1-004
     */
    @Test
    void testDivideGivenRightArgs() {
        Account account = new Account();
        account.setId(1L);
        //顺便检测一下构造函数
        PayTrans payTrans = new PayTrans(null,null,null,null,1L, account);
        payTrans.setStatus(PayTrans.NEW);
        assertThrows(BusinessException.class, payTrans::divide);
    }

    /**
     * @author ych
     * task 2023-dgn1-004
     */
    @Test
    void testAllowStatusWhenWrongToSuccess(){
        PayTrans payTrans = new PayTrans();
        payTrans.setStatus(PayTrans.WRONG);
        assertTrue( payTrans.allowStatus(PayTrans.SUCCESS));
    }
}
