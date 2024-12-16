//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.payment.dao.bo.Channel;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({PayTrans.class})
public class PayTransVo {

    private Long id;

    private String outNo;

    private String transNo;

    private Long amount;

    private Long divAmount;

    private LocalDateTime successTime;

    private String prepayId;

    private Byte inRefund;

    private SimpleChannelVo channel;

    private Byte status;

    private LocalDateTime timeBegin;

    private LocalDateTime timeExpire;

    private IdNameTypeVo adjustor;

    private LocalDateTime adjustTime;

    private IdNameTypeVo creator;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private IdNameTypeVo modifier;

    private LedgerVo ledger;

    public PayTransVo(PayTrans payTrans){
        super();
        CloneFactory.copy(this, payTrans);
        this.creator = IdNameTypeVo.builder().id(payTrans.getCreatorId()).name(payTrans.getCreatorName()).build();
        this.modifier = IdNameTypeVo.builder().id(payTrans.getModifierId()).name(payTrans.getModifierName()).build();
        this.adjustor = IdNameTypeVo.builder().id(payTrans.getAdjustId()).name(payTrans.getAdjustName()).build();
        this.ledger = payTrans.getLedger().map(o-> CloneFactory.copy(new LedgerVo(),o)).orElse(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getDivAmount() {
        return divAmount;
    }

    public void setDivAmount(Long divAmount) {
        this.divAmount = divAmount;
    }

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public Byte getInRefund() {
        return inRefund;
    }

    public void setInRefund(Byte inRefund) {
        this.inRefund = inRefund;
    }

    public SimpleChannelVo getChannel() {
        return channel;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public LocalDateTime getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(LocalDateTime timeBegin) {
        this.timeBegin = timeBegin;
    }

    public LocalDateTime getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(LocalDateTime timeExpire) {
        this.timeExpire = timeExpire;
    }

    public IdNameTypeVo getAdjustor() {
        return adjustor;
    }


    public LocalDateTime getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(LocalDateTime adjustTime) {
        this.adjustTime = adjustTime;
    }

    public IdNameTypeVo getCreator() {
        return creator;
    }


    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public IdNameTypeVo getModifier() {
        return modifier;
    }

    public void setChannel(Channel channel) {
        this.channel = CloneFactory.copy(new SimpleChannelVo(), channel);
    }
}