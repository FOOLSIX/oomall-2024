package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.PaymentApplication;
import cn.edu.xmu.oomall.payment.dao.bo.Channel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 2023-dgn1-006
 * @author huangzian
 */
@SpringBootTest(classes = PaymentApplication.class)
@Transactional(propagation = Propagation.REQUIRED)
public class ChannelDaoTest {
    @MockBean
    RedisUtil redisUtil;

    @Autowired
    private ChannelDao channelDao;
    /**
     * @author huangzian
     * 2023-dgn1-006
     * 数据库资源不存在
     */
    @Test
    public void testFindByIdWhenDataBaseHasNoResource()
    {
        Long id=499L;
        assertThrows(BusinessException.class,()->channelDao.findById(id));
    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * rids有效
     */
    @Test
    public void testFindByIdWhenReidsHasKey()
    {
        Channel channel=new Channel();
        channel.setId(501L);
        channel.setName("微信支付");
        channel.setChannelDao(this.channelDao);
        channel.setBeanName("wePayChannel");
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(true);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(channel);
        assertEquals(channel.getName(),channelDao.findById(501L).getName());

    }
    /**
     * @author huangzian
     * 2023-dgn1-006
     * id超出范围
     */
    @Test
    public void save()
    {
        Long id=499L;
        Channel channel=new Channel();
        channel.setId(id);
        UserDto userDto =new UserDto(1L,"admin123",0L,1);
        assertThrows(BusinessException.class,()->channelDao.save(channel, userDto));
    }
}
