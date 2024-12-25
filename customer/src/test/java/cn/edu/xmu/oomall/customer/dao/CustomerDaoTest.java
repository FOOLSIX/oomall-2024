package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.mapper.jpa.CustomerPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.*;

public class CustomerDaoTest {

    @Mock
    private CustomerPoMapper customerPoMapper;

    @InjectMocks
    private CustomerDao customerDao;

    private CustomerPo customerPo;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerPo = new CustomerPo();
        customerPo.setId(1L);
        customerPo.setName("Test Customer");

        customer = customerDao.build(customerPo);
    }

    @Test
    void testFindById_ValidId() {
        when(customerPoMapper.findById(1L)).thenReturn(Optional.of(customerPo));

        Customer result = customerDao.findById(1L);

        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
        verify(customerPoMapper, times(1)).findById(1L);
    }

    @Test
    void testFindById_InvalidId() {
        Long invalidId = -1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerDao.findById(invalidId);
        });

        assertEquals("findById: id is null", exception.getMessage(), "异常消息不符合预期");

        verify(customerPoMapper, times(0)).findById(invalidId);
    }

    @Test
    void testSave_CustomerIdIsNull_ThrowsException() {
        // 创建无效的 Customer 对象（id 为 null）
        Customer invalidCustomer = new Customer();

        // 模拟 UserDto
        UserDto userDto = new UserDto();

        // 执行方法并验证异常
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerDao.save(invalidCustomer, userDto);
        });

        assertEquals("save: customer id is null", exception.getMessage(), "异常消息不匹配");

        // 验证 customerPoMapper.save() 没有被调用
        verify(customerPoMapper, never()).save(any(CustomerPo.class));
    }

    @Test
    void testSave_ValidCustomer_Success() {
        // 创建有效的 Customer 对象
        Customer validCustomer = new Customer();
        validCustomer.setId(1L); // 设置有效 ID
        validCustomer.setName("Test Customer");

        // 模拟 UserDto
        UserDto userDto = new UserDto();
        userDto.setId(777L);

        // 模拟 CloneFactory 行为
        CustomerPo mockedCustomerPo = new CustomerPo();
        when(CloneFactory.copy(any(CustomerPo.class), eq(validCustomer))).thenReturn(mockedCustomerPo);

        // 对 customerPoMapper.save() 使用 doNothing()
        doNothing().when(customerPoMapper).save(any(CustomerPo.class));

        // 调用待测试方法
        customerDao.save(validCustomer, userDto);

        // 验证 customerPoMapper.save() 被正确调用
        verify(customerPoMapper, times(1)).save(mockedCustomerPo);

        // 捕获生成的 CustomerPo 并验证属性
        ArgumentCaptor<CustomerPo> captor = ArgumentCaptor.forClass(CustomerPo.class);
        verify(customerPoMapper).save(captor.capture());
        CustomerPo captured = captor.getValue();

        assertEquals("Test Customer", captured.getName(), "保存的名称与输入不符");
        assertNotNull(captured.getGmtModified(), "修改时间不应为空");
        assertEquals(777L, captured.getModifierId(), "修改人信息不符");
    }



    @Test
    void testSave_NullId() {
        customer.setId(null);

        assertThrows(IllegalArgumentException.class, () -> customerDao.save(customer, new UserDto()));
    }

    @Test
    void testGetAllUsers() {
        int page = 1;
        int pageSize = 10;

        // 创建模拟的 CustomerPo 对象
        CustomerPo customerPo1 = new CustomerPo();
        customerPo1.setId(1L);
        customerPo1.setName("Test Customer 1");

        CustomerPo customerPo2 = new CustomerPo();
        customerPo2.setId(2L);
        customerPo2.setName("Test Customer 2");

        List<CustomerPo> customerPoList = Arrays.asList(customerPo1, customerPo2);

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id");

        when(customerPoMapper.findAll(pageable)).thenReturn(new PageImpl<>(customerPoList, pageable, customerPoList.size()));

        List<Customer> result = customerDao.getAllUsers(page, pageSize);

        assertNotNull(result, "用户列表不该为空.");
        assertEquals(2, result.size(), "返回结果的大小与模拟不一致");

        assertEquals(1L, result.get(0).getId(), "第一个用户与mock不符");
        assertEquals("Test Customer 1", result.get(0).getName(), "第一个用户名字与mock不符");

        assertEquals(2L, result.get(1).getId(), "第二个用户与mock不符");
        assertEquals("Test Customer 2", result.get(1).getName(), "第二个用户名字与mock不符");

        verify(customerPoMapper, times(1)).findAll(pageable);

        when(customerPoMapper.findAll(pageable)).thenReturn(Page.empty());

        result = customerDao.getAllUsers(page, pageSize);

        assertNotNull(result, "结果应该是空列表而不是空值");
        assertTrue(result.isEmpty(), "列表不为空");

        verify(customerPoMapper, times(2)).findAll(pageable);
    }

}