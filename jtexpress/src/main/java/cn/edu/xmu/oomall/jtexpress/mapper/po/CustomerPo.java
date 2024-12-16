package cn.edu.xmu.oomall.jtexpress.mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "jtexpress_customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

}

