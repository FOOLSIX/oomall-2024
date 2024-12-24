package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import lombok.*;

import java.time.LocalDateTime;

@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@CopyFrom({AddressPo.class})
@Data

public class Address extends OOMallObject {
    @Setter
    private Long customer_id;
    private Integer region_id;
    private Long count;
    private String address;
    private String consignee;
    private String mobile;
    private Boolean be_default;

    private AddressDao addressDao;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {this.gmtCreate = gmtCreate;}
    @Override
    public void setGmtModified(LocalDateTime gmtModified) {this.gmtModified = gmtModified;}

    //创建者id就是顾客id
    public void setCustomer_id(Long id) {this.setCreatorId(id);this.customer_id = id;}
}
