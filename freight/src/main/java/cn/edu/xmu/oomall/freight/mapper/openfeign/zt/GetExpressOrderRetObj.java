package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 查询订单接口返回结果类
 * @Author 李子晴
 * 2023-dgn2-001
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class GetExpressOrderRetObj {

private String orderType;//订单类型

private String sendCity;//发件市，对应Express-sendRegion中的市信息

private String receivName;//收件人姓名，对应Express-receivName

private int orderStatus;//订单状态，中通返回值中，0：下单成功； 1：分配网点； 2：分配业务员； 3：业务员上门取件； -2订单取消； 99：订单完成

private String sendPro;//发件省，对应Express-sendRegion中的省信息

private String receivPhone;//收件人电话号（座机）

private String receivAddress;//收件人详细地址，对应Express-receivAddress

private String sendMobile;//发件人手机号码，对应Express-sendMobile

private String receivMobile;//收件人手机号码，对应Express-receivMobile

private String sendName;//发件人姓名，对应Express-sendName

private String receivPro;//收件人省，对应Express-receivRegion中的省信息

private String receivCity;//收件人市，对应Express-receivRegion中的市信息

private String sendCounty;//发件人区，对应Express-sendRegion中的区信息

private String billCode;//运单号，对应Express-billCode

private String parcelTakingEndTime;//预约取件结束时间

private String sendAddress;//发件人详细地址，对应Express-sendAddress

private String receivCounty;//收件人区，对应Express-receivRegion中的区信息

private String parcelTakingStartTime;//预约取件开始时间

private String sendPhone;//发件人电话号

private String orderCode;//订单号，对应Express-orderCode

private String cancelReason;//取消原因

private String secStatus;//取消类型
    
}
