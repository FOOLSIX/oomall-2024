package cn.edu.xmu.oomall.wechatpay.controller.dto;

import cn.edu.xmu.oomall.wechatpay.dao.bo.DivPayTrans;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DivPayTransDto {

    private String transactionId;

    private String outOrderNo;

    private String orderId;

    private String state;

    private Collection<DivReceiverDto> receivers = new ArrayList<>();

    public DivPayTransDto(DivPayTrans divPayTrans){
        this.transactionId = divPayTrans.getTransactionId();
        this.outOrderNo = divPayTrans.getOutOrderNo();
        this.orderId = divPayTrans.getOrderId();
        this.state = divPayTrans.getState();
        this.receivers = divPayTrans.getReceivers().stream().map(DivReceiverDto::new).collect(Collectors.toCollection(ArrayList::new));
    }
}
