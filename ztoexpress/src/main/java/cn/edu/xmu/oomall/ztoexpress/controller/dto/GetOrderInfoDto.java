package cn.edu.xmu.oomall.ztoexpress.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderInfoDto {
    private String type;
    private String orderCode;
    private String billCode;
}
