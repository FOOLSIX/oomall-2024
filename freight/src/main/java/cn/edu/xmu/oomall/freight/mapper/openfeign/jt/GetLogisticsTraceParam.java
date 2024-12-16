package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流轨迹查询信息类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetLogisticsTraceParam {
    /**
     * 运单号
     * 对应Express的billCode
     * 多个运单号以英文逗号隔开
     * 一次性最多查询30票运单
     */
    String billCodes;
}
