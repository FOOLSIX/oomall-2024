package cn.edu.xmu.oomall.product.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Rui Li
 * @task 2023-dgn2-007
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class OnSaleServiceTest {

    @Autowired
    private OnsaleService onsaleService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    @Test
    void testDelete() {
        UserDto platformUser = UserDto.builder().id(1L).departId(PLATFORM).userLevel(1).name("admin").build();
        onsaleService.delete(845L, platformUser);
    }

    /**
     * 获取参加指定活动的商品
     */
    @Test
    void testGetCouponActProduct() {
        List<Product> couponActProduct = onsaleService.getCouponActProduct(4L, 1, 10);
        Product product = couponActProduct.get(0);
        assert couponActProduct.size() == 1;
        assert Objects.equals(product.getName(), "肖家白胡椒粉30");

    }

    /**
     * 库存不足
     */
    @Test
    void testIncrQuantityGivenExceedQuantity() {
        assertThrows(BusinessException.class,()->onsaleService.incrQuantity(147L,100));
    }
}
