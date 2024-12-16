//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.shop.dao.bo.divide;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.shop.ShopTestApplication;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.dao.bo.template.*;
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
import java.util.stream.Collectors;

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
public class OptimalDivideStrategyTest {

    @Autowired
    RegionTemplateDao regionTemplateDao;

    @MockBean
    RedisUtil redisUtil;

    /**
     * 获取一个其他分包策略的运费用于对比测试
     */
    private Long getOtherStrategyFee(DivideStrategy strategy, RegionTemplate regionTemplate, Collection<ProductItem> items) {
        regionTemplate.getTemplate().setStrategy(strategy);
        Collection<TemplateResult> results = regionTemplate.calculate(items);
        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        return fee;
    }


    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testOptimalDivideStrategyWhenWeighDivideMorePack1() {
        // 常见场景：包裹重量上限能够装下所有商品，但是重量过大时运费较高，算法应该适当拆包
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 5, 1L, 3));
                add(new ProductItem(2L, 2L, 100L, 10, 1L, 2));
                add(new ProductItem(3L, 3L, 100L, 15, 1L, 3));
                add(new ProductItem(4L, 4L, 100L, 20, 1L, 1));
            }
        };
        WeightTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(21L);
                setFirstWeight(1);
                setFirstWeightPrice(100L);
                setThresholds(new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(10, 5L));
                        add(new WeightThresholdPo(50, 10L));
                        add(new WeightThresholdPo(100, 50L));
                    }
                });
            }
        };

        regionTemplateDao.build(weightTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 100);
            // System.out.println(pack);
        });
        assertEquals(100, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(9, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());

        // 预期运费结果
        assertTrue(fee <= 1090);

        // 与(最大分包/平均分包)+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
    }



    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testOptimalDivideStrategyWhenWeighDivideLightPack1() {
        // 包裹运费在重量40-100内较高，算法应该尝试将重量维持在40以下
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 20, 1L, 11));
                add(new ProductItem(2L, 2L, 100L, 30, 1L, 1));
            }
        };
        WeightTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(21L);
                setFirstWeight(10);
                setFirstWeightPrice(1000L);
                setThresholds(new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(40, 1L));
                        add(new WeightThresholdPo(100, 100L));
                    }
                });
            }
        };

        regionTemplateDao.build(weightTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 100);
            // System.out.println(pack);
        });
        assertEquals(250, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        assertTrue(fee <= 7180);

        // 与(最大分包/平均分包)+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
    }



    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testOptimalDivideStrategyWhenWeighDivideLightPack2() {
        // 包裹运费在重量80-100内较高，算法应该尝试将重量维持在80以下
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 20, 1L, 11));
                add(new ProductItem(2L, 2L, 100L, 30, 1L, 1));
            }
        };
        WeightTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(21L);

                setFirstWeight(10);
                setFirstWeightPrice(1000L);
                setThresholds(new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(80, 1L));
                        add(new WeightThresholdPo(100, 10L));
                    }
                });
            }
        };

        regionTemplateDao.build(weightTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 100);
            // System.out.println(pack);
        });
        assertEquals(250, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        System.out.println("fee = " + fee);
        assertTrue(fee <= 3310);

        // 与(最大分包/平均分包)+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
    }



    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testOptimalDivideStrategyWhenWeighDivideMorePack2() {
        // 包裹运费首重相比续重费用较低（现实出现这种情况概率较低），算法应该尽量拆包
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 20, 1L, 11));
                add(new ProductItem(2L, 2L, 100L, 30, 1L, 1));
            }
        };
        WeightTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(21L);

                setFirstWeight(20);
                setFirstWeightPrice(100L);
                setThresholds(new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(100, 100L));
                    }
                });
            }
        };
        regionTemplateDao.build(weightTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 100);
            // System.out.println(pack);
        });
        assertEquals(250, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        assertTrue(fee <= 2200);

        // 与(最大分包/平均分包)+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
    }



    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testOptimalDivideStrategyWhenWeighDivideLessPack1() {
        // 包裹运费首重相比续重费用较高，算法应该尽量装满
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 20, 1L, 3));
                add(new ProductItem(2L, 2L, 100L, 30, 1L, 3));
                add(new ProductItem(3L, 3L, 100L, 70, 1L, 3));
                add(new ProductItem(4L, 4L, 100L, 80, 1L, 3));
            }
        };
        WeightTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(21L);

                setFirstWeight(1);
                setFirstWeightPrice(1000L);
                setThresholds(new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(100, 10L));
                    }
                });
            }
        };

        regionTemplateDao.build(weightTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 100);
            // System.out.println(pack);
        });
        assertEquals(600, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        System.out.println("fee = " + fee);
        assertTrue(fee <= 11940);

        // 与(最大分包/平均分包)+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
    }



    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testOptimalDivideStrategyWhenWeighNoItem() {
        // 空物品
        List<ProductItem> items = new ArrayList<>() {
        };
        WeightTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(21L);

                setFirstWeight(20);
                setFirstWeightPrice(100L);
                setThresholds(new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(100, 100L));
                    }
                });
            }
        };
        regionTemplateDao.build(weightTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        assertEquals(0, packs.size());
    }



    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testOptimalDivideStrategyWhenWeighRandom1() {
        // 随机大数据测试
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 17, 1L, 4));
                add(new ProductItem(2L, 2L, 100L, 6, 1L, 8));
                add(new ProductItem(3L, 3L, 100L, 12, 1L, 3));
                add(new ProductItem(4L, 4L, 100L, 3, 1L, 10));
                add(new ProductItem(5L, 5L, 100L, 50, 1L, 2));
                add(new ProductItem(6L, 6L, 100L, 97, 1L, 1));
                add(new ProductItem(7L, 7L, 100L, 24, 1L, 4));
                add(new ProductItem(8L, 8L, 100L, 60, 1L, 1));
                add(new ProductItem(9L, 9L, 100L, 21, 1L, 2));
            }
        };
        WeightTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(21L);

                setFirstWeight(1);
                setFirstWeightPrice(100L);
                setThresholds(new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(10, 5L));
                        add(new WeightThresholdPo(50, 10L));
                        add(new WeightThresholdPo(100, 50L));
                    }
                });
            }
        };
        regionTemplateDao.build(weightTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 100);
            // System.out.println(pack);
        });
        assertEquals(577, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(35, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        System.out.println("fee = " + fee);
        assertTrue(fee <= 15165);

        // 与最大分包+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
    }



    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testOptimalDivideStrategyWhenWeighRandom2() {
        // 随机大数据测试
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 43, 1L, 4));
                add(new ProductItem(2L, 2L, 100L, 13, 1L, 2));
                add(new ProductItem(3L, 3L, 100L, 10, 1L, 5));
                add(new ProductItem(4L, 4L, 100L, 81, 1L, 2));
                add(new ProductItem(5L, 5L, 100L, 6, 1L, 10));
                add(new ProductItem(6L, 6L, 100L, 2, 1L, 12));
                add(new ProductItem(7L, 7L, 100L, 99, 1L, 2));
                add(new ProductItem(8L, 8L, 100L, 1, 1L, 31));
                add(new ProductItem(9L, 9L, 100L, 13, 1L, 9));
            }
        };
        WeightTemplate weightTemplate = new WeightTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(100);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(21L);

                setFirstWeight(1);
                setFirstWeightPrice(100L);
                setThresholds(new ArrayList<>() {
                    {
                        add(new WeightThresholdPo(10, 5L));
                        add(new WeightThresholdPo(50, 10L));
                        add(new WeightThresholdPo(100, 50L));
                    }
                });
            }
        };

        regionTemplateDao.build(weightTemplate);

        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = weightTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 100);
            // System.out.println(pack);
        });
        assertEquals(840, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(77, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        assertTrue(fee <= 24485);

        // 与最大分包+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), weightTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), weightTemplate, items));
    }

    @Test
    public void testOptimalDivideStrategyWhenPieceNormal1() {
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 1, 1L, 3));
                add(new ProductItem(2L, 2L, 100L, 1, 1L, 3));
                add(new ProductItem(3L, 3L, 100L, 1, 1L, 3));
                add(new ProductItem(4L, 4L, 100L, 1, 1L, 3));
            }
        };
        // 包裹运费首件比续件费用较高，算法应该尽量装满
        PieceTemplate pieceTemplate = new PieceTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(3);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(19L);

                setFirstItems(1);
                setFirstPrice(20L);
                setAdditionalItems(1);
                setAdditionalPrice(10L);
            }
        };
        regionTemplateDao.build(pieceTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = pieceTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(ProductItem::getQuantity).reduce((x, y) -> x + y).get() <= 3);
        });
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(ProductItem::getQuantity).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        assertTrue(fee <= 160);

        // 与最大分包+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), pieceTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), pieceTemplate, items));
    }

    @Test
    public void testOptimalDivideStrategyWhenPieceLessPack1() {
        // 包裹运费价格首件续件一样，算法应该尽量装满
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 1, 1L, 3));
                add(new ProductItem(2L, 2L, 100L, 1, 1L, 3));
                add(new ProductItem(3L, 3L, 100L, 1, 1L, 3));
                add(new ProductItem(4L, 4L, 100L, 1, 1L, 3));
            }
        };
        PieceTemplate pieceTemplate = new PieceTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(3);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(19L);
                setFirstItems(1);
                setFirstPrice(10L);
                setAdditionalItems(1);
                setAdditionalPrice(10L);
            }
        };
        regionTemplateDao.build(pieceTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = pieceTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(ProductItem::getQuantity).reduce((x, y) -> x + y).get() <= 3);
            // System.out.println(pack);
        });
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(ProductItem::getQuantity).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        assertTrue(fee <= 120);

        // 与(最大分包/平均分包)+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), pieceTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new SimpleAlgorithm()), pieceTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), pieceTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new BackPackAlgorithm()), pieceTemplate, items));
    }

    @Test
    public void testOptimalDivideStrategyWhenPieceMorePack1() {
        // 包裹运费价格首件比续件低，算法应该尽量拆包
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 1, 1L, 3));
                add(new ProductItem(2L, 2L, 100L, 1, 1L, 3));
                add(new ProductItem(3L, 3L, 100L, 1, 1L, 3));
                add(new ProductItem(4L, 4L, 100L, 1, 1L, 3));
            }
        };
        PieceTemplate pieceTemplate = new PieceTemplate() {
            {
                setId(1L);
                setUnit(1);
                setRegionId(112L);
                setUpperLimit(12);
                setCreatorId(1L);
                setCreatorName("admin");
                setTemplateId(19L);
                setFirstItems(1);
                setFirstPrice(10L);
                setAdditionalItems(1);
                setAdditionalPrice(20L);
            }
        };
        regionTemplateDao.build(pieceTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Collection<TemplateResult> results = pieceTemplate.calculate(items);

        Long fee = results.stream().mapToLong(TemplateResult::getFee).sum();
        Collection<Collection<ProductItem>> packs = results.stream().map(TemplateResult::getPack).collect(Collectors.toList());

        // 检测正确性
        assertNotNull(packs);
        packs.stream().forEach(pack -> {
                    assertTrue(pack.stream().map(ProductItem::getQuantity).reduce((x, y) -> x + y).get() <= 12);
                    // System.out.println(pack);
                }
        );
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(ProductItem::getQuantity).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        assertEquals(12, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce(Integer::sum).get()).reduce(Integer::sum).get());
        // 预期运费结果
        System.out.println("fee = " + fee);
        assertTrue(fee <= 120);

        // 与(最大分包/平均分包)+(简单算法/背包算法)的组合对比
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new SimpleAlgorithm()), pieceTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new SimpleAlgorithm()), pieceTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new MaxDivideStrategy(new BackPackAlgorithm()), pieceTemplate, items));
        assertTrue(fee <= getOtherStrategyFee(new AverageDivideStrategy(new BackPackAlgorithm()), pieceTemplate, items));
    }
}
