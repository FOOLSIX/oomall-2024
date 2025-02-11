package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class WarehouseRegionDaoTest {

    @Autowired
    private WarehouseRegionDao warehouseRegionDao;

    @Test
    public void testCheckRedis() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("13088admin");
        WarehouseRegion warehouseRegion = new WarehouseRegion();
        warehouseRegion.setWarehouseId(8L);
        warehouseRegion.setRegionId(1L);
        warehouseRegion.setBeginTime(LocalDateTime.now());
        warehouseRegion.setEndTime(LocalDateTime.now());
        List<Long> regionIds = new ArrayList<>();
        regionIds.add(1L);
        assertThrows(RuntimeException.class, ()->warehouseRegionDao.checkWarehouseRegion(warehouseRegion.getWarehouseId(), regionIds));
    }

    @Test
    public void testCheckRedis2() throws Exception {
        warehouseRegionDao.findByWarehouseIdAndRegionId(1L,1L);
        List<Long> regionIds = new ArrayList<>();
        regionIds.add(1L);
        assertThrows(RuntimeException.class, ()->warehouseRegionDao.checkWarehouseRegion(1L, regionIds));
    }

    @Test
    public void testFindRedis() throws Exception {
        warehouseRegionDao.findByWarehouseIdAndRegionId(1L,1L);
        warehouseRegionDao.findByWarehouseIdAndRegionId(1L,1L);
    }
}
