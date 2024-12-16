package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 取消运单接口返回数据类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelExpressOrderRetObj {
    /**
     * 运单号
     * 对应Express的billCode
     */
    String billCode;

    /**
     * 客户订单号
     * 对应Express的orderCode
     */
    String txlogisticId;
}
