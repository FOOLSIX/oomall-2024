package cn.edu.xmu.oomall.sfexpress.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * UpdateOrder请求参数
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostUpdateOrderDTO {

    /**
     * 客户订单操作标识: 1:确认  2:取消
     */
    @JsonProperty("dealType")
    private Integer dealType;

    /**
     * 客户订单号
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * 客户订单货物总高，单位厘米， 精确到小数点后3位，包含子 母件
     */
    @JsonProperty("totalHeight")
    private Double totalHeight;

    /**
     * 	客户订单货物总长，单位厘米， 精确到小数点后3位，包含子 母件
     */
    @JsonProperty("totalLength")
    private Double totalLength;

    /**
     * 订单货物总体积，单位立方厘 米，精确到小数点后3位，会 用于计抛（是否计抛具体商务 沟通中双方约定）
     */
    @JsonProperty("totalVolume")
    private Double totalVolume;

    /**
     * 订单货物总重量，包含子母 件，单位千克，精确到小数点 后3位，如果提供此值，必 须>0
     */
    @JsonProperty("totalWeight")
    private Double totalWeight;

    /**
     * 客户订单货物总宽，单位厘米， 精确到小数点后3位，包含子 母件
     */
    @JsonProperty("totalWidth")
    private Double totalWidth;

    /**
     * 顺丰运单号(如dealtype=1， 必填)
     */
    @JsonProperty("waybillNoInfoList")
    private List<WaybillNoInfoListDTO> waybillNoInfoList;

    @JsonProperty("sendStartTm")
    private Timestamp sendStartTm;

    @JsonProperty("pickupAppointEndtime")
    private Timestamp pickupAppointEndtime;

    @NoArgsConstructor
    @Data
    public static class WaybillNoInfoListDTO {
        /**
         * 运单号
         */
        @JsonProperty("waybillNo")
        private String waybillNo;

        /**
         * 运单号类型 1：母单 2 :子单 3 : 签回单
         */
        @JsonProperty("waybillType")
        private Integer waybillType;
    }
}
