package cn.edu.xmu.oomall.order.mapper;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("search-service")
public interface SearchMapper {

    /**
     * 调用 searchOrders 接口，按条件查询订单ID
     *
     * @param itemName 商品名称
     * @param customerId 客户ID
     * @param page 页码
     * @param size 每页大小
     * @return 返回订单ID列表
     */
    @GetMapping("/search/orders")
    InternalReturnObject<List<Long>> searchOrders(
            @RequestParam("itemName") String itemName,
            @RequestParam("customerId") Long customerId,
            @RequestParam("page") int page,
            @RequestParam("size") int size);
}
