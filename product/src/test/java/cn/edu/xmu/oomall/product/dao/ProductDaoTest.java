package cn.edu.xmu.oomall.product.dao;

import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;


@SpringBootTest(classes = ProductTestApplication.class)
@Transactional
public class ProductDaoTest {
    @Autowired
    ProductDao productDao;
    @MockBean
    private RedisUtil redisUtil;
    /**
 * @auther huang zhong
 * @date 2023-dgn2-005
 */
    @Test
    public void testFindNoOnsaleByIdWhenNull() {
        assertThrows(BusinessException.class, ()->productDao.findNoOnsaleById(null,null));
    }
    /**
 * @auther huang zhong
 * @date 2023-dgn2-005
 */
    @Test
    public void testFindNoOnsaleWhenOther() {
        assertThrows(BusinessException.class, ()->productDao.findNoOnsaleById(1l,5461l));
    }
    /**
 * @auther huang zhong
 * @date 2023-dgn2-005
 */
    @Test
    public void testFindNoOnsaleWhenAdmin() {
        assertEquals(385, productDao.findNoOnsaleById(0l,5461l).getGoodsId());
    }

    @MockBean
    ShopMapper shopMapper;
    /**
     * 寻找商品测试
     * 2023-12-09
     * @author yuhao shi
     */
    @Test
    public void findByOnsaleTest(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        OnSale onsale = OnSale.builder().id(189L).type((byte) 0).maxQuantity(50).quantity(15).shopId(9L).price(7008L).
                endTime(LocalDateTime.parse("2027-02-19T14:38:20", DateTimeFormatter.ISO_DATE_TIME)).beginTime(LocalDateTime.parse("2021-11-11T14:38:20", DateTimeFormatter.ISO_DATE_TIME))
                .productId(1738L).build();
        Product product = productDao.findByOnsale(onsale);
        assertThat(product.getName()).isEqualTo("丰悦长粒香米");
        assertThat(product.getOriginalPrice()).isEqualTo(13909);
    }
}
