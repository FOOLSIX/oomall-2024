package cn.edu.xmu.oomall.customer.dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.mapper.jpa.AddressPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Collections;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AddressDaoTest {

    @Mock
    private AddressPoMapper addressPoMapper;

    @InjectMocks
    private AddressDao addressDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Long validId = 1L;
        AddressPo addressPo = new AddressPo();
        addressPo.setId(validId);

        when(addressPoMapper.findById(validId)).thenReturn(Optional.of(addressPo));

        Address result = addressDao.findById(validId);
        assertNotNull(result);
        assertEquals(validId, result.getId());
        verify(addressPoMapper, times(1)).findById(validId);

        try {
            addressDao.findById(null);
        } catch (BusinessException e) {
            assertEquals(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo(), e.getErrno().getErrNo());
            assertEquals("id is null", e.getMessage());
        }

        verify(addressPoMapper, never()).findById(null);
    }



    @Test
    void testFindByIdNotFound() {
        Long id = 1L;
        when(addressPoMapper.findById(id)).thenReturn(Optional.empty());

        Address result = addressDao.findById(id);

        assertNull(result);
        verify(addressPoMapper, times(1)).findById(id);
    }

    @Test
    void testGetAllAddress() {
        Long customerId = 1L;
        int page = 1;
        int pageSize = 10;

        AddressPo addressPo1 = new AddressPo();
        addressPo1.setId(1L);
        AddressPo addressPo2 = new AddressPo();
        addressPo2.setId(2L);
        List<AddressPo> addressPoList = Arrays.asList(addressPo1, addressPo2);

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id");
        when(addressPoMapper.findBycustomer_id(customerId, pageable)).thenReturn(addressPoList);

        List<Address> result = addressDao.getAlladdress(customerId, page, pageSize);

        assertNotNull(result, "地址列表不该为空.");
        assertEquals(2, result.size(), "返回结果的大小与模拟不一致");
        assertEquals(1L, result.get(0).getId(), "第一个地址与mock不符");
        assertEquals(2L, result.get(1).getId(), "第二个地址与mock不符");

        verify(addressPoMapper, times(1)).findBycustomer_id(customerId, pageable);

        when(addressPoMapper.findBycustomer_id(customerId, pageable)).thenReturn(Collections.emptyList());

        result = addressDao.getAlladdress(customerId, page, pageSize);

        assertNotNull(result, "结果应该是空列表而不是空值");
        assertTrue(result.isEmpty(), "列表不为空");

        verify(addressPoMapper, times(2)).findBycustomer_id(customerId, pageable);
    }

    @Test
    void testSaveAddress() {
        Address address = new Address();
        address.setId(1L);
        AddressPo addressPo = new AddressPo();
        addressPo.setId(1L);

        when(addressPoMapper.save(any(AddressPo.class))).thenReturn(addressPo);

        addressDao.SaveAddress(address);

        verify(addressPoMapper, times(1)).save(any(AddressPo.class));
    }

    @Test
    void testDeleteAddress() {
        Long id = 1L;

        addressDao.DeleteAddress(id);

        verify(addressPoMapper, times(1)).deleteById(id);
    }

    @Test
    void testSetDefault() {
        Long id = 1L;

        AddressPo currentDefault = new AddressPo();
        currentDefault.setId(2L);
        currentDefault.setBe_default(true);

        AddressPo newDefault = new AddressPo();
        newDefault.setId(id);

        when(addressPoMapper.findById(id)).thenReturn(Optional.of(newDefault));
        when(addressPoMapper.findBycustomer_id(id, Boolean.TRUE)).thenReturn(currentDefault);

        addressDao.setdefault(id);

        assertFalse(currentDefault.getBe_default(), "The current default should no longer be default.");
        assertTrue(newDefault.getBe_default(), "The new address should now be the default.");
        verify(addressPoMapper, times(1)).save(currentDefault);
        verify(addressPoMapper, times(1)).save(newDefault);

        AddressPo noDefault = new AddressPo();
        noDefault.setId(id);

        when(addressPoMapper.findById(id)).thenReturn(Optional.of(noDefault));
        when(addressPoMapper.findBycustomer_id(id, Boolean.TRUE)).thenReturn(null);

        addressDao.setdefault(id);

        assertTrue(noDefault.getBe_default(), "The new address should be set as default when no default exists.");
        verify(addressPoMapper, times(2)).save(noDefault);
    }

}

