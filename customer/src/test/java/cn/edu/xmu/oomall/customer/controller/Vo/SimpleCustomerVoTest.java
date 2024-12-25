package cn.edu.xmu.oomall.customer.controller.Vo;

import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cn.edu.xmu.oomall.customer.controller.vo.SimpleCustomerVo;
import org.mockito.Mockito;

public class SimpleCustomerVoTest {

    @Test
    public void testConstructor() {
        Customer customer = Mockito.mock(Customer.class);

        Mockito.when(customer.getId()).thenReturn(1L);
        Mockito.when(customer.getName()).thenReturn("John Doe");

        // 调用构造函数
        SimpleCustomerVo simpleCustomerVo = new SimpleCustomerVo(customer);

        // 验证字段是否被正确复制
        assertEquals(1L, simpleCustomerVo.getId());
        assertEquals("John Doe", simpleCustomerVo.getName());

    }

    @Test
    public void testNullCustomer() {
        // 测试传入 null 是否抛出异常
        assertThrows(NullPointerException.class, () -> new SimpleCustomerVo(null));
    }

}

