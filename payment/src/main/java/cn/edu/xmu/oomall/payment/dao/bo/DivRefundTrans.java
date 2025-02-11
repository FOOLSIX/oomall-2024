//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.DivPayTransDao;
import cn.edu.xmu.oomall.payment.dao.RefundTransDao;
import cn.edu.xmu.oomall.payment.mapper.generator.po.DivRefundTransPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 支付分账交易
 */
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@CopyFrom({DivRefundTransPo.class})
@CopyTo(DivRefundTransPo.class)
public final class DivRefundTrans extends Transaction{

    /**
     * 待退款
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte NEW = 0;
    /**
     * 已退款
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte SUCCESS = 1;
    /**
     * 错账
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte WRONG = 3;
    /**
     * 退款失败
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte FAIL = 4;
    /**
     * 退款取消
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte CANCEL = 5;

    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(NEW, "待退回");
            put(SUCCESS, "已退回");
            put(WRONG, "错账");
            put(FAIL, "退回失败");
            put(CANCEL, "取消");
        }
    };

    protected static Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(NEW, new HashSet<>(){
                {
                    add(SUCCESS);
                    add(CANCEL);
                    add(FAIL);
                    add(WRONG);
                }
            });
            put(WRONG, new HashSet<>(){
                {
                    add(SUCCESS);
                }
            });
        }
    };

    protected Map<Byte, Set<Byte>> getTransition(){
        return toStatus;
    }

    @Override
    public void adjust(UserDto user) {

    }


    @Override
    public String getTransName() {
        return "退款分账交易";
    }

    @Setter
    @Getter
    private Long refundTransId;

    @ToString.Exclude
    @JsonIgnore
    private RefundTrans refundTrans;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private RefundTransDao refundTransDao;

    public RefundTrans getRefundTrans() throws BusinessException{
        if (Objects.isNull(this.refundTrans) && !Objects.isNull(this.refundTransDao)){
            this.refundTrans = this.refundTransDao.findById(this.shopId, this.refundTransId);
        }
        return this.refundTrans;
    }

    @Setter
    @Getter
    private Long divPayTransId;

    @ToString.Exclude
    @JsonIgnore
    private DivPayTrans divPayTrans;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private DivPayTransDao divPayTransDao;

    public DivPayTrans getDivPayTrans() throws BusinessException{
        if (Objects.isNull(this.divPayTrans) && !Objects.isNull(this.divPayTransDao)){
            this.divPayTrans = this.divPayTransDao.findById(this.shopId, this.divPayTransId);
        }
        return this.divPayTrans;
    }

    public DivRefundTrans(RefundTrans refundTrans, DivPayTrans divPayTrans) {
        super();
        this.refundTransId = refundTrans.getId();
        this.amount = refundTrans.getDivAmount();
        this.accountId = divPayTrans.getAccountId();
        this.shopId = divPayTrans.getShopId();
        this.status  = DivRefundTrans.NEW;
        this.divPayTransId = divPayTrans.getId();
    }

    @Override
    public Long getLedgerId() {
        return this.ledgerId;
    }

    @Override
    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
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
}
