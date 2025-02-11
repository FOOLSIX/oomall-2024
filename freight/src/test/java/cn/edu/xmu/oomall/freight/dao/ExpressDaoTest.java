package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptor;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Wu Yiwei
 * @date 2024/12/20
 * @description ExpressDao 测试
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ExpressDaoTest {
    @Mock
    private ExpressMapper mockExpressMapper;
    @Mock
    private ContractDao mockContractDao;
    @Mock
    private LogisticsAdaptorFactory mockFactory;
    @Mock
    private LogisticsAdaptor mockLogisticsAdaptor;
    private ExpressDao expressDaoUnderTest;
    @BeforeEach
    void setUp() {
        expressDaoUnderTest = new ExpressDao(mockExpressMapper, mockContractDao, mockFactory);
    }

    private Express createTestExpress() {
        Logistics logistics = new Logistics();
        logistics.setId(0L);
        logistics.setName("testLogistics");
        logistics.setAppId("testAppId");
        logistics.setLogisticsClass("testClass");
        Contract contract = new Contract();
        contract.setId(0L);
        contract.setLogisticsId(0L);
        contract.setLogistics(logistics);
        contract.setShopId(0L);
        Express express = Express.builder()
                .id(0L)
                .shopId(0L)
                .billCode("testBillCode")
                .contractId(0L)
                .status(Express.UNSHIPPED)
                .contract(contract)
                .build();
        express.setContractDao(mockContractDao);
        express.setExpressDao(expressDaoUnderTest);

        express.setSendRegionId(1L);
        express.setSendAddress("test address");
        express.setSendMobile("12345678901");
        express.setReceivRegionId(2L);
        express.setReceivAddress("test receive address");
        express.setReceivMobile("12345678902");
        express.setOrderCode("TEST001");

        return express;
    }

    @Test
    void testInsert() {
        // Setup
        final UserDto user = UserDto.builder()
                .id(1L)
                .name("test")
                .build();

        Express bo = createTestExpress();

        Express savedExpress = createTestExpress();
        savedExpress.setId(1L);
        savedExpress.setCreatorId(user.getId());
        savedExpress.setCreatorName(user.getName());
        savedExpress.setGmtCreate(LocalDateTime.now());

        // Configure mock behavior
        when(mockExpressMapper.save(any(Express.class))).thenReturn(savedExpress);
        when(mockFactory.createAdaptor(any())).thenReturn(mockLogisticsAdaptor);
        when(mockContractDao.findById(anyLong(), anyLong())).thenReturn(bo.getContract());
        // Run the test
        final Express result = expressDaoUnderTest.insert(bo, user);
        // Verify
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCreatorId()).isEqualTo(user.getId());
        assertThat(result.getCreatorName()).isEqualTo(user.getName());

        verify(mockExpressMapper).save(argThat(express ->
                express.getShopId().equals(bo.getShopId()) &&
                        express.getBillCode().equals(bo.getBillCode())
        ));
    }

    @Test
    void testFindById() {
        // Setup
        Express express = createTestExpress();

        when(mockExpressMapper.findById(0L)).thenReturn(Optional.of(express));
        when(mockFactory.createAdaptor(any())).thenReturn(mockLogisticsAdaptor);
        when(mockContractDao.findById(anyLong(), anyLong())).thenReturn(express.getContract());
        // Run the test
        final Express result = expressDaoUnderTest.findById(0L, 0L);
        // Verify
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(0L);
        assertThat(result.getShopId()).isEqualTo(0L);
        assertThat(result.getContract()).isNotNull();
        assertThat(result.getContract().getLogistics()).isNotNull();
    }

    @Test
    void testFindById_ExpressMapperReturnsAbsent() {
        // Setup
        when(mockExpressMapper.findById(0L)).thenReturn(Optional.empty());
        // Verify
        assertThatThrownBy(() -> expressDaoUnderTest.findById(0L, 0L))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void testSave() {
        // Setup
        final UserDto user = UserDto.builder().build();
        Express bo = createTestExpress();

        when(mockExpressMapper.save(any(Express.class))).thenReturn(bo);
        when(mockFactory.createAdaptor(any())).thenReturn(mockLogisticsAdaptor);
        when(mockContractDao.findById(anyLong(), anyLong())).thenReturn(bo.getContract());
        // Run the test
        expressDaoUnderTest.save(bo, user);
        // Verify
        verify(mockExpressMapper).save(any(Express.class));
    }

    @Test
    void testRetrieveByBillCode() {
        // Setup
        Express express = createTestExpress();

        when(mockExpressMapper.findByBillCode("billCode")).thenReturn(express);
        when(mockFactory.createAdaptor(any())).thenReturn(mockLogisticsAdaptor);
        when(mockContractDao.findById(anyLong(), anyLong())).thenReturn(express.getContract());
        // Run the test
        final Express result = expressDaoUnderTest.retrieveByBillCode(0L, "billCode");
        // Verify
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(0L);
        assertThat(result.getShopId()).isEqualTo(0L);
        assertThat(result.getContract()).isNotNull();
        assertThat(result.getContract().getLogistics()).isNotNull();
    }

    @Test
    void testRetrieveByBillCode_ExpressMapperReturnsNull() {
        // Setup
        when(mockExpressMapper.findByBillCode("billCode")).thenReturn(null);
        // Run the test
        final Express result = expressDaoUnderTest.retrieveByBillCode(0L, "billCode");
        // Verify
        assertThat(result).isNull();
    }
}
