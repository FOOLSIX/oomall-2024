package cn.edu.xmu.oomall.customer.controller.Vo;

import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.controller.vo.CustomerVo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

class CustomerVoTest {

    @Test
    void testCustomerVoConstruction() {
        // 创建 Customer 的 mock 对象
        Customer mockCustomer = mock(Customer.class);

        // 设置 mock 对象的返回值
        when(mockCustomer.getId()).thenReturn(1L);
        when(mockCustomer.getUserName()).thenReturn("testUser");
        when(mockCustomer.getName()).thenReturn("Test Name");
        when(mockCustomer.getCreatorId()).thenReturn(101L);
        when(mockCustomer.getCreatorName()).thenReturn("Creator Name");
        when(mockCustomer.getModifierId()).thenReturn(102L);
        when(mockCustomer.getModifierName()).thenReturn("Modifier Name");
        when(mockCustomer.getGmtCreate()).thenReturn(LocalDateTime.of(2023, 12, 25, 10, 30));
        when(mockCustomer.getGmtModified()).thenReturn(LocalDateTime.of(2023, 12, 26, 11, 45));

        // 使用 mock 对象创建 CustomerVo
        CustomerVo customerVo = new CustomerVo(mockCustomer);

        // 验证字段是否正确复制
        assertEquals(1L, customerVo.getId());
        assertEquals("testUser", customerVo.getUserName());
        assertEquals("Test Name", customerVo.getName());

        assertNotNull(customerVo.getCreator());
        assertEquals(101L, customerVo.getCreator().getId());
        assertEquals("Creator Name", customerVo.getCreator().getName());

        assertNotNull(customerVo.getModifier());
        assertEquals(102L, customerVo.getModifier().getId());
        assertEquals("Modifier Name", customerVo.getModifier().getName());

        assertEquals(LocalDateTime.of(2023, 12, 25, 10, 30), customerVo.getGmtCreate());
        assertEquals(LocalDateTime.of(2023, 12, 26, 11, 45), customerVo.getGmtModified());
    }

    @Test
    void testCustomerVoWithNullCustomer() {
        // 测试 null 输入时的行为
        Customer nullCustomer = null;

        // 确保构造方法能够正确处理空对象
        assertThrows(NullPointerException.class, () -> new CustomerVo(nullCustomer));
    }
}

