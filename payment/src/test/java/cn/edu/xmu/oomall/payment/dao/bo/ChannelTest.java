package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 2023-dgn1-006
 * @author huangzian
 */
@SpringBootTest(classes = PaymentApplication.class)
@Transactional(propagation = Propagation.REQUIRED)
public class ChannelTest {
    @Test
    void changeStatus()
    {
        Channel channel=new Channel();
        channel.setId(501L);
        channel.setStatus(Channel.VALID);
        UserDto userDto =new UserDto(1L,"admin123",0L,1);
        assertThrows(BusinessException.class,()->channel.valid(userDto));
    }
}
