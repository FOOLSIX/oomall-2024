package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchRoute请求参数
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@NoArgsConstructor
@Data
public class SearchRoutesParam {

    @JsonProperty("language")
    private String language = "zh-CN";

    /**
     * 查询号类别:
     * 1:根据顺丰运单号查询,trackingNumber将被当作顺丰运单号处理
     * 2:根据客户订单号查询,trackingNumber将被当作客户订单号处理
     */
    @JsonProperty("trackingType")
    private String trackingType;

    /**
     * 查询号:
     * trackingType=1,则此值为顺丰运单号
     * 如果trackingType=2,则此值为客户订单号
     */
    @JsonProperty("trackingNumber")
    private List<String> trackingNumber;
}
