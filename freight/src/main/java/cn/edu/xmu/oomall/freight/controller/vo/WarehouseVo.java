package cn.edu.xmu.oomall.freight.controller.vo;

import lombok.Builder;

@Builder
public class WarehouseVo {
    private String name;
    private String address;
    private String mobile;
    private Long regionId;
}
