package cn.edu.xmu.oomall.customer.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer_customer")
@AllArgsConstructor
@NoArgsConstructor
@CopyFrom({Customer.class})
public class CustomerPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String name;
    private Byte invalid;
    private Long creatorId;
    private String creatorName;

    private Long modifierId;
    private String modifierName;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

}
