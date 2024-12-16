package cn.edu.xmu.oomall.ztoexpress.mapper.po;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.actuate.audit.listener.AuditListener;

@Data
@Entity
@Table(name = "zto_site")
@EntityListeners(AuditListener.class)
public class SitePo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;


    @Column(name = "prov")
    private String prov;
    @Column(name = "city")
    private String city;
    @Column(name = "is_Center")
    private Integer isCenter;

    @Column(name = "phone")
    private String phone;

}