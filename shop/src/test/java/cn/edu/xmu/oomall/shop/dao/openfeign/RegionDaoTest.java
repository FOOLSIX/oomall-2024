package cn.edu.xmu.oomall.shop.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.shop.ShopTestApplication;
import cn.edu.xmu.oomall.shop.dao.bo.Region;
import cn.edu.xmu.oomall.shop.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.shop.mapper.openfeign.po.RegionPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = ShopTestApplication.class)
@Slf4j
public class RegionDaoTest {

    @Autowired
    private RegionDao regionDao;

    @SpyBean
    private RegionMapper regionMapper; // 模拟 RegionMapper

    @Test
    public void testFindByIdSuccess() {
        RegionPo mockRegionPo = new RegionPo(5L, "Test Region");
        InternalReturnObject<RegionPo> mockReturnObject = new InternalReturnObject<>();
        mockReturnObject.setErrno(ReturnNo.OK.getErrNo()); // 设置成功的 errno
        mockReturnObject.setData(mockRegionPo); // 设置返回的数据

        doReturn(mockReturnObject).when(regionMapper).findRegionById(5L);

        Region region = regionDao.findById(5L);

        // 验证返回值
        assertNotNull(region);
        assertEquals(5L, region.getId());
        log.debug("Test successful: {}", region);
    }

    @Test
    public void testFindByIdResourceNotExist() {
        InternalReturnObject<RegionPo> mockReturnObject = new InternalReturnObject<>();
        mockReturnObject.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()); // 设置失败的 errno
        mockReturnObject.setErrmsg("Region not found");

        doReturn(mockReturnObject).when(regionMapper).findRegionById(99999999L);

        // 确保测试环境中没有 ID 为 9999 的记录
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            regionDao.findById(99999999L);
        });

        // 验证异常内容
        assertEquals(ReturnNo.RESOURCE_ID_NOTEXIST, exception.getErrno());
        log.debug("Exception captured: {}", exception.getMessage());
    }

    @Test
    public void testRetrieveParentRegionsByIdSuccess() {
        List<RegionPo> mockRegionPoList = List.of(new RegionPo(1L, "Parent Region"));
        InternalReturnObject<List<RegionPo>> mockReturnObject = new InternalReturnObject<>();
        mockReturnObject.setErrno(ReturnNo.OK.getErrNo()); // 设置成功的 errno
        mockReturnObject.setData(mockRegionPoList); // 设置返回的数据

        doReturn(mockReturnObject).when(regionMapper).retrieveParentRegionsById(5L);

        List<Region> regions = regionDao.retrieveParentRegionsById(5L);

        // 验证返回值
        assertNotNull(regions);
        assertFalse(regions.isEmpty());
        log.debug("Test successful: {}", regions);
    }

    @Test
    public void testRetrieveParentRegionsByIdResourceNotExist() {
        InternalReturnObject<List<RegionPo>> mockReturnObject = new InternalReturnObject<>();
        mockReturnObject.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()); // 设置失败的 errno
        mockReturnObject.setErrmsg("Parent regions not found");

        doReturn(mockReturnObject).when(regionMapper).retrieveParentRegionsById(99999999L);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            regionDao.retrieveParentRegionsById(99999999L);
        });

        // 验证异常内容
        assertEquals(ReturnNo.RESOURCE_ID_NOTEXIST, exception.getErrno());
        log.debug("Exception captured: {}", exception.getMessage());
    }
}