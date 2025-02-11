//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 交易
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Transaction extends OOMallObject{

    /**
     * 内部交易号
     */
    @Setter
    @Getter
    protected String outNo;

    /**
     * 渠道交易号
     */
    protected String transNo;

    /**
     * 金额
     */
    protected Long amount;

    /**
     * 交易时间
     */
    protected LocalDateTime successTime;

    /**
     * 调账者id
     */
    protected Long adjustId;

    /**
     * 调账者
     */
    protected String adjustName;

    /**
     * 调账时间
     */
    protected LocalDateTime adjustTime;


    protected Long shopChannelId;

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

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }

    public Long getAdjustId() {
        return adjustId;
    }

    public void setAdjustId(Long adjustId) {
        this.adjustId = adjustId;
    }

    public String getAdjustName() {
        return adjustName;
    }

    public void setAdjustName(String adjustName) {
        this.adjustName = adjustName;
    }

    public LocalDateTime getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(LocalDateTime adjustTime) {
        this.adjustTime = adjustTime;
    }

    public Long getShopChannelId() {
        return shopChannelId;
    }

    public void setShopChannelId(Long shopChannelId) {
        this.shopChannelId = shopChannelId;
    }


    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {

    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {

    }
}
