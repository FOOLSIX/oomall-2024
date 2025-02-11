package cn.edu.xmu.oomall.jtexpress.mapper.po;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "jtexpress_api_account")
@Data
public class ApiAccountPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account")
    private Long account;

    @Column(name = "private_key", length = 50)
    private String privateKey;

}
