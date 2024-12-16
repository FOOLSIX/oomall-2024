package cn.edu.xmu.oomall.product.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.CategoryDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * @author huangzian
 * 2023-dgn2-004
 */
@SpringBootTest(classes = ProductTestApplication.class)
@Transactional(propagation = Propagation.REQUIRED)
public class CategoryTest {
    @Autowired
    private CategoryDao categoryDao;
    @MockBean
    private RedisUtil redisUtil;
    @Test
    public void testGetParent()
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Category category=Category.builder().id(186L).pid(1L).build();
        category.setCategoryDao(categoryDao);
        assertEquals("女装男装",category.getParent().getName());
    }
    @Test
    public void testGetParentWhenPIdNotExist()
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Category category=Category.builder().id(186L).pid(114514L).build();
        category.setCategoryDao(categoryDao);
        assertThrows(BusinessException.class,()->category.getParent());
    }
    @Test
    public void testGetParentWhenPIdIs0L()
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Category category=Category.builder().id(0L).build();
        category.setCategoryDao(categoryDao);
        assertEquals(null,category.getParent());
    }
    @Test
    public void testBeFirstClassCategoryTrue()
    {
        Category category=Category.builder().id(186L).pid(0L).build();
        assertEquals(true,category.beFirstClassCategory());
    }
    @Test
    public void testBeFirstClassCategoryFalse()
    {
        Category category=Category.builder().id(186L).pid(186L).build();
        assertEquals(false,category.beFirstClassCategory());
    }
    @Test
    public void testGetCommissionRatio()
    {
        Category category=Category.builder().id(186L).pid(1L).build();
        category.setCategoryDao(categoryDao);
        assertEquals(4,category.getCommissionRatio());
    }
}
