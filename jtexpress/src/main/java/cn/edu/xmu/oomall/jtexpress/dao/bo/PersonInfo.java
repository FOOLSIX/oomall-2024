package cn.edu.xmu.oomall.jtexpress.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.jtexpress.controller.vo.PersonInfoVo;
import cn.edu.xmu.oomall.jtexpress.mapper.po.PersonInfoPo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@CopyFrom({PersonInfoVo.class, PersonInfoPo.class})
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getMailBox() {
        return mailBox;
    }

    public void setMailBox(String mailBox) {
        this.mailBox = mailBox;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @CopyFrom.Exclude({PersonInfoVo.class, PersonInfoPo.class})
    public Boolean getAlter() {
        return alter;
    }

    @CopyFrom.Exclude({PersonInfoVo.class, PersonInfoPo.class})
    public void setAlter(Boolean alter) {
        this.alter = alter;
    }
}
