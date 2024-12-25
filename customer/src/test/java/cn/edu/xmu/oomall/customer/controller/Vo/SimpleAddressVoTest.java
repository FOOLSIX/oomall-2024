package cn.edu.xmu.oomall.customer.controller.Vo;

import cn.edu.xmu.oomall.customer.controller.vo.SimpleAddressVo;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimpleAddressVoTest {

    @Test
    void testSimpleAddressVoConstruction() {
        // 创建 Address 的 mock 对象
        Address mockAddress = mock(Address.class);

        // 设置 mock 对象的返回值
        when(mockAddress.getRegion_id()).thenReturn(1001L);
        when(mockAddress.getAddress()).thenReturn("123 Main St");
        when(mockAddress.getConsignee()).thenReturn("John Doe");
        when(mockAddress.getMobile()).thenReturn("1234567890");
        when(mockAddress.getBe_default()).thenReturn(true);

        // 使用 mock 对象创建 SimpleAddressVo
        SimpleAddressVo simpleAddressVo = new SimpleAddressVo(mockAddress);

        // 验证字段是否正确复制
        assertEquals(1001L, simpleAddressVo.getRegion_id());
        assertEquals("123 Main St", simpleAddressVo.getAddress());
        assertEquals("John Doe", simpleAddressVo.getConsignee());
        assertEquals("1234567890", simpleAddressVo.getMobile());
        assertTrue(simpleAddressVo.getBe_default());
    }

    @Test
    void testSimpleAddressVoWithNullAddress() {
        // 测试 null 输入时的行为
        Address nullAddress = null;

        // 确保构造方法能够正确处理空对象
        assertThrows(NullPointerException.class, () -> new SimpleAddressVo(nullAddress));
    }
}

