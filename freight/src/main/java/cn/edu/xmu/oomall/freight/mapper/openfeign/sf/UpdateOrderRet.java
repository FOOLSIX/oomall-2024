package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * UpdateOrder响应参数
 *
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@NoArgsConstructor
@Data
public class UpdateOrderRet {

    /**
     * 客户订单号
     */
    @JsonProperty("orderId")
    private String orderId;

    /**
     * 运单列表
     */
    @JsonProperty("waybillNoInfoList")
    private List<WaybillNoInfoListDTO> waybillNoInfoList;

    /**
     * 备注 1:客户订单号与顺丰运单不匹配 2 :操作成功
     */
    @JsonProperty("resStatus")
    private Integer resStatus;

    @NoArgsConstructor
    @Data
    public static class WaybillNoInfoListDTO {
        @JsonProperty("waybillType")
        private Integer waybillType;
        @JsonProperty("waybillNo")
        private String waybillNo;
    }
}
