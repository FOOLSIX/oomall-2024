package cn.edu.xmu.oomall.customer.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({Address.class})
@Data

public class SimpleAddressVo {
    private Long id;
    private Long region_id;
    private String address;
    private String consignee;
    private String mobile;
    private Boolean be_default;

    public SimpleAddressVo(Address address) {
        this.region_id = address.getRegion_id();
        this.address = address.getAddress();
        this.consignee = address.getConsignee();
        this.mobile = address.getMobile();
        this.be_default = address.getBe_default();
    }
}
