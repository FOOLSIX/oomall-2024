package cn.edu.xmu.oomall.order.controller.dto;

import lombok.Data;

@Data
public class OrderUpdateDto {
    private String consignee; // 收货人
    private Long regionId;    // 地区ID
    private String address;   // 详细地址
    private String mobile;    // 电话
}
