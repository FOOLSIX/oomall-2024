package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试 getUserById 方法
     */
    @Test
    public void testGetUserById() {
        // 模拟数据
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setName("John Doe");

        when(customerDao.findById(1L)).thenReturn(mockCustomer);

        // 测试方法
        Customer result = customerService.getUserById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());

        // 验证交互
        verify(customerDao, times(1)).findById(1L);
    }

    /**
     * 测试 getAllUsers 方法
     */
    @Test
    public void testGetAllUsers() {
        // 模拟数据
        List<Customer> mockCustomers = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");

        mockCustomers.add(customer1);
        mockCustomers.add(customer2);

        when(customerDao.getAllUsers(1, 10)).thenReturn(mockCustomers);

        // 测试方法
        List<Customer> result = customerService.getAllUsers(1, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());

        // 验证交互
        verify(customerDao, times(1)).getAllUsers(1, 10);
    }

    /**
     * 测试 banUser 方法
     */
    @Test
    public void testBanUser() {
        // 模拟数据
        Customer mockCustomer = mock(Customer.class);
        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(1L);
        mockUserDto.setName("Admin");

        when(customerDao.findById(1L)).thenReturn(mockCustomer);

        // 测试方法
        customerService.banUser(1L, mockUserDto);

        // 验证交互
        verify(mockCustomer, times(1)).banUser(mockUserDto);
        verify(customerDao, times(1)).findById(1L);
    }

    /**
     * 测试 releaseUser 方法
     */
    @Test
    public void testReleaseUser() {
        // 模拟数据
        Customer mockCustomer = mock(Customer.class);
        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(1L);
        mockUserDto.setName("Admin");

        when(customerDao.findById(1L)).thenReturn(mockCustomer);

        // 测试方法
        customerService.releaseUser(1L, mockUserDto);

        // 验证交互
        verify(mockCustomer, times(1)).releaseUser(mockUserDto);
        verify(customerDao, times(1)).findById(1L);
    }

    /**
     * 测试 delUserById 方法
     */
    @Test
    public void testDelUserById() {
        // 模拟数据
        Customer mockCustomer = mock(Customer.class);
        UserDto mockUserDto = new UserDto();
        mockUserDto.setId(1L);
        mockUserDto.setName("Admin");

        when(customerDao.findById(1L)).thenReturn(mockCustomer);

        // 测试方法
        customerService.delUserById(1L, mockUserDto);

        // 验证交互
        verify(mockCustomer, times(1)).delUserById(mockUserDto);
        verify(customerDao, times(1)).findById(1L);
    }
}

