//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.LedgerDao;
import cn.edu.xmu.oomall.payment.dao.AccountDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 交易
 */
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class Transaction extends OOMallObject{

    /**
     * 内部交易号
     */
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
     * 状态
     */
    protected Byte status;

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

    /**
     * 所属商铺id
     */
    protected Long shopId;

    /**
     * 状态迁移
     * @return
     */
    protected abstract Map<Byte, Set<Byte>> getTransition();

    /**
     * 是否允许状态迁移
     * @author Ming Qiu
     * <p>
     * date: 2022-11-13 0:25
     * @param status
     * @return
     */
    public boolean allowStatus(Byte status){
        boolean ret = false;

        if (null != status && null != this.status){
            Set<Byte> allowStatusSet = getTransition().get(this.status);
            if (!Objects.isNull(allowStatusSet)) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    protected Long accountId;

    /**
     * 交易所属的商铺渠道
     */
    @ToString.Exclude
    @JsonIgnore
    protected Account account;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    protected AccountDao accountDao;

    public Account getAccount() throws BusinessException {
        if (Objects.isNull(this.account) && !Objects.isNull(this.accountDao)) {
            this.account = this.accountDao.findById(PLATFORM, this.accountId);
        }
        return this.account;
    }


    /**
     * 获得支付渠道
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    public Channel getChannel() {
        return this.getAccount().getChannel();
    }


    /**
     * 错账id
     */
    protected Long ledgerId;

    /**
     * 错账
     */
    @ToString.Exclude
    @JsonIgnore
    protected Ledger ledger;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    protected LedgerDao ledgerDao;

    public Optional<Ledger> getLedger(){
        if (null == this.ledger && null != this.ledgerDao){
            if (Objects.isNull(this.ledgerId)){
                this.ledger = null;
            }else{
                this.ledger = this.ledgerDao.findById(this.ledgerId);
            }
        }
        return Optional.ofNullable(this.ledger);
    }

    /**
     * 调账
     * @param user 调账者
     */
    public abstract void adjust(UserDto user);

    public abstract String getTransName();

    public abstract Long getLedgerId();

    public abstract void setLedgerId(Long ledgerId);

    public abstract Long getShopId();

    public abstract void setShopId(Long shopId);

    public abstract String getOutNo();

    public abstract void setOutNo(String outNo);

    public abstract String getTransNo();

    public abstract void setTransNo(String transNo);

    public abstract Long getAmount();

    public abstract void setAmount(Long amount);

    public abstract LocalDateTime getSuccessTime();

    public abstract void setSuccessTime(LocalDateTime successTime);

    public abstract Byte getStatus();

    public abstract void setStatus(Byte status);

    public abstract Long getAdjustId();

    public abstract void setAdjustId(Long adjustId);

    public abstract String getAdjustName();

    public abstract void setAdjustName(String adjustName);

    public abstract LocalDateTime getAdjustTime();

    public abstract void setAdjustTime(LocalDateTime adjustTime);

    public abstract Long getAccountId();

    public abstract void setAccountId(Long accountId);

    public abstract void setId(Long id);

    public abstract Long getId();

    public abstract void  setCreatorId(Long creatorId);

    public abstract Long  getCreatorId();

    public abstract void setCreatorName(String creatorName);

    public abstract String getCreatorName();

    public abstract void setModifierId(Long modifierId);

    public abstract Long getModifierId();
    public abstract void setModifierName(String modifierName);

    public abstract String getModifierName();

    public abstract void setGmtCreate(LocalDateTime gmtCreate);

    public abstract LocalDateTime getGmtCreate();

    public abstract void setGmtModified(LocalDateTime gmtModified);

    public abstract LocalDateTime getGmtModified();
}
