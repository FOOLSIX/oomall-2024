package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 查询物流轨迹接口信息类
 * @Author 李子晴
 * 2023-dgn2-001
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryOrderTrackParam {
/*
 * 运单号（必传）
 * 对应Express-billCode
 */
private String billCode;

/*
 * 收寄人任一方电话号码后4位（手机或座机）,通过电话号码鉴权时必填，鉴权方式可以电子面单账号或电话号码二选一。
 * 选择电子面单账号鉴权时，该字段非必填；选择电话号码鉴权时，可以不绑定下单电子面单账号。
 * 对应Express-sendMobile或Express-receivMobile其后四位。
 */
 private String mobilePhone;
    
}
