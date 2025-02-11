package cn.edu.xmu.oomall.jtexpress.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.jtexpress.controller.dto.PersonInfoDto;
import cn.edu.xmu.oomall.jtexpress.mapper.po.PersonInfoPo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@CopyFrom({PersonInfoDto.class, PersonInfoPo.class})
public class PersonInfo {
    private Long id;
    private String name;
    private String company;
    private String postCode;
    private String mailBox;
    private String mobile;
    private String phone;
    private String countryCode;
    private String prov;
    private String city;
    private String area;
    private String town;
    private String street;
    private String address;
    private Boolean alter=true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonInfo that = (PersonInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(company, that.company) &&
                Objects.equals(postCode, that.postCode) &&
                Objects.equals(mailBox, that.mailBox) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(prov, that.prov) &&
                Objects.equals(city, that.city) &&
                Objects.equals(area, that.area) &&
                Objects.equals(town, that.town) &&
                Objects.equals(street, that.street) &&
                Objects.equals(address, that.address);
    }


    @CopyFrom.Exclude({PersonInfoDto.class, PersonInfoPo.class})
    public Boolean getAlter() {
        return alter;
    }

    @CopyFrom.Exclude({PersonInfoDto.class, PersonInfoPo.class})
    public void setAlter(Boolean alter) {
        this.alter = alter;
    }
}
