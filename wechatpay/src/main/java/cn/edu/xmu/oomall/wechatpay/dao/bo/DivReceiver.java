package cn.edu.xmu.oomall.wechatpay.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.wechatpay.controller.vo.DivReceiverVo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivReceiverPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author maguoqi
 * @date 2023/12/4
 * @check result, failReason, createTime, finishTime, detailId
 */

@NoArgsConstructor
@CopyFrom({DivReceiverPo.class})
public class DivReceiver {

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
    private String detailId;    // start with "D"

    public DivReceiver(DivReceiverVo divReceiverVo) {
        this.type = divReceiverVo.getType();
        this.account = divReceiverVo.getAccount();
        this.description = divReceiverVo.getDescription();
        this.amount = divReceiverVo.getAmount();
    }

    public DivReceiver(DivReceiver otherDivReceiver) {
        this.type = otherDivReceiver.getType();
        this.account = otherDivReceiver.getAccount();
        this.description = otherDivReceiver.getDescription();
        this.amount = otherDivReceiver.getAmount();
        this.result = otherDivReceiver.getResult();
        this.failReason = otherDivReceiver.getFailReason();
        this.createTime = otherDivReceiver.getCreateTime();
        this.finishTime = otherDivReceiver.getFinishTime();
        this.detailId = otherDivReceiver.getDetailId();
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
