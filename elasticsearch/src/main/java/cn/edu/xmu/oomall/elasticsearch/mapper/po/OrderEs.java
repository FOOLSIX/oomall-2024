package cn.edu.xmu.oomall.elasticsearch.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Data
@Document(indexName = "order_index")
public class OrderEs implements Serializable {

    @Id
    private Long orderId; // order_order 的唯一 ID

    @Field(type = FieldType.Long)
    private Long customerId; // 客户 ID

    @Field(type = FieldType.Keyword)
    private String orderSn; // 订单编号

    @Field(type = FieldType.Nested) // 嵌套类型，用于存储订单项的详细信息
    private List<OrderItemInfo> orderItems;

    // 内部类定义，表示订单项的信息
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemInfo {
        @Field(type = FieldType.Long)
        private Long orderItemId; // order_item 的 ID

        @Field(type = FieldType.Long)
        private Long productNameId; // 产品名称的唯一 ID
    }
}
