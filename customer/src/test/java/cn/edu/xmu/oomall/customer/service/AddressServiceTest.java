package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    @Mock
    private AddressDao addressDao;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() throws Exception {
        Address mockAddress = new Address();
        mockAddress.setId(1L);
        when(addressDao.findById(1L)).thenReturn(mockAddress);

        Address result = addressService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(addressDao, times(1)).findById(1L);
    }

    @Test
    public void testAddAddress() {
        Address newAddress = new Address();
        newAddress.setId(2L);

        addressService.addAddress(newAddress);
        verify(addressDao, times(1)).SaveAddress(newAddress);
    }

    @Test
    public void testGetCustomerAddress() {
        Address address1 = new Address();
        address1.setId(1L);
        Address address2 = new Address();
        address2.setId(2L);

        List<Address> mockAddressList = Arrays.asList(address1, address2);
        when(addressDao.getAlladdress(1L, 0, 10)).thenReturn(mockAddressList);

        List<Address> result = addressService.getCustomerAddress(1L, 0, 10);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(addressDao, times(1)).getAlladdress(1L, 0, 10);
    }

    @Test
    public void testSetDefaultAddress() {
        addressService.setDefaultAddress(1L);
        verify(addressDao, times(1)).setdefault(1L);
    }

    @Test
    public void testDeleteAddress() {
        addressService.deleteAddress(1L);
        verify(addressDao, times(1)).DeleteAddress(1L);
    }
}

