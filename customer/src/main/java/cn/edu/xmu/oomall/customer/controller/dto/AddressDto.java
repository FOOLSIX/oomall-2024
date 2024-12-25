package cn.edu.xmu.oomall.customer.controller.dto;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import jakarta.validation.constraints.NotNull;

public class AddressDto {
    @NotNull(message = "region_id不能为空", groups = {NewGroup.class})
    private Long region_id;
    private String address;
    private String consignee;
    private String mobile;

    public Long getRegion_id() {return this.region_id;}
    public void setRegion_id(Long region_id) {this.region_id = region_id;}
    public String getAddress() {return this.address;}
    public void setAddress(String address) {this.address = address;}
    public String getConsignee() {return this.consignee;}
    public void setConsignee(String consignee) {this.consignee = consignee;}
    public String getMobile() {return this.mobile;}
    public void setMobile(String mobile) {this.mobile = mobile;}

}
