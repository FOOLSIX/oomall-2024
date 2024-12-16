//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.bo.divide;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.shop.ShopTestApplication;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import cn.edu.xmu.oomall.shop.dao.bo.template.TemplateResult;
import cn.edu.xmu.oomall.shop.dao.bo.template.WeightTemplate;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import cn.edu.xmu.oomall.shop.mapper.po.WeightThresholdPo;
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
public class WeightTemplateTest {

    @Autowired
    RegionTemplateDao regionTemplateDao;

    @MockBean
    private RedisUtil redisUtil;

    @Test
    public void testJsonWhenWeigh() {
        RegionTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setFirstWeight(1);
                setFirstWeightPrice(100L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                List<WeightThresholdPo> threshods = new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(10, 10L));
                        add(new WeightThresholdPo(100, 100L));
                        add(new WeightThresholdPo(200, 200L));
                    }
                };
                setThresholds(threshods);
                setTemplateId(1L);
            }
        };
        regionTemplateDao.build(weightTemplate);

        assertEquals("{\"id\":1,\"creatorId\":1,\"creatorName\":\"admin\",\"upperLimit\":100,\"unit\":1,\"regionId\":112,\"templateId\":1,\"firstWeight\":1,\"firstWeightPrice\":100,\"thresholds\":[{\"below\":10,\"price\":10},{\"below\":100,\"price\":100},{\"below\":200,\"price\":200}],\"templateBean\":\"weightTemplateDao\"}", JacksonUtil.toJson(weightTemplate));
    }

    /**
     * 计重运费计算（最大简单打包）
     */
    @Test
    public void testCalculateWeighWhenMaxSimplePack() {
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
        RegionTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(2);
                setRegionId(112L);
                setFirstWeight(3);
                setFirstWeightPrice(5L);
                setUpperLimit(20);
                setCreatorId(1L);
                setCreatorName("admin");
                List<WeightThresholdPo> threshods = new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(7, 10L));
                        add(new WeightThresholdPo(13, 15L));
                        add(new WeightThresholdPo(20, 20L));
                    }
                };
                setThresholds(threshods);
                setTemplateId(1L);
            }
        };

        regionTemplateDao.build(weightTemplate);

        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        assertNotNull(results);
        results.stream().forEach(result -> {
            assertTrue(result.getPack().stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 20);
        });
        assertEquals(64, results.stream().map(result -> result.getPack().stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get());
        assertEquals(10, results.stream().map(result -> result.getPack().size()).reduce((x, y) -> x + y).get());
        assertEquals(4, results.size());
        assertEquals(420, results.stream().map(result -> result.getFee()).reduce((x, y) -> x + y).get());
    }
}
