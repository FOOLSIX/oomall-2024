package cn.edu.xmu.oomall.product.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author keyu zhu
 * @date 2022/12/11
 */
@SpringBootTest(classes = ProductTestApplication.class)
@Transactional
public class OnsaleDaoTest {

    @Autowired
    private OnSaleDao onsaleDao;

    @MockBean
    ShopMapper shopMapper;

    @MockBean
    RedisUtil redisUtil;
    /**
     * 上架情况测试
     * 2023-12-09
     * @author yuhao shi
     */
    @Test
    public void findByIdTestGivenO4(){
        Mockito.when(redisUtil.hasKey("O4")).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        OnSale onsale = onsaleDao.findById(4L,3L);
        assertThat(onsale.getProductId()).isEqualTo(1552L);
        assertThat(onsale.getPrice()).isEqualTo(12650);
        assertThat(onsale.getQuantity()).isEqualTo(26);
    }

    @Test
    public void findByIdTestGivenNoEqual(){
        Mockito.when(redisUtil.hasKey("O20")).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        assertThrows(BusinessException.class, () -> onsaleDao.findById(4L,20L));
    }

    @Test
    public void findByIdTestGivenNoExist(){
        Mockito.when(redisUtil.hasKey("O10000000")).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () -> onsaleDao.findById(20L,10000000L));
    }

/*


    @Test
    public void testInsert(){

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("测试商铺10");
        Mockito.when(shopMapper.getShopById(1L)).thenReturn(new InternalReturnObject<>(shop));
        OnSale onsale = OnSale.builder().type(OnSale.NORMAL).maxQuantity(200).quantity(2000).shopId(10L).price(10L).
                endTime(LocalDateTime.parse("2040-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME)).beginTime(LocalDateTime.parse("2030-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME))
                .productId(1550L).build();
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("admin2");
        userDto.setDepartId(1L);
        OnSale insert = onsaleDao.insert(onsale, userDto);
        assertEquals(10L, insert.getPrice());
        assertEquals(200,insert.getMaxQuantity());
        assertNotNull(insert.getId());
    }


    @Test
    public void testSave1(){

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Shop shop = Shop.builder().id(10L).name("测试商铺10").build();
        Mockito.when(shopDao.getShopById(1L)).thenReturn(new InternalReturnObject<>(shop));
        Onsale onsale = Onsale.builder().id(1L).invalid(Byte.valueOf("1")).type(NORMAL).maxQuantity(200).quantity(2000).shopId(10L).price(10L).
                endTime(LocalDateTime.parse("2040-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME)).beginTime(LocalDateTime.parse("2030-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME))
                .productId(1550L).build();
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setUserLevel(1);
        userDto.setName("admin2");
        userDto.setDepartId(1L);
        String key = onsaleDao.save(onsale, userDto);
        assertEquals("O1",key);

    }

    @Test
    public void testRetrieveByProductId(){

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        Shop shop = Shop.builder().id(10L).name("测试商铺10").build();
        Mockito.when(shopDao.getShopById(1L)).thenReturn(new InternalReturnObject<>(shop));
        List<Onsale> onsales = onsaleDao.retrieveByProductId(1550L, 0, 10);
        assertEquals(1,onsales.size());

        assertEquals(53295L,onsales.get(0).getPrice());
    }


    @Test
    public void findByIdTest1(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Onsale onsale = onsaleDao.findById(4L);
        assertThat(onsale.getProductId()).isEqualTo(1553L);
        assertThat(onsale.getPrice()).isEqualTo(1027);
    }

    @Test
    public void findByIdTest2(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        assertThrows(BusinessException.class, () -> onsaleDao.findById(4000L));
    }

    @Test
    public void retrieveByShopIdAndOnsaleIdAndProductIdTest1(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Onsale> onsales = onsaleDao.retrieveByShopIdAndOnsaleIdAndProductId( 10L, 1L, 1550L);
        assertThat(onsales.size()).isEqualTo(1);
    }

    @Test
    public void retrieveByShopIdAndOnsaleIdAndProductIdTest2(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Onsale> onsales = onsaleDao.retrieveByShopIdAndOnsaleIdAndProductId( 10L, null, 1550L);
        assertThat(onsales.size()).isEqualTo(1);
    }

    @Test
    public void retrieveByShopIdAndOnsaleIdAndProductIdTest3(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Onsale> onsales = onsaleDao.retrieveByShopIdAndOnsaleIdAndProductId( 8L, null, 5461L);
        assertThat(onsales.size()).isEqualTo(1);
    }

    @Test
    public void retrieveByShopIdAndOnsaleIdAndProductIdTest4(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Onsale> onsales = onsaleDao.retrieveByShopIdAndOnsaleIdAndProductId( 1L, null, null);
        assertThat(onsales.size()).isEqualTo(316);
    }
    @Test
    public void retrieveByShopIdAndProductIdAndInvalidEqualsTest1(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Onsale> onsales = onsaleDao.retrieveValidByShopIdAndProductId(null, 1551L, null,null);
        assertThat(onsales.size()).isEqualTo(0);
    }

    @Test
    public void retrieveByShopIdAndProductIdAndInvalidEqualsTest2(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Onsale> onsales = onsaleDao.retrieveValidByShopIdAndProductId(8L, 5461L);
        assertThat(onsales.size()).isEqualTo(1);
        assertThat(onsales.get(0).getId()).isEqualTo(3913L);
    }

    @Test
    public void retrieveByShopIdAndProductIdAndInvalidEqualsTest3(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Onsale> onsales = onsaleDao.retrieveValidByShopIdAndProductId(1L, null);
        assertThat(onsales.size()).isEqualTo(0);
    }

    @Test
    public void retrieveByShopIdAndProductIdAndInvalidEqualsTest4(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Onsale> onsales = onsaleDao.retrieveValidByShopIdAndProductId(null, null);
        assertThat(onsales.size()).isEqualTo(1);
    }
*/
}