package cn.edu.xmu.oomall.customer.controller.Vo;

import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.customer.controller.vo.AddressVo;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AddressVoTest {

    private Address address;
    private AddressVo addressVo;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setId(1L);
        address.setRegion_id(100L);
        address.setAddress("123 Main St");
        address.setConsignee("John Doe");
        address.setMobile("1234567890");
        address.setBe_default(true);
        address.setCreatorId(2L);
        address.setCreatorName("Admin");
        address.setModifierId(3L);
        address.setModifierName("Editor");
        address.setGmtCreate(LocalDateTime.of(2023, 12, 25, 10, 30));
        address.setGmtModified(LocalDateTime.of(2023, 12, 26, 12, 15));

        addressVo = new AddressVo(address);
    }

    @Test
    void testConstructor() {
        assertEquals(1L, addressVo.getId());
        assertEquals(100L, addressVo.getRegion_id());
        assertEquals("123 Main St", addressVo.getAddress());
        assertEquals("John Doe", addressVo.getConsignee());
        assertEquals("1234567890", addressVo.getMobile());
        assertTrue(addressVo.getBe_default());
        assertEquals("Admin", addressVo.getCreator(null).getName());
        assertEquals("Editor", addressVo.getModifier(null).getName());
        assertEquals(LocalDateTime.of(2023, 12, 25, 10, 30), addressVo.getGmtCreate());
        assertEquals(LocalDateTime.of(2023, 12, 26, 12, 15), addressVo.getGmtModified());
    }

    @Test
    void testSettersAndGetters() {
        addressVo.setId(2L);
        assertEquals(2L, addressVo.getId());

        addressVo.setRegion_id(200L);
        assertEquals(200L, addressVo.getRegion_id());

        addressVo.setAddress("456 Elm St");
        assertEquals("456 Elm St", addressVo.getAddress());

        addressVo.setConsignee("Jane Doe");
        assertEquals("Jane Doe", addressVo.getConsignee());

        addressVo.setMobile("0987654321");
        assertEquals("0987654321", addressVo.getMobile());

        addressVo.setGmtModified(LocalDateTime.of(2023, 12, 27, 14, 45));
        assertEquals(LocalDateTime.of(2023, 12, 27, 14, 45), addressVo.getGmtModified());
    }

    @Test
    void testChangeBeDefault() {
        assertTrue(addressVo.getBe_default());
        addressVo.changeBe_default();
        assertFalse(addressVo.getBe_default());
        addressVo.changeBe_default();
        assertTrue(addressVo.getBe_default());
    }

    @Test
    void testCreatorRegionModifier() {
        IdNameTypeVo newCreator = IdNameTypeVo.builder().id(10L).name("New Admin").build();
        addressVo.setCreator(newCreator);
        assertEquals(newCreator, addressVo.getCreator(null));

        IdNameTypeVo newRegion = IdNameTypeVo.builder().id(20L).name("New Region").build();
        addressVo.setRegion(newRegion);
        assertEquals(newRegion, addressVo.getRegion(null));

        IdNameTypeVo newModifier = IdNameTypeVo.builder().id(30L).name("New Editor").build();
        addressVo.setModifier(newModifier);
        assertEquals(newModifier, addressVo.getModifier(null));
    }
}

