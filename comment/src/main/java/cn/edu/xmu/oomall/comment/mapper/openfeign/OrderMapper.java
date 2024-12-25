package cn.edu.xmu.oomall.comment.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.comment.mapper.openfeign.po.OrderPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("order-service")
public interface OrderMapper {

    @GetMapping("/internal/orders/{id}/status")
    InternalReturnObject<OrderPo> getOrderById(@PathVariable Long id);

}
