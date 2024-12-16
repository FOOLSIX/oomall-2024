package cn.edu.xmu.oomall.jtexpress.controller.vo;

import cn.edu.xmu.oomall.jtexpress.dao.bo.PersonInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonInfoVo {

    @NotNull
    @Size(max = 32)
    private String name;

    @Size(max = 100)
    private String company;

    @Size(max = 32)
    private String postCode;

    @Size(max = 150)
    private String mailBox;

    @Size(max = 30)
    private String mobile;

    @Size(max = 30)
    private String phone;

    @Size(max = 20)
    private String countryCode="CHN";

    @NotNull(message="地址信息不全")
    @Size(max = 32)
    private String prov;

    @NotNull(message="地址信息不全")
    @Size(max = 32)
    private String city;

    @NotNull(message="地址信息不全")
    @Size(max = 32)
    private String area;

    @Size(max = 32)
    private String town;

    @Size(max = 32)
    private String street;

    @NotNull(message="地址信息不全")
    @Size(max = 150)
    private String address;

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
}
