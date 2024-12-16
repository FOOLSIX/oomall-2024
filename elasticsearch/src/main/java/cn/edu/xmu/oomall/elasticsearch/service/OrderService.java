package cn.edu.xmu.oomall.elasticsearch.service;

import cn.edu.xmu.oomall.elasticsearch.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    public List<Long> searchOrderIds(String itemName, Long customerId, int page, int size) throws IOException {
        // 获取模糊匹配的 productNameId 列表
        List<Long> productNameIds = orderMapper.getProductNameIdsByName(itemName);
        if (productNameIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 根据 customerId 和 productNameIds 查询匹配的订单ID列表
        List<Long> orderIds = orderMapper.searchOrderIdsByCustomerIdAndProductNameIds(customerId, productNameIds);

        // 分页处理：这里使用 Java 8 的 Stream 来模拟分页效果
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, orderIds.size());
        return orderIds.subList(startIndex, endIndex);

    }
}
