package cn.edu.xmu.oomall.product.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.Category;

@SpringBootTest(classes = ProductTestApplication.class)

public class CategoryDaoTest {
    /**
     * @author huang zhong
     * @task 2023-dgn2-005
     */
    @Autowired
    CategoryDao categoryDao;
    @MockBean
    private RedisUtil redisUtil;

    @Test
    public void testFindNoOnsaleByIdWhenNull() {
        Category category = new Category();
        category.setName("test");
        Mockito.when(redisUtil.hasKey("C1")).thenReturn(true);
        Mockito.when(redisUtil.get("C1")).thenReturn(category);
        assertEquals(category.getName(), categoryDao.findById(1l).getName());
    }

    /**
     * @author huang zhong
     * @task 2023-dgn2-005
     */
    @Test
    public void testFindNoOnsaleByIdWhenNotExist() {
        assertThrows(BusinessException.class, () -> categoryDao.findById(100000l));
    }

    /**
     * @author huangzian
     *         2023-dgn2-004
     */
    @MockBean
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testDelete() {
        assertThrows(BusinessException.class, () -> categoryDao.delete(500L));
    }

}
