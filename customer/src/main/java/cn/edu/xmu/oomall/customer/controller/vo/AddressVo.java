package cn.edu.xmu.oomall.customer.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;

import java.time.LocalDateTime;

@CopyFrom({Address.class, AddressPo.class})
public class AddressVo {
    private Long id;
    private Long region_id;
    private String address;
    private String consignee;
    private String mobile;
    private Boolean be_default;

    private IdNameTypeVo creator;
    private IdNameTypeVo region;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameTypeVo modifier;

    public AddressVo(Address address) {
        super();
        CloneFactory.copy(this,address);
        this.setCreator(IdNameTypeVo.builder().id(address.getCreatorId()).name(address.getCreatorName()).build());
        this.setRegion(IdNameTypeVo.builder().id(address.getRegion_id()).name(address.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(address.getModifierId()).name(address.getModifierName()).build());
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public Long getRegion_id(){return region_id.longValue();}
    public void setRegion_id(Long Region_id){this.region_id = region.getId();}

    public String getAddress(){return address;}
    public void setAddress(String address){this.address = address;}

    public String getConsignee(){return consignee;}
    public void setConsignee(String consignee){this.consignee = consignee;}

    public String getMobile(){return mobile;}
    public void setMobile(String mobile){this.mobile = mobile;}

    public Boolean getBe_default(){return be_default;}
    public void changeBe_default(){be_default = !be_default;}

    public LocalDateTime getGmtModified(){return gmtModified;}
    public void setGmtModified(LocalDateTime gmtModified){this.gmtModified = gmtModified;}

    public LocalDateTime getGmtCreate(){return gmtCreate;}
    public void setGmtCreate(LocalDateTime gmtCreate){this.gmtCreate = gmtCreate;}

    public void setCreator(IdNameTypeVo creator) {this.creator = creator;}
    public IdNameTypeVo getCreator(IdNameTypeVo creator) {return this.creator;}

    public void setRegion(IdNameTypeVo region) {
        this.region = region;
    }
    public IdNameTypeVo getRegion(IdNameTypeVo region) {return this.region;}

    public void setModifier(IdNameTypeVo modifier) {
        this.modifier = modifier;
    }
    public IdNameTypeVo getModifier(IdNameTypeVo modifier) {return this.modifier;}
}
