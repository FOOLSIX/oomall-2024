//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.shop.dao.bo.divide;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.shop.ShopTestApplication;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.dao.bo.template.PieceTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import cn.edu.xmu.oomall.shop.dao.bo.template.TemplateResult;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 运费计件测试
 * ZhaoDong Wang
 * 2023-dgn1-009
 */
@SpringBootTest(classes = ShopTestApplication.class)
public class PieceTemplateTest {

    @Autowired
    RegionTemplateDao regionTemplateDao;

    @MockBean
    RedisUtil redisUtil;

    /**
     * 计件运费计算（最大简单打包）
     */
    @Test
    public void testCalculatePieceWhenMaxSimplePack() {
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 1, 1L, 1));
                add(new ProductItem(2L, 2L, 100L, 2, 1L, 2));
                add(new ProductItem(3L, 3L, 100L, 3, 1L, 3));
                add(new ProductItem(4L, 4L, 100L, 4, 1L, 4));
                add(new ProductItem(5L, 5L, 100L, 5, 1L, 3));
                add(new ProductItem(6L, 6L, 100L, 6, 1L, 2));
                add(new ProductItem(7L, 7L, 100L, 7, 1L, 1));
            }
        };
        RegionTemplate pieceTemplate = new PieceTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setFirstItems(1);
                setFirstPrice(10L);
                setAdditionalItems(3);
                setAdditionalPrice(15L);
                setUpperLimit(5);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(23L);
            }
        };
        regionTemplateDao.build(pieceTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = pieceTemplate.calculate(items);

        assertNotNull(results);
        results.stream().forEach(result -> {
            assertTrue(result.getPack().stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get() <= 5);
        });
        //总数应该是16件商品
        assertEquals(16, results.stream().map(result -> result.getPack().stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get());
        assertEquals(8, results.stream().map(result -> result.getPack().size()).reduce((x, y) -> x + y).get());
        assertEquals(4, results.size());
        assertEquals(130, results.stream().map(result -> result.getFee()).reduce((x, y) -> x + y).get());


    }
}
