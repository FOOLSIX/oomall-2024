package cn.edu.xmu.oomall.shop.dao.bo.divide;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.shop.ShopTestApplication;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.dao.bo.template.PieceTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import cn.edu.xmu.oomall.shop.dao.bo.template.WeightTemplate;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
public class GreedyAverageDivideStrategyTest {

    @Autowired
    RegionTemplateDao regionTemplateDao;

    @MockBean
    RedisUtil redisUtil;
    /**
     * 贪心分包（计重）
     */
    @Test
    public void testGreedyAverageDivideStrategyWhenWeigh() {
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
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
                setTemplateId(18L);
            }
        };

        regionTemplateDao.build(regionTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);


        Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
        int baseline = 0;
        for (Collection<ProductItem> pack : packs) {
            baseline += pack.stream().mapToInt(item -> item.getWeight() * item.getQuantity()).sum();
        }
        baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);

        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get() <= 12);
        });
        assertEquals(64, packs.stream().map(pack -> pack.stream().map(item -> item.getWeight() * item.getQuantity()).reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get());
        assertEquals(16, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get());
        assertEquals(6, packs.size());
    }

    /**
     * 贪心分包（计件）
     */

    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testGreedyAverageDivideStrategyWhenPiece() {
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        List<ProductItem> items = new ArrayList<>() {
            {
                add(new ProductItem(1L, 1L, 100L, 100, 1L, 1));
                add(new ProductItem(2L, 2L, 100L, 10, 1L, 2));
                add(new ProductItem(3L, 3L, 100L, 12, 1L, 3));
                add(new ProductItem(4L, 4L, 100L, 13, 1L, 4));
                add(new ProductItem(5L, 5L, 100L, 45, 1L, 3));
                add(new ProductItem(6L, 6L, 100L, 64, 1L, 2));
                add(new ProductItem(7L, 7L, 100L, 75, 1L, 1));
            }
        };
        RegionTemplate regionTemplate = new PieceTemplate() {
            {
                setUpperLimit(6);
                setTemplateId(20L);
            }
        };
        regionTemplateDao.build(regionTemplate);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);


        Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
        int baseline = 0;
        for (Collection<ProductItem> pack : packs) {
            baseline += pack.stream().mapToInt(item -> item.getQuantity()).sum();
        }
        baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);

        assertNotNull(packs);
        packs.stream().forEach(pack -> {
            assertTrue(pack.stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get() <= 6);
        });
        assertEquals(16, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get());
        assertEquals(16, packs.stream().map(pack -> pack.stream().map(item -> item.getQuantity()).reduce((x, y) -> x + y).get()).reduce((x, y) -> x + y).get());
        assertEquals(3, packs.size());

    }

    /**
     * 贪心分包与最大简单分包比较（计重）
     * 限制最大重量
     */

    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testGreedyAverageDivideStrategyWhenCmpMaxSimpleWeigh() {

        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        Random random = new Random();
        int greedy_win = 0;
        int max_win = 0;
        for (int i = 0; i < 10; i++) {
            List<ProductItem> items = new ArrayList<>() {
                {
                    add(new ProductItem(1L, 1L, 100L, random.nextInt(3), 1L, random.nextInt(10)));
                    add(new ProductItem(2L, 2L, 100L, random.nextInt(3) + 3, 1L, random.nextInt(10)));
                    add(new ProductItem(3L, 3L, 100L, random.nextInt(3) + 6, 1L, random.nextInt(10)));
                    add(new ProductItem(4L, 4L, 100L, random.nextInt(3) + 9, 1L, random.nextInt(10)));
                    add(new ProductItem(5L, 5L, 100L, random.nextInt(3) + 12, 1L, random.nextInt(10)));
                    add(new ProductItem(6L, 6L, 100L, random.nextInt(3) + 15, 1L, random.nextInt(10)));
                    add(new ProductItem(7L, 7L, 100L, random.nextInt(3) + 18, 1L, random.nextInt(10)));
                }
            };
            RegionTemplate regionTemplate = new WeightTemplate() {
                {
                    setUpperLimit(20);
                    setTemplateId(18L);
                }
            };
            regionTemplateDao.build(regionTemplate);
            Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
            Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

            Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
            int baseline = 0;
            for (Collection<ProductItem> pack : packs) {
                baseline += pack.stream().mapToInt(item -> item.getWeight() * item.getQuantity()).sum();
            }
            baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);
            assertNotNull(packs);

            //和MaxDivide进行比较
            PackAlgorithm algorithm = new SimpleAlgorithm();
            DivideStrategy max = new MaxDivideStrategy((algorithm));
            Collection<Collection<ProductItem>> packs2 = max.divide(regionTemplate, items);

            int loss_greedyaverage = 0;
            int loss_average = 0;
            for (Collection<ProductItem> pack : packs) {
                loss_greedyaverage += Math.abs(pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum() - baseline);

            }
            for (Collection<ProductItem> pack : packs2) {
                loss_average += Math.abs(pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum() - baseline);
            }
            if (packs.size() < packs2.size()) {
                greedy_win += 1;//我们的算法得到的包裹数小于其他算法
            } else if (packs.size() > packs2.size()) {
                max_win += 1;
            } else {
                if (loss_greedyaverage < loss_average) {
                    greedy_win += 1;//贪心重量算法获胜
                } else if (loss_average < loss_greedyaverage) {
                    max_win += 1;//最大重量贪心获胜
                }
            }
        }
        //可能存在两种算法得到同样的loss
        System.out.println(greedy_win);
        System.out.println(max_win);
    }

    /**
     * 贪心分包与最大简单分包比较（计件）
     * 限制最大件数
     */

    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testGreedyAverageDivideStrategyWhenCmpMaxSimplePiece() {
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        Random random = new Random();
        int greedy_win = 0;
        int max_win = 0;
        for (int i = 0; i < 10; i++) {
            List<ProductItem> items = new ArrayList<>() {
                {
                    add(new ProductItem(1L, 1L, 100L, null, 1L, random.nextInt(10)));
                    add(new ProductItem(2L, 2L, 100L, null, 1L, random.nextInt(10)));
                    add(new ProductItem(3L, 3L, 100L, null, 1L, random.nextInt(10)));
                    add(new ProductItem(4L, 4L, 100L, null, 1L, random.nextInt(10)));
                    add(new ProductItem(5L, 5L, 100L, null, 1L, random.nextInt(10)));
                    add(new ProductItem(6L, 6L, 100L, null, 1L, random.nextInt(10)));
                    add(new ProductItem(7L, 7L, 100L, null, 1L, random.nextInt(10)));
                }
            };
            RegionTemplate regionTemplate = new PieceTemplate() {
                {
                    setUpperLimit(6);
                    setTemplateId(20L);
                }
            };
            regionTemplateDao.build(regionTemplate);

            Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
            int baseline = 0;
            for (Collection<ProductItem> pack : packs) {
                baseline += pack.stream().mapToInt(item -> item.getQuantity()).sum();
            }
            baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);
            assertNotNull(packs);

            //和MaxDivide进行比较
            PackAlgorithm algorithm = new SimpleAlgorithm();
            DivideStrategy max = new MaxDivideStrategy((algorithm));
            Collection<Collection<ProductItem>> packs2 = max.divide(regionTemplate, items);

            int loss_greedyaverage = 0;
            int loss_average = 0;
            for (Collection<ProductItem> pack : packs) {
                loss_greedyaverage += Math.abs(pack.stream().mapToInt(item -> item.getQuantity()).sum() - baseline);
            }
            for (Collection<ProductItem> pack : packs2) {
                loss_average += Math.abs(pack.stream().mapToInt(item -> item.getQuantity()).sum() - baseline);
            }
            if (packs.size() < packs2.size()) {
                greedy_win += 1;//我们的算法得到的包裹数小于其他算法
            } else if (packs.size() > packs2.size()) {
                max_win += 1;
            } else {
                if (loss_greedyaverage < loss_average) {
                    greedy_win += 1;//贪心重量算法获胜
                } else if (loss_average < loss_greedyaverage) {
                    max_win += 1;//最大重量贪心获胜
                }
            }
        }
    }

    /**
     * 贪心分包与平均简单分包比较（计件）
     * 限制最大件数
     */

    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testGreedyAverageDivideStrategyWhenCmpAverageSimplePiece() {
        int greedy_win = 0;
        int average_win = 0;
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            List<ProductItem> items = new ArrayList<>() {
                {
                    add(new ProductItem(1L, 1L, 100L, 12, 1L, random.nextInt(10)));
                    add(new ProductItem(2L, 2L, 100L, 111, 1L, random.nextInt(10)));
                    add(new ProductItem(3L, 3L, 100L, 222, 1L, random.nextInt(10)));
                    add(new ProductItem(4L, 4L, 100L, 2, 1L, random.nextInt(10)));
                    add(new ProductItem(5L, 5L, 100L, 22, 1L, random.nextInt(10)));
                    add(new ProductItem(6L, 6L, 100L, 44, 1L, random.nextInt(10)));
                    add(new ProductItem(7L, 7L, 100L, 55, 1L, random.nextInt(10)));
                }
            };
            RegionTemplate regionTemplate = new PieceTemplate() {
                {
                    setUpperLimit(6);
                    setTemplateId(20L);
                }
            };
            regionTemplateDao.build(regionTemplate);
            Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
            Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

            Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
            int baseline = 0;
            for (Collection<ProductItem> pack : packs) {
                baseline += pack.stream().mapToInt(item -> item.getQuantity()).sum();
            }
            baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);
            assertNotNull(packs);

            PackAlgorithm algorithm = new SimpleAlgorithm();
            DivideStrategy average = new AverageDivideStrategy((algorithm));
            //和AverageDivide进行比较
            Collection<Collection<ProductItem>> packs2 = average.divide(regionTemplate, items);

            int loss_greedyaverage = 0;
            int loss_average = 0;
            for (Collection<ProductItem> pack : packs) {
                loss_greedyaverage += Math.abs(pack.stream().mapToInt(item -> item.getQuantity()).sum() - baseline);
            }
            for (Collection<ProductItem> pack : packs2) {
                loss_average += Math.abs(pack.stream().mapToInt(item -> item.getQuantity()).sum() - baseline);
            }
            if (packs.size() < packs2.size()) {
                greedy_win += 1;//我们的算法得到的包裹数小于其他算法
            } else {
                if (loss_greedyaverage < loss_average) {
                    greedy_win += 1;//贪心重量算法获胜
                } else if (loss_average < loss_greedyaverage) {
                    average_win += 1;//最大重量贪心获胜
                }
            }
        }
    }

    /**
     * 贪心分包与平均简单分包比较（计重）
     * 限制最大重量
     */
    @Test
    public void testGreedyAverageDivideStrategyWhenCmpAverageSimpleWeigh() {
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        Random random = new Random();
        int greedy_win = 0;
        int average_win = 0;
        for (int i = 0; i < 10; i++) {
            List<ProductItem> items = new ArrayList<>() {
                {
                    add(new ProductItem(1L, 1L, 100L, random.nextInt(3), 1L, random.nextInt(10)));
                    add(new ProductItem(2L, 2L, 100L, random.nextInt(3) + 3, 1L, random.nextInt(10)));
                    add(new ProductItem(3L, 3L, 100L, random.nextInt(3) + 6, 1L, random.nextInt(10)));
                    add(new ProductItem(4L, 4L, 100L, random.nextInt(3) + 9, 1L, random.nextInt(10)));
                    add(new ProductItem(5L, 5L, 100L, random.nextInt(3) + 12, 1L, random.nextInt(10)));
                    add(new ProductItem(6L, 6L, 100L, random.nextInt(3) + 15, 1L, random.nextInt(10)));
                    add(new ProductItem(7L, 7L, 100L, random.nextInt(3) + 18, 1L, random.nextInt(10)));
                }
            };
            RegionTemplate regionTemplate = new WeightTemplate() {
                {
                    setUpperLimit(10);
                    setTemplateId(18L);
                }
            };
            regionTemplateDao.build(regionTemplate);
            Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
            Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

            Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
            int baseline = 0;
            for (Collection<ProductItem> pack : packs) {
                baseline += pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum();
            }
            baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);
            assertNotNull(packs);

            PackAlgorithm algorithm = new SimpleAlgorithm();
            DivideStrategy average = new AverageDivideStrategy((algorithm));
            //和AverageDivide进行比较
            Collection<Collection<ProductItem>> packs2 = average.divide(regionTemplate, items);

            int loss_greedyaverage = 0;
            int loss_average = 0;
            for (Collection<ProductItem> pack : packs) {
                loss_greedyaverage += Math.abs(pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum() - baseline);
            }
            for (Collection<ProductItem> pack : packs2) {
                loss_average += Math.abs(pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum() - baseline);
            }
            if (packs.size() < packs2.size()) {
                greedy_win += 1;//我们的算法得到的包裹数小于其他算法
            } else {
                if (loss_greedyaverage < loss_average) {
                    greedy_win += 1;//贪心重量算法获胜
                } else if (loss_average < loss_greedyaverage) {
                    average_win += 1;//最大重量贪心获胜
                }
            }
        }
    }

    /**
     * 贪心分包与平均背包分包比较（计重）
     * 限制最大重量
     */
    @Test
    public void testGreedyAverageDivideStrategyWhenCmpAverageBackPackWeigh() {
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        Random random = new Random();
        int greedy_win = 0;
        int average_win = 0;
        for (int i = 0; i < 10; i++) {
            List<ProductItem> items = new ArrayList<>() {
                {
                    add(new ProductItem(1L, 1L, 100L, random.nextInt(3), 1L, random.nextInt(10)));
                    add(new ProductItem(2L, 2L, 100L, random.nextInt(3) + 3, 1L, random.nextInt(10)));
                    add(new ProductItem(3L, 3L, 100L, random.nextInt(3) + 6, 1L, random.nextInt(10)));
                    add(new ProductItem(4L, 4L, 100L, random.nextInt(3) + 9, 1L, random.nextInt(10)));
                    add(new ProductItem(5L, 5L, 100L, random.nextInt(3) + 12, 1L, random.nextInt(10)));
                    add(new ProductItem(6L, 6L, 100L, random.nextInt(3) + 15, 1L, random.nextInt(10)));
                    add(new ProductItem(7L, 7L, 100L, random.nextInt(3) + 18, 1L, random.nextInt(10)));
                }
            };
            RegionTemplate regionTemplate = new WeightTemplate() {
                {
                    setUpperLimit(20);
                    setTemplateId(18L);
                }
            };
            regionTemplateDao.build(regionTemplate);
            Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
            Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);


            Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
            int baseline = 0;
            for (Collection<ProductItem> pack : packs) {
                baseline += pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum();
            }
            baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);
            assertNotNull(packs);

            PackAlgorithm algorithm = new BackPackAlgorithm();
            DivideStrategy average = new AverageDivideStrategy((algorithm));
            //和AverageDivide进行比较
            Collection<Collection<ProductItem>> packs2 = average.divide(regionTemplate, items);

            int loss_greedyaverage = 0;
            int loss_average = 0;
            for (Collection<ProductItem> pack : packs) {
                loss_greedyaverage += Math.abs(pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum() - baseline);
            }
            for (Collection<ProductItem> pack : packs2) {
                loss_average += Math.abs(pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum() - baseline);
            }
            if (packs.size() < packs2.size()) {
                greedy_win += 1;//我们的算法得到的包裹数小于其他算法
            } else {
                if (loss_greedyaverage < loss_average) {
                    greedy_win += 1;//贪心重量算法获胜
                } else if (loss_average < loss_greedyaverage) {
                    average_win += 1;//最大重量贪心获胜
                }
            }
        }
    }

    /**
     * 贪心分包与平均背包分包比较（计件）
     * 限制最大件数
     */

    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testGreedyAverageDivideStrategyWhenCmpAverageBackPackPiece() {
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        Random random = new Random();
        int greedy_win = 0;
        int average_win = 0;
        for (int i = 0; i < 10; i++) {
            List<ProductItem> items = new ArrayList<>() {
                {
                    add(new ProductItem(1L, 1L, 100L, 10, 1L, random.nextInt(10)));
                    add(new ProductItem(2L, 2L, 100L, 12, 1L, random.nextInt(10)));
                    add(new ProductItem(3L, 3L, 100L, 134, 1L, random.nextInt(10)));
                    add(new ProductItem(4L, 4L, 100L, 13, 1L, random.nextInt(10)));
                    add(new ProductItem(5L, 5L, 100L, 44, 1L, random.nextInt(10)));
                    add(new ProductItem(6L, 6L, 100L, 22, 1L, random.nextInt(10)));
                    add(new ProductItem(7L, 7L, 100L, 14, 1L, random.nextInt(10)));
                }
            };
            RegionTemplate regionTemplate = new PieceTemplate() {
                {
                    setUpperLimit(6);
                    setTemplateId(20L);
                }
            };
            regionTemplateDao.build(regionTemplate);
            Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
            Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);


            Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
            int baseline = 0;
            for (Collection<ProductItem> pack : packs) {
                baseline += pack.stream().mapToInt(item -> item.getQuantity()).sum();
            }
            baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);
            assertNotNull(packs);

            PackAlgorithm algorithm = new BackPackAlgorithm();
            DivideStrategy average = new AverageDivideStrategy((algorithm));
            //和AverageDivide进行比较
            Collection<Collection<ProductItem>> packs2 = average.divide(regionTemplate, items);

            int loss_greedyaverage = 0;
            int loss_average = 0;
            for (Collection<ProductItem> pack : packs) {
                loss_greedyaverage += Math.abs(pack.stream().mapToInt(item -> item.getQuantity()).sum() - baseline);
            }
            for (Collection<ProductItem> pack : packs2) {
                loss_average += Math.abs(pack.stream().mapToInt(item -> item.getQuantity()).sum() - baseline);
            }
            if (packs.size() < packs2.size()) {
                greedy_win += 1;//我们的算法得到的包裹数小于其他算法
            } else {
                if (loss_greedyaverage < loss_average) {
                    greedy_win += 1;//贪心重量算法获胜
                } else if (loss_average < loss_greedyaverage) {
                    average_win += 1;//最大重量贪心获胜
                }
            }
        }
    }

    /**
     * 贪心分包与最大背包分包比较（计重）
     * 限制最大重量
     */
    @Test
    public void testGreedyAverageDivideStrategyWhenCmpMaxBackPackWeigh() {
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        Random random = new Random();
        int greedy_win = 0;
        int max_win = 0;
        for (int i = 0; i < 10; i++) {
            List<ProductItem> items = new ArrayList<>() {
                {
                    add(new ProductItem(1L, 1L, 100L, random.nextInt(3), 1L, random.nextInt(10)));
                    add(new ProductItem(2L, 2L, 100L, random.nextInt(3) + 3, 1L, random.nextInt(10)));
                    add(new ProductItem(3L, 3L, 100L, random.nextInt(3) + 6, 1L, random.nextInt(10)));
                    add(new ProductItem(4L, 4L, 100L, random.nextInt(3) + 9, 1L, random.nextInt(10)));
                    add(new ProductItem(5L, 5L, 100L, random.nextInt(3) + 12, 1L, random.nextInt(10)));
                    add(new ProductItem(6L, 6L, 100L, random.nextInt(3) + 15, 1L, random.nextInt(10)));
                    add(new ProductItem(7L, 7L, 100L, random.nextInt(3) + 18, 1L, random.nextInt(10)));
                }
            };
            RegionTemplate regionTemplate = new WeightTemplate() {
                {
                    setUpperLimit(20);
                    setTemplateId(18L);
                }
            };
            regionTemplateDao.build(regionTemplate);
            Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
            Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

            Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
            int baseline = 0;
            for (Collection<ProductItem> pack : packs) {
                baseline += pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum();
            }
            baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);
            assertNotNull(packs);

            PackAlgorithm algorithm = new BackPackAlgorithm();
            DivideStrategy max = new MaxDivideStrategy((algorithm));
            //和MaxDivide进行比较
            Collection<Collection<ProductItem>> packs2 = max.divide(regionTemplate, items);

            int loss_greedyaverage = 0;
            int loss_average = 0;
            for (Collection<ProductItem> pack : packs) {
                loss_greedyaverage += Math.abs(pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum() - baseline);
            }
            for (Collection<ProductItem> pack : packs2) {
                loss_average += Math.abs(pack.stream().mapToInt(item -> item.getQuantity() * item.getWeight()).sum() - baseline);
            }
            if (packs.size() < packs2.size()) {
                greedy_win += 1;//我们的算法得到的包裹数小于其他算法
            } else if (packs.size() > packs2.size()) {
                max_win += 1;
            } else {
                if (loss_greedyaverage < loss_average) {
                    greedy_win += 1;//贪心重量算法获胜
                } else if (loss_average < loss_greedyaverage) {
                    max_win += 1;//最大重量贪心获胜
                }
            }
        }
    }

    /**
     * 贪心分包与最大背包分包比较（计件）
     * 限制最大件数
     */

    /**
     * 37220222203708
     * 修改setTemplateId()
     */

    @Test
    public void testGreedyAverageDivideStrategyWhenCmpMaxBackPackPiece() {
        DivideStrategy divideStrategy = new GreedyAverageDivideStrategy(null);
        Random random = new Random();
        int greedy_win = 0;
        int max_win = 0;
        for (int i = 0; i < 10; i++) {
            List<ProductItem> items = new ArrayList<>() {
                {
                    add(new ProductItem(1L, 1L, 100L, 22, 1L, random.nextInt(10)));
                    add(new ProductItem(2L, 2L, 100L, 33, 1L, random.nextInt(10)));
                    add(new ProductItem(3L, 3L, 100L, 4, 1L, random.nextInt(10)));
                    add(new ProductItem(4L, 4L, 100L, 55, 1L, random.nextInt(10)));
                    add(new ProductItem(5L, 5L, 100L, 66, 1L, random.nextInt(10)));
                    add(new ProductItem(6L, 6L, 100L, 77, 1L, random.nextInt(10)));
                    add(new ProductItem(7L, 7L, 100L, 88, 1L, random.nextInt(10)));
                }
            };
            RegionTemplate regionTemplate = new PieceTemplate() {
                {
                    setUpperLimit(6);
                    setTemplateId(20L);
                }
            };
            regionTemplateDao.build(regionTemplate);

            Collection<Collection<ProductItem>> packs = divideStrategy.divide(regionTemplate, items);
            int baseline = 0;
            for (Collection<ProductItem> pack : packs) {
                baseline += pack.stream().mapToInt(item -> item.getQuantity()).sum();
            }
            baseline = (baseline % packs.size() == 0) ? baseline / packs.size() : (baseline / packs.size() + 1);
            assertNotNull(packs);

            PackAlgorithm algorithm = new BackPackAlgorithm();
            DivideStrategy max = new MaxDivideStrategy((algorithm));
            //和MaxDivide进行比较
            Collection<Collection<ProductItem>> packs2 = max.divide(regionTemplate, items);

            int loss_greedyaverage = 0;
            int loss_average = 0;
            for (Collection<ProductItem> pack : packs) {
                loss_greedyaverage += Math.abs(pack.stream().mapToInt(item -> item.getQuantity()).sum() - baseline);
            }
            for (Collection<ProductItem> pack : packs2) {
                loss_average += Math.abs(pack.stream().mapToInt(item -> item.getQuantity()).sum() - baseline);
            }
            if (packs.size() < packs2.size()) {
                greedy_win += 1;//我们的算法得到的包裹数小于其他算法
            } else if (packs.size() > packs2.size()) {
                max_win += 1;
            } else {
                if (loss_greedyaverage < loss_average) {
                    greedy_win += 1;//贪心重量算法获胜
                } else if (loss_average < loss_greedyaverage) {
                    max_win += 1;//最大重量贪心获胜
                }
            }
        }
        //可能存在两种算法得到同样的loss
    }
}
