package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private UserDto userDto;

    @InjectMocks
    private Customer customer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // 初始化Mock对象
        customer = new Customer();
        customer.setId(1L);
    }

//    @Test
//    public void testBanUserWhenNotBanned() {
//
//        customer.setInvalid((byte) 0);
//
//        doNothing().when(customerDao).save(any(Customer.class), eq(userDto));
//
//        customer.banUser(userDto);
//
//        // 验证customerDao.save是否被调用
//        verify(customerDao, times(1)).save(any(Customer.class), eq(userDto));
//
//        assertEquals((byte) 1, customer.getInvalid());
//    }

    @Test
    public void testBanUserWhenAlreadyBanned() {

        customer.setInvalid((byte) 1);

        assertThrows(BusinessException.class, () -> customer.banUser(userDto));
    }

//    @Test
//    public void testReleaseUserWhenBanned() {
//        customer.setInvalid((byte) 1);
//
//        doNothing().when(customerDao).save(any(Customer.class), eq(userDto));
//
//        customer.releaseUser(userDto);
//
//        verify(customerDao, times(1)).save(any(Customer.class), eq(userDto));
//
//        assertEquals((byte)0, customer.getInvalid());
//    }

    @Test
    public void testReleaseUserWhenNotBanned() {

        customer.setInvalid((byte) 0);

        assertThrows(BusinessException.class, () -> customer.releaseUser(userDto));
    }

//    @Test
//    public void testDelUserByIdWhenNotBanned() {
//        customer.setInvalid((byte) 0);
//
//        doNothing().when(customerDao).save(any(Customer.class), eq(userDto));
//
//        customer.delUserById(userDto);
//
//        verify(customerDao, times(1)).save(any(Customer.class), eq(userDto));
//
//        assertEquals((byte)2, customer.getInvalid());
//    }

    @Test
    public void testDelUserByIdWhenBanned() {
        customer.setInvalid((byte) 1);

        assertThrows(BusinessException.class, () -> customer.delUserById(userDto));
    }

//    @Test
//    public void testChangeInvalid() {
//        when(userDto.getDepartId()).thenReturn(10L);
//
//        customer.changeInvalid((byte) 1, userDto);
//
//        verify(customerDao, times(1)).save(any(Customer.class), eq(userDto));
//    }
}
