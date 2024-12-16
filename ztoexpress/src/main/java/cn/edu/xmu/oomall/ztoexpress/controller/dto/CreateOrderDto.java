package cn.edu.xmu.oomall.ztoexpress.controller.dto;

import cn.edu.xmu.oomall.ztoexpress.unit.PersonInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    private String partnerType;
    private String orderType;
    private String partnerOrderCode;
    private PersonInfo senderInfo;
    private PersonInfo receiverInfo;
}
