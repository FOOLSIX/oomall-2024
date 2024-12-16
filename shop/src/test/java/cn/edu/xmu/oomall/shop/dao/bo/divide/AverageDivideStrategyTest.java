//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.shop.dao.bo.divide;

import cn.edu.xmu.oomall.shop.ShopTestApplication;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.dao.bo.template.PieceTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import cn.edu.xmu.oomall.shop.dao.bo.template.WeightTemplate;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 平均分包策略测试
 * 测试Template数据情况
 * id,shop,算法
 * 1,1,最大简单分包计重模板,  MaxDivideStrategy,SimpleAlgorithm
 * 2,1,平均背包分包计件模板,  AverageDivideStrategy, BackPackAlgorithm
 * 16,2,最大背包分包计重模板, MaxDivideStrategy, BackPackAlgorithm
 * 17,2,平均简单分包计件模板, AverageDivideStrategy, SimpleAlgorithm
 * 18,3,贪心计重模板 GreedyAverageDivideStrategy,
 * 19,3,优费计件模板 OptimalDivideStrategy,
 * 20,3,贪心计件模板 GreedyAverageDivideStrategy,
 * 21,3,优费计重模板 OptimalDivideStrategy,
 * 22,1,平均简单分包计重模板,AverageDivideStrategy, SimpleAlgorithm
 * 23,1,最大简单分包计件模板，MaxDivideStrategy,SimpleAlgorithm
 */

@SpringBootTest(classes = ShopTestApplication.class)
public class AverageDivideStrategyTest {

    @Autowired
    RegionTemplateDao regionTemplateDao;

    /**
     * 平均简单分包(计重）
     */

    /**
     * 37220222203708
     * 修改setTemplateId()
     */
    @Test
    public void testAverageDivideStrategyWhenSimpleWeigh() {
        PackAlgorithm algorithm = new SimpleAlgorithm();
        DivideStrategy divideStrategy = new AverageDivideStrategy(algorithm);
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
        RegionTemplate regionTemplate = new WeightTemplate() {
            {
                setUpperLimit(12);
                setTemplateId(22L);
            }
        };

        regionTemplateDao.build(regionTemplate);

        Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);

        assertNotNull(packs);
        // size=64/(64/12+1)=10
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 10);
        });
        assertEquals(64, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get());
        assertEquals(12, packs.stream().map(pack -> pack.size()).reduce((x, y) -> x + y).get());
        assertEquals(8, packs.size());
    }

    /**
     * 平均简单分包(计件）
     */
    @Test
    public void testAverageDivideStrategyWhenSimplePiece() {
        PackAlgorithm algorithm = new SimpleAlgorithm();
        DivideStrategy divideStrategy = new AverageDivideStrategy(algorithm);
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
        RegionTemplate regionTemplate = new PieceTemplate() {
            {
                setUpperLimit(6);
                setTemplateId(17L);
            }
        };
        regionTemplateDao.build(regionTemplate);

        Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);

        assertNotNull(packs);
        // size=16/(16/6+1)=5
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get() <= 5);
        });
        assertEquals(16, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get());
        assertEquals(8, packs.stream().map(pack -> pack.size()).reduce((x, y) -> x + y).get());
        assertEquals(4, packs.size());
    }

}
