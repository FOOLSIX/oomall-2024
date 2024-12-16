package cn.edu.xmu.oomall.sfexpress.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.sfexpress.controller.dto.ContactInfoListDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
//@Data
@CopyFrom({ContactInfoListDTO.class})
public class ContactInfo {
    /**
     * 详细地址，若有四级行政区划，如镇/街道等信息可拼接至此字段，
     * 格式样例：镇/街道+详细地址。
     * 若province/city 字段的值不传，此字段必须包含省市信息，避免影响原寄地代码识别，
     * 如：广东省深圳市福田区新洲十一街万基商务大厦10楼；
     * 此字段地址必须详细，否则会影响目的地中转识别；
     */
    private String address;

    /**
     * 所在地级行政区名称，必须是标准的城市称谓 如：北京市、 深圳市、大理白族自治州等；
     * 此字段影响原寄地代码识别， 建议尽可能传该字段的值
     */
    private String city;

    /**
     * 联系人姓名
     */
    private String contact;

    /**
     * 地址类型： 1，寄件方信息 2，到件方信息
     */
    private Integer contactType;

    /**
     * 公司名称
     */
    private String country;

    /**
     * 国家或地区代码 例如：内地件CN 香港852，默认CN
     */
    private String county = "CN";

    /**
     * 手机
     */
    private String mobile;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public static List<ContactInfoListDTO> toDto(List<ContactInfo> contactInfos) {
        List<ContactInfoListDTO> contactInfoListDTOList = new ArrayList<>();
        for (ContactInfo contactInfo : contactInfos) {
            ContactInfoListDTO contactInfoListDTO = new ContactInfoListDTO();
            BeanUtils.copyProperties(contactInfo, contactInfoListDTO);
            contactInfoListDTOList.add(contactInfoListDTO);
        }
        return contactInfoListDTOList;
    }
}
