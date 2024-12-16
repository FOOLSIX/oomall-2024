package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchOrder返回参数
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@NoArgsConstructor
@Data
public class SearchOrderRet {

    /**
     * 目的地区域代码
     */
    @JsonProperty("destcode")
    private String destcode;

    /**
     * 筛单结果：1：人工确认 2：可收派 3：不可以收派
     */
    @JsonProperty("filterResult")
    private String filterResult;

    /**
     * 客户订单号
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * 原寄地区域代码，可用于顺丰电子运单标签打印
     */
    @JsonProperty("origincode")
    private String origincode;

    /**
     * 路由标签数据列表
     */
    @JsonProperty("routeLabelInfo")
    private List<RouteLabelInfoDTO> routeLabelInfo;

    /**
     * 顺丰运单号列表
     */
    @JsonProperty("waybillNoInfoList")
    private List<WaybillNoInfoListDTO> waybillNoInfoList;


    @NoArgsConstructor
    @Data
    public static class RouteLabelInfoDTO {
        @JsonProperty("code")
        private String code;
    }

    @NoArgsConstructor
    @Data
    public static class WaybillNoInfoListDTO {
        /**
         * 运单号
         */
        @JsonProperty("waybillNo")
        private String waybillNo;
        /**
         * 运单号类型：1（母单）、2（子单）、3（签回单）
         */
        @JsonProperty("waybillType")
        private Integer waybillType;
    }
}
