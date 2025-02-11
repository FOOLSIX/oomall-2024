package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ContractDao;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;

import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptor;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.mapper.mongo.ExpressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Wu Yiwei
 * @date 2024/12/20
 * @description ExpressService 测试
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ExpressServiceTest {

    @Mock
    private ContractDao mockContractDao;
    @Mock
    private ExpressDao mockExpressDao;
    @Mock
    private ExpressMapper mockExpressMapper;

    private ExpressService expressServiceUnderTest;

    @Mock
    private LogisticsAdaptorFactory mockLogisticsAdaptorFactory;

    @Mock
    private LogisticsAdaptor mockLogisticsAdaptor;


    @BeforeEach
    void setUp() {
        mockExpressDao = new ExpressDao(mockExpressMapper, mockContractDao, mockLogisticsAdaptorFactory);
        expressServiceUnderTest = new ExpressService(mockContractDao, mockExpressDao);
    }

    @Test
    void testCreateExpress() {
        // Setup
        final Express express = Express.builder()
                .contractId(0L)
                .build();
        final UserDto user = UserDto.builder().build();
        // Configure ContractDao.findById(...).
        final Contract contract = new Contract();

        final Logistics logistics = new Logistics();
        logistics.setName("name");
        logistics.setAppId("appId");
        logistics.setLogisticsClass("testLogisticsClass");
        contract.setLogistics(logistics);
        contract.setLogisticsId(0L);
        contract.setExpressDao(mockExpressDao);

        when(mockLogisticsAdaptorFactory.createAdaptor(any(Logistics.class)))
                .thenReturn(mockLogisticsAdaptor);
        contract.setLogisticsAdaptor(mockLogisticsAdaptorFactory);

        PostCreatePackageAdaptorDto adaptorDto = new PostCreatePackageAdaptorDto();
        adaptorDto.setBillCode("testBillCode");
        when(mockLogisticsAdaptor.createPackage(any(Contract.class), any(Express.class)))
                .thenReturn(adaptorDto);
        when(mockContractDao.findById(0L, 0L)).thenReturn(contract);
        // Run the test
        final Express result = expressServiceUnderTest.createExpress(0L, express, user);
        // Verify the results
        assertThat(result.getContractId()).isEqualTo(express.getContractId());
        assertThat(result.getBillCode()).isEqualTo("testBillCode");
        verify(mockContractDao).findById(0L, 0L);
    }

    @Test
    void testCreateExpress_ContractDaoThrowsRuntimeException() {
        // Setup
        final Express express = Express.builder()
                .contractId(0L)
                .build();
        final UserDto user = UserDto.builder().build();

        when(mockContractDao.findById(0L, 0L)).thenThrow(RuntimeException.class);

        // Run the test
        assertThatThrownBy(() -> expressServiceUnderTest.createExpress(0L, express, user))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testSendExpress() {
        // Setup
        final UserDto user = UserDto.builder().build();
        final Logistics logistics = new Logistics();
        logistics.setName("name");
        logistics.setAppId("appId");
        logistics.setLogisticsClass("testLogisticsClass");
        final Contract contract = new Contract();
        contract.setLogistics(logistics);
        contract.setLogisticsId(0L);
        final Express express = Express.builder()
                .contractId(0L)
                .billCode("testBillCode")
                .status(Express.UNSHIPPED)
                .contract(contract)
                .build();

        when(mockExpressMapper.findById(0L)).thenReturn(Optional.of(express));
        when(mockExpressMapper.save(any(Express.class))).thenReturn(express);
        when(mockContractDao.findById(anyLong(), anyLong())).thenReturn(contract);
        when(mockLogisticsAdaptorFactory.createAdaptor(any(Logistics.class)))
                .thenReturn(mockLogisticsAdaptor);

        when(mockLogisticsAdaptor.getPackage(any(), any()))
                .thenReturn(express);
        // Run the test
        expressServiceUnderTest.sendExpress(0L, 0L, user,
                LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Verify
        verify(mockExpressMapper).findById(0L);
    }

    @Test
    void testSendExpress_ExpressDaoThrowsRuntimeException() {
        // Setup
        final UserDto user = UserDto.builder().build();
        when(mockExpressMapper.findById(0L)).thenThrow(RuntimeException.class);

        // Run the test
        assertThatThrownBy(
                () -> expressServiceUnderTest.sendExpress(0L, 0L, user, LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0))).isInstanceOf(RuntimeException.class);
    }

    @Test
    void testCancelExpress() {
        // Setup
        final UserDto user = UserDto.builder().build();
        final Logistics logistics = new Logistics();
        logistics.setName("name");
        logistics.setAppId("appId");
        logistics.setLogisticsClass("testLogisticsClass");
        final Contract contract = new Contract();
        contract.setLogistics(logistics);
        contract.setLogisticsId(0L);
        final Express express = Express.builder()
                .contractId(0L)
                .billCode("testBillCode")
                .status(Express.UNSHIPPED)
                .contract(contract)
                .build();

        when(mockExpressMapper.findById(0L)).thenReturn(Optional.of(express));
        when(mockExpressMapper.save(any(Express.class))).thenReturn(express);
        when(mockContractDao.findById(anyLong(), anyLong())).thenReturn(contract);
        when(mockLogisticsAdaptorFactory.createAdaptor(any(Logistics.class)))
                .thenReturn(mockLogisticsAdaptor);

        when(mockLogisticsAdaptor.getPackage(any(), any()))
                .thenReturn(express);
        // Run the test
        expressServiceUnderTest.cancelExpress(0L, 0L, user);

        // Verify
        verify(mockExpressMapper).findById(0L);
    }


    @Test
    void testCancelExpress_ExpressDaoThrowsRuntimeException() {
        // Setup
        final UserDto user = UserDto.builder().build();
        when(mockExpressMapper.findById(0L)).thenThrow(RuntimeException.class);

        // Run the test
        assertThatThrownBy(() -> expressServiceUnderTest.cancelExpress(0L, 0L, user))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testRetrieveExpressByBillCode() {
        // Setup
        final Contract contract = new Contract();
        final Logistics logistics = new Logistics();
        logistics.setName("name");
        logistics.setAppId("appId");
        logistics.setLogisticsClass("testLogisticsClass");
        contract.setLogistics(logistics);
        contract.setLogisticsId(0L);

        final Express express = Express.builder()
                .id(0L)
                .contractId(0L)
                .billCode("testBillCode")
                .status(Express.SHIPPED)
                .shopId(0L)
                .contract(contract)
                .build();

        when(mockExpressMapper.findByBillCode("testBillCode")).thenReturn(express);
        when(mockContractDao.findById(anyLong(), anyLong())).thenReturn(contract);
        when(mockLogisticsAdaptorFactory.createAdaptor(any(Logistics.class)))
                .thenReturn(mockLogisticsAdaptor);
        when(mockLogisticsAdaptor.getPackage(any(), any())).thenReturn(express);

        // Run the test
        final Express result = expressServiceUnderTest.retrieveExpressByBillCode(0L, "testBillCode");

        // Verify
        assertThat(result).isEqualTo(express);
        verify(mockExpressMapper).findByBillCode("testBillCode");
    }

    @Test
    void testRetrieveExpressByBillCode_ExpressDaoThrowsRuntimeException() {
        // Setup
        when(mockExpressMapper.findByBillCode("testBillCode")).thenThrow(RuntimeException.class);
        // Run the test
        assertThatThrownBy(() -> expressServiceUnderTest.retrieveExpressByBillCode(0L, "testBillCode"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testFindExpressById() {
        // Setup
        final Contract contract = new Contract();
        final Logistics logistics = new Logistics();
        logistics.setName("name");
        logistics.setAppId("appId");
        logistics.setLogisticsClass("testLogisticsClass");
        contract.setLogistics(logistics);
        contract.setLogisticsId(0L);

        final Express express = Express.builder()
                .id(0L)
                .contractId(0L)
                .billCode("testBillCode")
                .status(Express.SHIPPED)
                .shopId(0L)
                .contract(contract)
                .build();

        when(mockExpressMapper.findById(0L)).thenReturn(Optional.of(express));
        when(mockContractDao.findById(anyLong(), anyLong())).thenReturn(contract);
        when(mockLogisticsAdaptorFactory.createAdaptor(any(Logistics.class)))
                .thenReturn(mockLogisticsAdaptor);
        when(mockLogisticsAdaptor.getPackage(any(), any())).thenReturn(express);

        // Run the test
        final Express result = expressServiceUnderTest.findExpressById(0L, 0L);

        // Verify
        assertThat(result).isEqualTo(express);
        verify(mockExpressMapper).findById(0L);
    }

    @Test
    void testFindExpressById_ExpressDaoThrowsRuntimeException() {
        // Setup
        when(mockExpressMapper.findById(0L)).thenThrow(RuntimeException.class);
        // Run the test
        assertThatThrownBy(() -> expressServiceUnderTest.findExpressById(0L, 0L))
                .isInstanceOf(RuntimeException.class);
    }
}
