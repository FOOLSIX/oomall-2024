package cn.edu.xmu.oomall.order.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderUpdateDto {
    private String consignee; // 收货人
    private Long regionId;    // 地区ID
    private String address;   // 详细地址
    private String mobile;    // 电话

    @Builder
    public OrderUpdateDto(String consignee,Long regionId,String address,String mobile){
        this.consignee = consignee;
        this.regionId = regionId;
        this.address = address;
        this.mobile = mobile;
    }
}
