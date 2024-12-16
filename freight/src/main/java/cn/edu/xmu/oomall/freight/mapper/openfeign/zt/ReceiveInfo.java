package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 收件人信息类
 * @Author 李子晴
 * 2023-dgn2-001
 */

 @NoArgsConstructor
 @Data
 @JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiveInfo {

    /*
     * 收件人姓名
     * 对应Express-receivName
     */
    private String receiverName;

    /*
     * 收件人座机，与收件人手机号不同时为空
     * 
     */
    private String receiverPhone;

    /*
     * 收件人手机号，与收件人座机不同时为空
     * 对应Express-receivMovile
     */
    private String receiverMobile;

    /*
     * 收件人省
     * 对应Express-receivRegion里的省信息
     */
    private String receiverProvince;

    /*
     * 收件人市
     * 对应Express-receivRegion里的市信息
     */
    private String receiverCity;

    /*
     * 收件人区
     * 对应Express-receivRegion里的区信息
     */
    private String receiverDistrict;

    /*
     * 收件人详细地址
     * 对应Express-receivAddress
     */
    private String receiverAddress;

}
