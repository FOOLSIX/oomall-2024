package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import cn.edu.xmu.oomall.freight.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author 37220222203558
 * 2024-dsg116
 */
@SpringBootTest(classes = FreightApplication.class)
@AutoConfigureMockMvc
@Transactional
public class WarehouseServiceTest {

    @Autowired
    private WarehouseService warehouseService;

    @MockBean
    public RegionMapper regionMapper;

    @Test
    public void testGetWarehousesRedis() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        RegionPo regionPo = new RegionPo();
        regionPo.setId(1043L);
        regionPo.setName("朝阳新城第二社区居民委员会");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(1043L))).thenReturn(ret);

        warehouseService.getWarehouses(1L, 1, 1);
        warehouseService.getWarehouses(1L, 1, 1);
    }

    @Test
    public void testChangeWarehouseWhenNotExist() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        Warehouse warehouse = new Warehouse();
        warehouse.setShopId(1L);
        warehouse.setId(30L);
        warehouse.setRegionId(1L);
        warehouse.setName("haha");
        warehouse.setAddress("haha");
        warehouse.setSenderName("haha");
        warehouse.setSenderMobile("12345678911");
        assertThrows(RuntimeException.class, ()->warehouseService.changeWarehouse(warehouse, user));
    }

    @Test
    public void testChangeWarehouseWhenOutScope() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        Warehouse warehouse = new Warehouse();
        warehouse.setShopId(1L);
        warehouse.setId(2L);
        warehouse.setRegionId(1L);
        warehouse.setName("haha");
        warehouse.setAddress("haha");
        warehouse.setSenderName("haha");
        warehouse.setSenderMobile("12345678911");
        assertThrows(RuntimeException.class, ()->warehouseService.changeWarehouse(warehouse, user));
    }

    @Test
    public void testDeleteWarehouseRedis() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        InternalReturnObject<RegionPo> ret1 = new InternalReturnObject<>();
        RegionPo regionPo = new RegionPo();
        regionPo.setId(1043L);
        regionPo.setName("朝阳新城第二社区居民委员会");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());
        regionPo.setId(1L);
        regionPo.setName("北京市");
        ret1.setData(regionPo);
        ret1.setErrno(ReturnNo.OK.getErrNo());
        ret1.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(1043L))).thenReturn(ret);
        Mockito.when(regionMapper.findRegionById(Mockito.eq(1L))).thenReturn(ret1);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        warehouseService.getWarehouses(1L, 1, 1);
        warehouseService.deleteWarehouse(1L, 1L, user);
    }

    @Test
    public void testDeleteWarehouseWhenContractNotExist() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        assertThrows(RuntimeException.class, ()->warehouseService.deleteWarehouse(1L, 25L, user));
    }


    @Test
    public void testGetWarehouseRegionsWhenNotExist() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        assertThrows(RuntimeException.class, ()->warehouseService.getWarehouseRegions(1L, 26L,1, 1));
    }

    @Test
    public void testGetWarehouseRegionsWhenOutScope() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        assertThrows(RuntimeException.class, ()->warehouseService.getWarehouseRegions(1L, 26L, 1, 1));
    }

    @Test
    public void testGetWarehouseRegionsWhenWarehouseNotExist() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        assertThrows(RuntimeException.class, ()->warehouseService.getWarehouseRegions(30L, 1L, 1, 1));
    }

    @Test
    public void testGetWarehouseRegionsRedis() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        InternalReturnObject<RegionPo> ret1 = new InternalReturnObject<>();
        RegionPo regionPo = new RegionPo();
        regionPo.setId(1L);
        regionPo.setName("北京市");
        ret.setData(regionPo);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());
        regionPo.setId(35L);
        regionPo.setName("交北头条社区居委会");
        ret1.setData(regionPo);
        ret1.setErrno(ReturnNo.OK.getErrNo());
        ret1.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(1L))).thenReturn(ret);
        Mockito.when(regionMapper.findRegionById(Mockito.eq(35L))).thenReturn(ret1);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        warehouseService.getWarehouseRegions(1L, 1L, 1, 1);
        warehouseService.getWarehouseRegions(1L, 1L, 1, 1);
    }

    @Test
    public void testCreateWarehouseRegionWhenExist() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(6L);
        warehouseRegion.setRegionId(1L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        assertThrows(RuntimeException.class, ()->warehouseService.createWarehouseRegion(1L, warehouseRegion, user));
    }

    @Test
    public void testCreateWarehouseRegionWhenRegionNotExist() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(1L);
        warehouseRegion.setRegionId(12345678L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        assertThrows(RuntimeException.class, ()->warehouseService.createWarehouseRegion(1L, warehouseRegion, user));
    }

    @Test
    public void testCreateWarehouseRegionWhenOutScope() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(1L);
        warehouseRegion.setRegionId(1L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        assertThrows(RuntimeException.class, ()->warehouseService.createWarehouseRegion(2L, warehouseRegion, user));
    }

    @Test
    public void testCreateWarehouseRegionWhenWarehouseNotExist() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(30L);
        warehouseRegion.setRegionId(1L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        assertThrows(RuntimeException.class, ()->warehouseService.createWarehouseRegion(1L, warehouseRegion, user));
    }

    @Test
    public void testChangeWarehouseRegionWhenNotExist() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(1L);
        warehouseRegion.setRegionId(30L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        assertThrows(RuntimeException.class, ()->warehouseService.changeWarehouseRegion(warehouseRegion, user));
    }

    @Test
    public void testChangeWarehouseRegionWhenTimeAllNull() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(1L);
        warehouseRegion.setRegionId(30L);
        warehouseRegion.setBeginTime(null);
        warehouseRegion.setEndTime(null);
        assertThrows(RuntimeException.class, ()->warehouseService.changeWarehouseRegion(warehouseRegion, user));
    }

    @Test
    public void testChangeWarehouseRegionWhenTimeNull() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(1L);
        warehouseRegion.setRegionId(30L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(null);
        assertThrows(RuntimeException.class, ()->warehouseService.changeWarehouseRegion(warehouseRegion, user));
    }

    @Test
    public void testChangeWarehouseRegionRedis() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        InternalReturnObject<List<RegionPo>> ret1 = new InternalReturnObject<>();
        RegionPo regionPo = new RegionPo();
        regionPo.setId(37L);
        regionPo.setName("国子监社区居委会");
        List<RegionPo> regionPoList = new ArrayList<>();
        regionPoList.add(regionPo);
        ret.setData(regionPo);
        ret1.setData(regionPoList);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());
        ret1.setErrno(ReturnNo.OK.getErrNo());
        ret1.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(37L))).thenReturn(ret);
        Mockito.when(regionMapper.retrieveParentRegionsById(Mockito.eq(37L))).thenReturn(ret1);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(1L);
        warehouseRegion.setRegionId(37L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        warehouseRegion = warehouseService.createWarehouseRegion(1L, warehouseRegion, user);
        warehouseRegion.setEndTime(LocalDateTime.now());
        warehouseRegion.setGmtCreate(LocalDateTime.now());
        warehouseService.changeWarehouseRegion(warehouseRegion, user);
    }

    @Test
    public void testCreateWarehouseRegionRedis() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        InternalReturnObject<List<RegionPo>> ret1 = new InternalReturnObject<>();
        RegionPo regionPo = new RegionPo();
        regionPo.setId(35L);
        regionPo.setName("交北头条社区居委会");
        List<RegionPo> regionPoList = new ArrayList<>();
        regionPoList.add(regionPo);
        ret.setData(regionPo);
        ret1.setData(regionPoList);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());
        ret1.setErrno(ReturnNo.OK.getErrNo());
        ret1.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(35L))).thenReturn(ret);
        Mockito.when(regionMapper.retrieveParentRegionsById(Mockito.eq(35L))).thenReturn(ret1);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(1L);
        warehouseRegion.setRegionId(35L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        warehouseService.createWarehouseRegion(1L, warehouseRegion, user);
        assertThrows(RuntimeException.class, ()->warehouseService.createWarehouseRegion(1L, warehouseRegion, user));
    }

    @Test
    public void testDeleteWarehouseRegionRedis() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        InternalReturnObject<List<RegionPo>> ret1 = new InternalReturnObject<>();
        RegionPo regionPo = new RegionPo();
        regionPo.setId(36L);
        regionPo.setName("北锣鼓巷社区居委会");
        List<RegionPo> regionPoList = new ArrayList<>();
        regionPoList.add(regionPo);
        ret.setData(regionPo);
        ret1.setData(regionPoList);
        ret.setErrno(ReturnNo.OK.getErrNo());
        ret.setErrmsg(ReturnNo.OK.getMessage());
        ret1.setErrno(ReturnNo.OK.getErrNo());
        ret1.setErrmsg(ReturnNo.OK.getMessage());

        Mockito.when(regionMapper.findRegionById(Mockito.eq(36L))).thenReturn(ret);
        Mockito.when(regionMapper.retrieveParentRegionsById(Mockito.eq(36L))).thenReturn(ret1);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(1L);
        warehouseRegion.setRegionId(36L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        warehouseService.createWarehouseRegion(1L, warehouseRegion, user);
        warehouseService.deleteWarehouseRegion(1L, 36L);
    }
}
