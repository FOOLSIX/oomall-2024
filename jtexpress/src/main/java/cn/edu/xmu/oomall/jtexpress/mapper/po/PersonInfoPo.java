package cn.edu.xmu.oomall.jtexpress.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.jtexpress.dao.bo.PersonInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@Data
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


}

