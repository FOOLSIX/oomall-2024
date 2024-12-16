package cn.edu.xmu.oomall.sfexpress.controller.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchOrder返回参数
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostSearchOrderRetVo {

    @JsonProperty("destcode")
    private String destcode;
    @JsonProperty("filterResult")
    private String filterResult;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("origincode")
    private String origincode;
    @JsonProperty("routeLabelInfo")
    private List<RouteLabelInfoDTO> routeLabelInfo;
    @JsonProperty("waybillNoInfoList")
    private List<WaybillNoInfoListDTO> waybillNoInfoList;

    @NoArgsConstructor
    @Data
    public static class RouteLabelInfoDTO {
        @JsonProperty("code")
        private String code;
        @JsonProperty("message")
        private String message;
        @JsonProperty("routeLabelData")
        private RouteLabelDataDTO routeLabelData;

        @NoArgsConstructor
        @Data
        public static class RouteLabelDataDTO {
            @JsonProperty("abFlag")
            private String abFlag;
            @JsonProperty("cargoTypeCode")
            private String cargoTypeCode;
            @JsonProperty("checkCode")
            private String checkCode;
            @JsonProperty("codingMapping")
            private String codingMapping;
            @JsonProperty("codingMappingOut")
            private String codingMappingOut;
            @JsonProperty("currencySymbol")
            private String currencySymbol;
            @JsonProperty("cusBatch")
            private String cusBatch;
            @JsonProperty("destCityCode")
            private String destCityCode;
            @JsonProperty("destCountry")
            private String destCountry;
            @JsonProperty("destDeptCode")
            private String destDeptCode;
            @JsonProperty("destDeptCodeMapping")
            private String destDeptCodeMapping;
            @JsonProperty("destGisDeptCode")
            private String destGisDeptCode;
            @JsonProperty("destPortCode")
            private String destPortCode;
            @JsonProperty("destPostCode")
            private String destPostCode;
            @JsonProperty("destRouteLabel")
            private String destRouteLabel;
            @JsonProperty("destTeamCode")
            private String destTeamCode;
            @JsonProperty("destTeamCodeMapping")
            private String destTeamCodeMapping;
            @JsonProperty("destTransferCode")
            private String destTransferCode;
            @JsonProperty("errMsg")
            private String errMsg;
            @JsonProperty("expressTypeCode")
            private String expressTypeCode;
            @JsonProperty("fbaIcon")
            private String fbaIcon;
            @JsonProperty("fileIcon")
            private String fileIcon;
            @JsonProperty("goodsNumber")
            private String goodsNumber;
            @JsonProperty("goodsValueTotal")
            private String goodsValueTotal;
            @JsonProperty("icsmIcon")
            private String icsmIcon;
            @JsonProperty("limitTypeCode")
            private String limitTypeCode;
            @JsonProperty("printFlag")
            private String printFlag;
            @JsonProperty("printIcon")
            private String printIcon;
            @JsonProperty("proCode")
            private String proCode;
            @JsonProperty("proIcon")
            private String proIcon;
            @JsonProperty("proName")
            private String proName;
            @JsonProperty("sourceCityCode")
            private String sourceCityCode;
            @JsonProperty("sourceDeptCode")
            private String sourceDeptCode;
            @JsonProperty("sourceTeamCode")
            private String sourceTeamCode;
            @JsonProperty("sourceTransferCode")
            private String sourceTransferCode;
            @JsonProperty("twoDimensionCode")
            private String twoDimensionCode;
            @JsonProperty("waybillNo")
            private String waybillNo;
            @JsonProperty("xbFlag")
            private String xbFlag;
        }
    }

    @NoArgsConstructor
    @Data
    public static class WaybillNoInfoListDTO {
        @JsonProperty("waybillNo")
        private String waybillNo;
        @JsonProperty("waybillType")
        private Integer waybillType;
    }
}
