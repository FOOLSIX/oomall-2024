package cn.edu.xmu.oomall.product.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.ProductDraft;
/**
 * @author huang zhong
 * @task 2023-dgn2-005
 */
@SpringBootTest(classes = ProductTestApplication.class)
public class ProductDraftDaoTest {
    @Autowired
    private ProductDraftDao productDraftDao;
    @MockBean
    private RedisUtil redisUtil;

    @Test
    public void testFindByIdWhenNull() {
        assertThrows(BusinessException.class, () -> productDraftDao.findById(1l, 1l));
    }

    @Test
    public void testRetrieveProductDraftByShopIdWhenNull() {
        assertEquals(0, productDraftDao.retrieveProductDraftByShopId(1l, 1, 10).size());
    }
    @Test
    public void testRetrieveProductDraftByShopIdWhenAdmin() {
        assertEquals(1, productDraftDao.retrieveProductDraftByShopId(0l, 1, 10).size());
    }
    @Test
    public void testSaveWhenNull() {
        ProductDraft productDraft=new ProductDraft();
        UserDto userDto =new UserDto();
        
        assertThrows(BusinessException.class, () ->productDraftDao.save(productDraft, userDto));
    }
}