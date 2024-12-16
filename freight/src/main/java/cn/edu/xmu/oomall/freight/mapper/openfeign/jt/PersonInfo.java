package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 寄件人/收件人信息类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonInfo {
    /**
     * 姓名
     * 对应Express对象的 sender/delivery的Name
     */
    private String name;

    /**
     * 手机号码（手机号码和电话二选一）
     * 对应Express对象的sender/delivery 的Mobile
     */
    private String mobile;
    /**
     * 电话（手机号码和电话二选一）
     */
    private String phone;
    /**
     * 国家三字码（如：中国=CHN、印尼 =IDN），固定为CHN
     */
    private String countryCode = "CHN";
    /**
     * 省份
     * prov + city + area对应Express对象的sender/delivery 的 Region
     */
    private String prov;
    /**
     * 城市
     * prov + city + area对应Express对象的sender/delivery 的 Region
     */
    private String city;
    /**
     * 区域
     * prov + city + area对应Express对象的sender/delivery 的 Region
     */
    private String area;
    /**
     * 详细地址
     * prov + city + area对应Express对象的sender/delivery 的 Region
     */
    private String address;
}
