package cn.edu.xmu.oomall.wechatpay.controller.dto;

import cn.edu.xmu.oomall.wechatpay.dao.bo.DivRefundTrans;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DivRefundTransDto {

    private String subMchid;
    private String orderId;
    private String outOrderNo;
    private String outReturnNo;
    private String returnId;
    private String returnMchid;
    private Integer amount;
    private String description;
    private String result;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;

    public DivRefundTransDto(DivRefundTrans divRefundTrans){
        this.subMchid = divRefundTrans.getSubMchid();
        this.orderId = divRefundTrans.getOrderId();
        this.outOrderNo = divRefundTrans.getOutOrderNo();
        this.outReturnNo = divRefundTrans.getOutReturnNo();
        this.returnId = divRefundTrans.getReturnId();
        this.returnMchid = divRefundTrans.getReturnMchid();
        this.amount = divRefundTrans.getAmount();
        this.description = divRefundTrans.getDescription();
        this.result = divRefundTrans.getResult();
        this.createTime = divRefundTrans.getCreateTime();
        this.finishTime = divRefundTrans.getFinishTime();
    }
}
