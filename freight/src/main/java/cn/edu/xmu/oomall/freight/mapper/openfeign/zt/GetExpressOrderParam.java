package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 查询订单接口信息类
 * @Author 李子晴
 * 2023-dgn2-001
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetExpressOrderParam {
    /*
     * 0，预约件   1，全网件
     */
    private int type;

    /*
     * 预约件订单号（与运单号不同时为空）
     * 对应Express-orderCode
     */
    private String orderCode;

    /*
     * 运单号（与预约运单号不同时为空）
     * 对应Express-billCode
     */
    private String billCode;
    
}
