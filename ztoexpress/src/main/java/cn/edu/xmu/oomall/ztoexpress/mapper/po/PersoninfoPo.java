package cn.edu.xmu.oomall.ztoexpress.mapper.po;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.actuate.audit.listener.AuditListener;

@Data
@Entity
@Table(name = "zto_personinfo")
@EntityListeners(AuditListener.class)
public class PersoninfoPo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;
    @Column(name = "district")
    private String district;
    @Column(name = "address")
    private String address;


}