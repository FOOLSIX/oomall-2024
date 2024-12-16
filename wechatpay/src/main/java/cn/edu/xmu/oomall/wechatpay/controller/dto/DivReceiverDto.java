package cn.edu.xmu.oomall.wechatpay.controller.dto;

import cn.edu.xmu.oomall.wechatpay.dao.bo.DivReceiver;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DivReceiverDto {

    private Integer amount;
    private String description;
    private String type;
    private String account;
    private String result;
    private String failReason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime finishTime;
    private String detailId;

    public DivReceiverDto(DivReceiver divReceiver) {
        this.type = divReceiver.getType();
        this.account = divReceiver.getAccount();
        this.amount = divReceiver.getAmount();
        this.description = divReceiver.getDescription();
        this.failReason = divReceiver.getFailReason();
        this.result = divReceiver.getResult();
        this.createTime = divReceiver.getCreateTime();
        this.finishTime = divReceiver.getFinishTime();
        this.detailId = divReceiver.getDetailId();
    }
}
