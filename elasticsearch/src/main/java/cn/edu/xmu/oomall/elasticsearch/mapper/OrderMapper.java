package cn.edu.xmu.oomall.elasticsearch.mapper;

import cn.edu.xmu.oomall.elasticsearch.mapper.po.OrderEs;
import cn.edu.xmu.oomall.elasticsearch.mapper.po.OrderItemNameEs;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ElasticsearchClient elasticsearchClient;

    /**
     * 根据名称获取产品名称的 ID 列表
     *
     * @param name 产品名称
     * @return 产品名称 ID 列表
     */
    public List<Long> getProductNameIdsByName(String name) throws IOException {
        SearchResponse<OrderItemNameEs> response = elasticsearchClient.search(s -> s
                        .index("order_item_name_index")
                        .size(1000)
                        .query(q -> q
                                .match(m -> m
                                        .field("name")
                                        .query(name)
                                        .operator(Operator.Or)
                                )
                        ),
                OrderItemNameEs.class
        );

        return response.hits().hits().stream()
                .map(Hit::source).filter(Objects::nonNull)
                .map(OrderItemNameEs::getProductNameId)
                .collect(Collectors.toList());
    }

    /**
     * 根据客户 ID 和产品名称 ID 列表查询订单 ID
     *
     * @param customerId     客户 ID
     * @param productNameIds 产品名称 ID 列表
     * @return 订单 ID 列表
     */
    public List<Long> searchOrderIdsByCustomerIdAndProductNameIds(Long customerId, List<Long> productNameIds) throws IOException {
        if (productNameIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 创建查询条件
        Query customerIdQuery = Query.of(q -> q
                .term(t -> t
                        .field("customerId")
                        .value(FieldValue.of(customerId))
                )
        );

        List<FieldValue> productNameIdList = productNameIds.stream()
                .map(FieldValue::of)
                .toList();

        Query productNameIdQuery = Query.of(q -> q
                .nested(n -> n
                        .path("orderItems")
                        .query(nq -> nq
                                .terms(t -> t
                                        .field("orderItems.productNameId")
                                        .terms(new TermsQueryField.Builder()
                                                .value(productNameIdList).build())
                                )
                        )
                )
        );

        // 布尔查询条件
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        boolQuery.must(customerIdQuery);
        boolQuery.must(productNameIdQuery);


        // 执行查询
        SearchResponse<OrderEs> response = elasticsearchClient.search(s -> s
                        .index("order_index")
                        .query(q -> q.bool(boolQuery.build()))
                        .size(1000), // 返回最多1000个订单
                OrderEs.class
        );

        // 提取订单ID
        return response.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(OrderEs::getOrderId)
                .collect(Collectors.toList());

    }
}
