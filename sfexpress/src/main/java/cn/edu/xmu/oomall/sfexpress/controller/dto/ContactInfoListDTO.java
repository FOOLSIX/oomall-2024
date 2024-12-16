package cn.edu.xmu.oomall.sfexpress.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.sfexpress.dao.bo.ContactInfo;
import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表DTO
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class ContactInfoListDTO {

    @JsonProperty("address")
    private String address;
    @JsonProperty("city")
    private String city;
    @JsonProperty("contact")
    private String contact;
    @JsonProperty("contactType")
    private Integer contactType;
    @JsonProperty("country")
    private String country;
    @JsonProperty("county")
    private String county;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("postCode")
    private String postCode;
    @JsonProperty("province")
    private String province;
    @JsonProperty("tel")
    private String tel;
    @JsonProperty("company")
    private String company;

    public static List<ContactInfo> toBo(List<ContactInfoListDTO> contactInfoListDTOList) {
        List<ContactInfo> contactInfos = new ArrayList<>();
        for (ContactInfoListDTO contactInfoListDTO : contactInfoListDTOList) {
            ContactInfo contactInfo = new ContactInfo();
            BeanUtils.copyProperties(contactInfoListDTO, contactInfo);
            contactInfos.add(contactInfo);
        }
        return contactInfos;
    }
}
