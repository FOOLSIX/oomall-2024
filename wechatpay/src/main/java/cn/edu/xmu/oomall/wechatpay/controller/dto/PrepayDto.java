package cn.edu.xmu.oomall.wechatpay.controller.dto;

import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrepayDto {

    private String prepayId;

    public PrepayDto(PayTrans payTrans){
        this.prepayId = payTrans.getPrepayId();
    }

}
