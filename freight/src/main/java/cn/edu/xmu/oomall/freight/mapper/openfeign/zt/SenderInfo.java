package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 发件人信息类
 * @Author 李子晴
 * 2023-dgn2-001
 */

 @NoArgsConstructor
 @Data
 @JsonInclude(JsonInclude.Include.NON_NULL)
public class SenderInfo {

    private String senderId;

    /*
     * 发件人姓名
     * 对应Express-sendName
     */
    private String senderName;

    /*
     * 发件人座机，与发件人手机号不同时为空
     * 
     */
    private String senderPhone;

    /*
     * 发件人手机号，与发件人座机不同时为空
     * 对应Express-sendMobile
     */
    private String senderMobile;

    /*
     * 发件人省
     * 对应Express-sendRegion里的省信息
     */
    private String senderProvince;

    /*
     * 发件人市
     * 对应Express-sendRegion里的市信息
     */
    private String senderCity;

    /*
     * 发件人区
     * 对应Express-sendRegion里的区信息
     */
    private String senderDistrict;

    /*
     * 发件人详细地址
     * 对应Express-sendAddress
     */
    private String senderAddress;

}
