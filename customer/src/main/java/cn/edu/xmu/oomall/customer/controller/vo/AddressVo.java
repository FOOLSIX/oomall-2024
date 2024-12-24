package cn.edu.xmu.oomall.customer.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import lombok.Setter;

import java.time.LocalDateTime;

@CopyFrom({Address.class, AddressPo.class})
public class AddressVo {
    private Long id;
    private Integer region_id;
    private String address;
    private String consignee;
    private String mobile;
    private Boolean be_default;

    public AddressVo(Address address) {
        super();
        CloneFactory.copy(this,address);

    }
}
