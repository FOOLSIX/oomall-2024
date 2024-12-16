package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

/**
 * 创建运单(带运单号)信息类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddExpressOrderWithBillCodeParam extends AddExpressOrderParam {
    /**
     * 运单号
     * 对应Express对象的billCode
     */
    String billCode;

    /**
     * 物流公司上门取货开始时间 yyyy-MM-dd HH:mm:ss
     */
    String sendStartTime;

    /**
     * 物流公司上门取货结束时间 yyyy-MM-dd HH:mm:ss
     */
    String sendEndTime;
}
