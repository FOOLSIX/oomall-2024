package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * 物流轨迹查询接口返回数据类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLogisticsTraceRetObj {
    /**
     * 运单号
     * 对应express中的billCode
     */
    String billCode;
    /**
     * 运单轨迹详细
     * 可以根据此信息修改系统中运单Express对象的状态（status）
     */
    ArrayList<TraceDetail> details;
}
