package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchRoutes返回参数
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@NoArgsConstructor
@Data
public class SearchRoutesRet {


    @JsonProperty("routeResps")
    private List<RouteRespsDTO> routeResps;

    @NoArgsConstructor
    @Data
    public static class RouteRespsDTO {
        /**
         * 运单号
         */
        @JsonProperty("mailNo")
        private String mailNo;
        /**
         * 路由列表
         */
        @JsonProperty("routes")
        private List<RoutesDTO> routes;

        @NoArgsConstructor
        @Data
        public static class RoutesDTO {
            /**
             * 发生时间
             */
            @JsonProperty("acceptTime")
            private String acceptTime;
            /**
             * 发生地点
             */
            @JsonProperty("acceptAddress")
            private String acceptAddress;
            /**
             * 操作码
             */
            @JsonProperty("opCode")
            private String opCode;

            /**
             * 物流状态，我们系统主要靠物流列表中最新的物流状态来更新物流状态
             */
            @JsonProperty("remark")
            private String remark;
        }
    }
}
