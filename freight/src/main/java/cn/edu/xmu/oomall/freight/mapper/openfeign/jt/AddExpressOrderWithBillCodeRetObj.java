package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

/**
 * 创建运单（带运单号）接口返回数据类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

//  继承AddExpressOrderRetObj,与不带运单号的创建订单接口返回值一致，
public class AddExpressOrderWithBillCodeRetObj extends AddExpressOrderRetObj {
}
