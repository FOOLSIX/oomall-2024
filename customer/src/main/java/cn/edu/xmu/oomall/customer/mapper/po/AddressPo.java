package cn.edu.xmu.oomall.customer.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.controller.dto.AddressDto;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_address")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CopyFrom({Address.class, AddressDto.class})

public class AddressPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customer_id;
    private Long region_id;
    private Long count;
    private String address;
    private String consignee;
    private String mobile;
    private Boolean be_default;


    @PrePersist
    public void prePersist() {
        if (be_default == null) {
            be_default = Boolean.FALSE;
        }
    }

}
