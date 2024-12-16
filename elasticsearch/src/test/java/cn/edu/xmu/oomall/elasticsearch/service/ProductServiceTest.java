package cn.edu.xmu.oomall.elasticsearch.service;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.elasticsearch.ElasticsearchApplication;
import cn.edu.xmu.oomall.elasticsearch.mapper.ProductMapper;
import cn.edu.xmu.oomall.elasticsearch.mapper.po.ProductEs;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ElasticsearchApplication.class)
@Slf4j
public class ProductServiceTest {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;


//    @Test
//    public void testSaveOrUpdateProduct() {
//        ProductEs productEs = new ProductEs();
//        productEs.setId(1L);
//        productEs.setName("Test Product");
//        productEs.setShopId(1L);
//        productEs.setBarcode("12345");
//
//        // 调用被测方法
//        ReturnObject result = productService.saveOrUpdateProduct(productEs);
//
//        // 验证返回结果
//        assertEquals(ReturnNo.OK, result.getCode());
//        assertEquals("Product saved or updated successfully: id=1, name='Test Product'", result.getErrMsg());
//
//        // 验证存储数据
//        Optional<ProductEs> savedProduct = productMapper.findById(1L);
//        assertTrue(savedProduct.isPresent());
//        assertEquals("Test Product", savedProduct.get().getName());
//
//        // 清理数据（模拟事务的回滚）
//        productMapper.deleteById(1L);
//    }

    @Test
    public void testSearchProductsWithAllParams() {

        ProductEs product = new ProductEs();
        product.setId(1550L);
        product.setName("罐头");
        product.setShopId(10L);
        product.setBarcode("6924254673572");

        // 调用被测方法
        ReturnObject result = productService
                .searchProducts(product.getName(), product.getBarcode(), product.getShopId(), 1, 10);

        // 验证返回结果
        assertEquals(ReturnNo.OK, result.getCode());
        List<Long> expectedIds = List.of(product.getId());
        assertEquals(expectedIds, result.getData());

    }

    @Test
    public void testSearchProductsByNameOnly() {

        ProductEs product = new ProductEs();
        product.setName("香皂");

        // 调用被测方法
        ReturnObject result = productService.searchProducts(product.getName(), null, null, 1, 10);

        // 验证返回结果
        assertEquals(ReturnNo.OK, result.getCode());
        List<Long> expectedIds = List.of(1902L, 1901L, 1900L, 1892L, 1891L, 1878L, 1978L, 1976L, 1973L, 1969L);
        assertEquals(expectedIds, result.getData());

    }

    @Test
    public void testSearchProductsNoResults() {
        // 调用被测方法
        ReturnObject result = productService.searchProducts("Nonexistent", null, null, 1, 10);

        // 验证返回结果
        assertEquals(ReturnNo.OK, result.getCode());
        assertTrue(((List<?>) result.getData()).isEmpty());
    }

}
