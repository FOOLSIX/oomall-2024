package cn.edu.xmu.oomall.wechatpay.controller.dto;

import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayTransDto {

    private String spMchid;

    private String subMchid;

    private String outTradeNo;

    private String tradeState;

    private String tradeStateDesc;

    public PayTransDto(PayTrans payTrans){
        this.spMchid = payTrans.getSpMchid();
        this.subMchid = payTrans.getSubMchid();
        this.outTradeNo = payTrans.getOutTradeNo();
        this.tradeState = payTrans.getTradeState();
        this.tradeStateDesc = payTrans.getTradeStateDesc();
    }
}
