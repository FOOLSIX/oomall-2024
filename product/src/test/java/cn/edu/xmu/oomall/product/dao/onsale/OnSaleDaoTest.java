package cn.edu.xmu.oomall.product.dao.onsale;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Rui Li
 * @task 2023-dgn2-007
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class OnSaleDaoTest {

    @Autowired OnSaleDao onsaleDao;

    @MockBean
    private RedisUtil redisUtil;

    /**
     * 获得货品的最近的价格和库存
     * Redis中无缓存
     */
    @Test
    void testFindLatestValidOnsaleByProductIdWhenNotExistCache() {
        when(redisUtil.hasKey(eq("OV1561L"))).thenReturn(false);
        OnSale latestValidOnsale = onsaleDao.findLatestValidOnsaleByProductId(1561L);
        assert latestValidOnsale.getPrice().equals(3569L);
        assert latestValidOnsale.getQuantity().equals(66);
    }

    /**
     * 获得货品的最近的价格和库存
     * 没有最近的价格和库存
     */
    @Test
    void testFindLatestValidOnsaleByProductIdWhenNotExistCacheAndOnsaleNotExist() {
        when(redisUtil.hasKey(eq("OV1561L"))).thenReturn(false);
        OnSale latestValidOnsale = onsaleDao.findLatestValidOnsaleByProductId(1894L);
        assert latestValidOnsale.getPrice() == null;
        assert latestValidOnsale.getQuantity() == null;
    }

    /**
     * 获得货品的最近的价格和库存
     * Redis中有缓存
     */
    @Test
    void testFindLatestValidOnsaleByProductIdWhenExistCache() {

        when(redisUtil.hasKey(eq("OV1561"))).thenReturn(true);
        when(redisUtil.get(eq("OV1561"))).thenReturn(12);

        OnSale latestValidOnsale = onsaleDao.findLatestValidOnsaleByProductId(1561L);
        assert latestValidOnsale.getPrice().equals(3569L);
        assert latestValidOnsale.getQuantity().equals(66);
    }



    /**
     * 获得货品的最近的价格和库存
     * 缓存中的id为-1
     */
    @Test
    void testFindLatestValidOnsaleByProductIdWhenExistCacheAndIdNotExist() {

        when(redisUtil.get(eq("OV1561"))).thenReturn(-1);

        OnSale latestValidOnsale = onsaleDao.findLatestValidOnsaleByProductId(1561L);
        assert latestValidOnsale.getPrice() == 3569;
        assert latestValidOnsale.getQuantity() == 66;
    }

    /**
     * 获得货品的最近的价格和库存
     * 缓存中的OnsaleId查不到结果
     */
    @Test
    void testFindLatestValidOnsaleByProductIdWhenExistCacheAndOnsaleNotExist() {

        when(redisUtil.hasKey(eq("OV1561"))).thenReturn(true);
        when(redisUtil.get(eq("OV1561"))).thenReturn(9999);

        OnSale latestValidOnsale = onsaleDao.findLatestValidOnsaleByProductId(1561L);
        assert latestValidOnsale.getPrice() == null;
        assert latestValidOnsale.getQuantity() == null;
    }

    /**
     * 通过活动id返回Onsale
     */
    @Test
    void testRetrieveByActIdGivenActIdNull() {
        List<OnSale> onSales = onsaleDao.retrieveByActId(null, 0, 10);
        assert onSales == null;
    }

    /**
     * 判断是否存在时间重叠的onsale
     */
    @Test
    void testHasInsertGivenProductIdNull(){
        UserDto user = UserDto.builder().id(1L).name("商铺8").departId(8L).userLevel(1).build();

        OnSale onSale = new OnSale();

        assertThrows(BusinessException.class,()->onsaleDao.insert(onSale,user));
    }

    /**
     * 删除所有相关的onsale
     */
    @Test
    void testDeleteRelateOnsales() {
        onsaleDao.deleteRelateOnsales(4L);
    }

}
