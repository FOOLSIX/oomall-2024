package cn.edu.xmu.oomall.customer.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressDtoTest {

    @Test
    void testValidAddressDto() {
        // 创建一个有效的 AddressDto
        AddressDto addressDto = new AddressDto();
        addressDto.setRegion_id(1001L);
        addressDto.setAddress("123 Main St");
        addressDto.setConsignee("John Doe");
        addressDto.setMobile("1234567890");

        // 手动验证字段
        assertNotNull(addressDto.getRegion_id(), "region_id 不能为空");
        assertEquals(1001L, addressDto.getRegion_id());
        assertEquals("123 Main St", addressDto.getAddress());
        assertEquals("John Doe", addressDto.getConsignee());
        assertEquals("1234567890", addressDto.getMobile());
    }

    @Test
    void testInvalidRegionId() {
        // 创建一个无效的 AddressDto（region_id 为空）
        AddressDto addressDto = new AddressDto();
        addressDto.setRegion_id(null);
        addressDto.setAddress("123 Main St");
        addressDto.setConsignee("John Doe");
        addressDto.setMobile("1234567890");

        // 手动验证字段
        assertNull(addressDto.getRegion_id(), "region_id 应为 null");
    }

    @Test
    void testEmptyFields() {
        // 创建一个空的 AddressDto
        AddressDto addressDto = new AddressDto();

        // 验证 region_id 是否为 null
        assertNull(addressDto.getRegion_id(), "region_id 不能为空");
        assertNull(addressDto.getAddress(), "address 不能为空");
        assertNull(addressDto.getConsignee(), "consignee 不能为空");
        assertNull(addressDto.getMobile(), "mobile 不能为空");
    }

    @Test
    void testSettersAndGetters() {
        // 创建一个 AddressDto 并设置值
        AddressDto addressDto = new AddressDto();
        addressDto.setRegion_id(1001L);
        addressDto.setAddress("456 Elm St");
        addressDto.setConsignee("Jane Doe");
        addressDto.setMobile("9876543210");

        // 验证 getter 是否正确
        assertEquals(1001L, addressDto.getRegion_id());
        assertEquals("456 Elm St", addressDto.getAddress());
        assertEquals("Jane Doe", addressDto.getConsignee());
        assertEquals("9876543210", addressDto.getMobile());
    }
}


