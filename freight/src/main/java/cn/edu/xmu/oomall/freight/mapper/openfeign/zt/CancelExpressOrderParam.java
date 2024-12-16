package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 取消订单接口信息类
 * @Author 李子晴
 * 2023-dgn2-001
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelExpressOrderParam {
    /*
     * 取消类型  1不想寄了,2下错单,3重复下单,4运费太贵,5无人联系,6取件太慢,7态度差
     */
    private String cancelType;

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