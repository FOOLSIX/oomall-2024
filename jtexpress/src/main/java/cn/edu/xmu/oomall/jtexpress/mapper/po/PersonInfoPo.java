package cn.edu.xmu.oomall.jtexpress.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.jtexpress.dao.bo.PersonInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "jtexpress_person_info")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@CopyFrom({PersonInfo.class})
public class PersonInfoPo {
    /*
     * 主键 ID，自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * 姓名
     */
    @Column(name = "name", length = 32, nullable = false)
    private String name;

    /*
     * 公司
     */
    @Column(name = "company", length = 100)
    private String company;

    /*
     * 邮编
     */
    @Column(name = "post_code", length = 32)
    private String postCode;

    /*
     * 邮箱
     */
    @Column(name = "mail_box", length = 150)
    private String mailBox;

    /*
     * 手机号码
     */
    @Column(name = "mobile", length = 30)
    private String mobile;

    /*
     * 电话号码
     */
    @Column(name = "phone", length = 30)
    private String phone;

    /*
     * 国家三字码
     */
    @Column(name = "country_code", length = 20, nullable = false)
    private String countryCode;

    /*
     * 省份
     */
    @Column(name = "prov", length = 32, nullable = false)
    private String prov;

    /*
     * 城市
     */
    @Column(name = "city", length = 32, nullable = false)
    private String city;

    /*
     * 区域
     */
    @Column(name = "area", length = 32, nullable = false)
    private String area;

    /*
     * 乡镇
     */
    @Column(name = "town", length = 32)
    private String town;

    /*
     * 街道
     */
    @Column(name = "street", length = 32)
    private String street;

    /*
     * 详细地址
     */
    @Column(name = "address", length = 150, nullable = false)
    private String address;


    // Getter 方法
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getMailBox() {
        return mailBox;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getProv() {
        return prov;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getTown() {
        return town;
    }

    public String getStreet() {
        return street;
    }

    public String getAddress() {
        return address;
    }

    // Setter 方法
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setMailBox(String mailBox) {
        this.mailBox = mailBox;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    }

