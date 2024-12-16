package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *创建订单接口返回结果类
 * @Author 李子晴
 * 2023-dgn2-001
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateExpressOrderRetObj {

/*
 * 订单号
 * 对应Express-orderCode
 */
private String orderCode;

/*
 * 运单号
 * 对应Express-billCode
 */
private String billCode;

    
}
