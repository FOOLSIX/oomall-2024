package cn.edu.xmu.oomall.order.controller.vo;

import cn.edu.xmu.oomall.order.dao.bo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class SimpleOrderVo {
    private Long id;
    private Byte status;
    private Long shopId;
    private List<Long> productIds;
    private Boolean isComplete;

    public SimpleOrderVo(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.shopId = order.getShopId();
        this.isComplete = Objects.equals(status, Order.COMPLETED);
        //todo:productIds
    }
}
