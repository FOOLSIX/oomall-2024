package cn.edu.xmu.oomall.shop.dao.bo.divide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SuperBackPackAlgorithm implements PackAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(SuperBackPackAlgorithm.class);

    @Override
    public Collection<Item> pack(Collection<Item> items, Integer packageSize) {
        logger.debug("pack: packageSize = {}, items = {}", packageSize, items);

        Item[] arrayItems = items.toArray(Item[]::new);
        logger.debug("create a new pack");
        int arrayLength = arrayItems.length;

        // 使用 Map 来存储物品和数量，避免重复遍历
        Map<Item, Integer> itemCountMap = new HashMap<>();

        // 滚动数组优化空间复杂度，从原来的O(mn)->O(n)
        int[] dp = new int[packageSize + 1];  // dp[j]表示容量为j的背包能获得的最大价值
        int[] path = new int[packageSize + 1]; // 记录最后一个导致状态改变的物品索引

        // 从前往后遍历每个物品
        for (int i = 1; i <= arrayLength; i++) {
            int weight = arrayItems[i - 1].getCount();
            for (int j = packageSize; j >= weight; j--) {
                int newValue = dp[j - weight] + weight;
                if (newValue > dp[j]) {
                    dp[j] = newValue;
                    path[j] = i;
                }
            }
        }

        int remainWeight = packageSize;

        while (remainWeight > 0 && dp[remainWeight] > 0) {
            int itemIndex = path[remainWeight] - 1;
            Item item = arrayItems[itemIndex];
            logger.debug("pack: add item {}", item);

            // 检查 Map 中是否已存在此物品
            itemCountMap.put(item, itemCountMap.getOrDefault(item, 0) + 1);  // 如果存在，则数量增加
            items.remove(item);  // 从 items 中移除已打包物品
            remainWeight -= item.getCount();  // 减少剩余容量
        }

        // 将 itemCountMap 转换成最终的物品集合
        Set<Item> newPack = new HashSet<>();
        for (Map.Entry<Item, Integer> entry : itemCountMap.entrySet()) {
            Item item = entry.getKey();
            int count = entry.getValue();
            item.setQuantity(count);
            newPack.add(item);  // 添加物品到最终背包中
        }

        logger.debug("newPack: {}", newPack);
        return newPack;
    }
}
