package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
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
public class AccountTest
{
    /**
     * @author ych
     * task 2023-dgn1-004
     */
    @Test
    public void testAllowStatusWhenValidToInvalid()
    {
        Account account = new Account();
        account.setStatus(Account.VALID);
        assertTrue(account.allowStatus(Account.INVALID));
    }

    /**
     * @author ych
     * task 2023-dgn1-004
     */
    @Test
    public void testCreatePaymentGivenRightArgs()
    {
        PayTrans payTrans = new PayTrans();
        UserDto userDto = new UserDto();
        Account account = new Account();
        account.setStatus(Account.INVALID);
        assertThrows(NullPointerException.class, () -> {
            account.createPayment(payTrans, userDto);
        });
    }
}
