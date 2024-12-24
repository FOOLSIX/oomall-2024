package cn.edu.xmu.oomall.customer.dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.mapper.jpa.AddressPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AddressDaoTest {

    @Mock
    private RedisUtil redisUtil;

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
        Long id = 1L;
        AddressPo addressPo = new AddressPo();
        addressPo.setId(id);
        when(addressPoMapper.findById(id)).thenReturn(Optional.of(addressPo));

        Address result = addressDao.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(addressPoMapper, times(1)).findById(id);
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
        Long customerId = 123L;
        int page = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id");

        AddressPo addressPo1 = new AddressPo();
        addressPo1.setId(1L);
        AddressPo addressPo2 = new AddressPo();
        addressPo2.setId(2L);
        List<AddressPo> addressPos = Arrays.asList(addressPo1, addressPo2);

        when(addressPoMapper.findBycustomer_id(customerId, pageable)).thenReturn(addressPos);

        List<Address> results = addressDao.getAlladdress(customerId, page, pageSize);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());
        verify(addressPoMapper, times(1)).findBycustomer_id(customerId, pageable);
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

        assertFalse(currentDefault.getBe_default());
        assertTrue(newDefault.getBe_default());
        verify(addressPoMapper, times(1)).save(currentDefault);
        verify(addressPoMapper, times(1)).save(newDefault);
    }
}

