//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.DivRefundTransDao;
import cn.edu.xmu.oomall.payment.dao.PayTransDao;
import cn.edu.xmu.oomall.payment.dao.RefundTransDao;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptor;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

import static cn.edu.xmu.javaee.core.model.Constants.SYSTEM;

/**
 * 退款交易
 */
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@CopyFrom({RefundTransPo.class})
@CopyTo({RefundTransPo.class})
public class RefundTrans extends Transaction{

    private static final Logger logger = LoggerFactory.getLogger(RefundTrans.class);

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

    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(NEW, "待退款");
            put(SUCCESS, "已退款");
            put(FAIL, "退款失败");
            put(WRONG, "错账");
        }
    };

    protected Map<Byte, Set<Byte>> getTransition(){
        return toStatus;
    };

    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(NEW, new HashSet<>(){
                {
                    add(FAIL);
                    add(WRONG);
                    add(SUCCESS);
                }
            });
            put(WRONG, new HashSet<>(){
                {
                    add(SUCCESS);
                }
            });
        }
    };

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
            if (null != allowStatusSet) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RefundTransDao refundTransDao;

    @Override
    public void adjust(UserDto user) {
        if (!this.allowStatus(RefundTrans.SUCCESS) || !this.status.equals(WRONG)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "退款交易", this.id, STATUSNAMES.get(this.status)));
        }else {
            RefundTrans trans = new RefundTrans();
            trans.setId(this.id);
            trans.setAdjustId(user.getId());
            trans.setAdjustName(user.getName());
            trans.setStatus(RefundTrans.SUCCESS);
            if (null != this.refundTransDao) {
                this.refundTransDao.save(trans, user);
            } else {
                logger.error("adjust: refundTransDao is null");
            }
        }
    }

    @Override
    public String getTransName() {
        return "退款交易";
    }

    /**
     * 获得当前状态名称
     * @author Ming Qiu
     * <p>
     * date: 2022-11-13 0:43
     * @return
     */
    public String getStatusName(){
        return STATUSNAMES.get(this.status);
    }

    /**
     * 用户退回账号
     */
    @Setter
    @Getter
    private String userReceivedAccount;

    /**
     * 待退回的分账金额
     */
    @Setter
    @Getter
    private Long divAmount;

    /**
     * 关联的支付交易
     */
    @ToString.Exclude
    @JsonIgnore
    private PayTrans payTrans;

    @Setter
    @Getter
    private Long payTransId;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private PayTransDao payTransDao;


    public PayTrans getPayTrans(){
        if (null == this.payTrans && null != this.payTransDao){
            this.payTrans = this.payTransDao.findById(this.shopId, this.payTransId);
        }
        return this.payTrans;
    }

    /**
     * 关联的分账退回
     */
    @ToString.Exclude
    @JsonIgnore
    private DivRefundTrans divTrans;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private DivRefundTransDao divRefundTransDao;

    public DivRefundTrans getDivTrans(){
        if (null == this.divTrans && null != this.divRefundTransDao) {
            this.divTrans = this.divRefundTransDao.findByRefundTransId(this.id);
        }
        return this.divTrans;
    }

    @ToString.Exclude
    @JsonIgnore
    private PayAdaptor payAdaptor;

    public void setPayAdaptor(PayAdaptorFactory factory){
        payAdaptor = factory.createPayAdaptor(this.getChannel());
    }

    /**
     * 退款交易构造函数
     * @param payTrans 支付交易
     * @param amount 退款金额
     * @param divAmount 分账退款金额
     * @throws BusinessException
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    public RefundTrans(PayTrans payTrans, Long amount, Long divAmount) throws BusinessException{
        super();
        Set<Byte> admitStatue = new HashSet<>(){
            {
                add(PayTrans.SUCCESS);
                add(PayTrans.DIV);
            }
        };
        if (!admitStatue.contains(payTrans.getStatus())){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(),"支付对象", payTrans.getId(), payTrans.getStatusName()));
        }

        // 判断退款金额总和小于支付金额
        if (amount + payTrans.getRefundAmount() > payTrans.getAmount()){
            throw new BusinessException(ReturnNo.PAY_REFUND_MORE, String.format(ReturnNo.PAY_REFUND_MORE.getMessage(), payTrans.getId()));
        }

        // 判断分账退款金额总和小于支付分账金额
        if (divAmount + payTrans.getDivRefundAmount() > payTrans.getDivAmount()){
            throw new BusinessException(ReturnNo.PAY_DIVREFUND_MORE, String.format(ReturnNo.PAY_DIVREFUND_MORE.getMessage(), payTrans.getId()));
        }

        this.amount = amount;
        this.accountId = payTrans.getAccountId();
        this.payTransId = payTrans.getId();
        this.outNo = payTrans.getOutNo();
        this.status = RefundTrans.NEW;
        this.divAmount = divAmount;
        this.shopId = payTrans.getShopId();
    }

    /**
     * 对账
     * @return 对账成功与否
     */
    public Boolean check(){
        Boolean ret = true;
        if (this.getStatus().equals(SUCCESS) ||
        this.getStatus().equals(FAIL)){
            Account account = this.getAccount();
            RefundTrans queryTrans;
            if (Objects.isNull(this.getOutNo())){
                logger.error("findRefundById: RefundTrans (id = {}) outNo is null", id);
                throw new BusinessException(ReturnNo.INCONSISTENT_DATA, String.format("退款交易(id = %d )的tranNo和outNo均为空",  id));
            }
            queryTrans = this.payAdaptor.returnRefund(account, this.getOutNo());

            if (queryTrans.getAmount().equals(this.getAmount())){
                queryTrans.setId(id);
                this.refundTransDao.save(queryTrans, SYSTEM);
            }else{
                //错账
                Ledger ledger = new Ledger();
                ledger.setCheckTime(LocalDateTime.now());
                ledger.setAmount(queryTrans.getAmount());
                ledger.setTransNo(queryTrans.getTransNo());
                ledger.setOutNo(queryTrans.getOutNo());
                ledger.setAccountId(account.getId());
                Ledger newLedger = this.ledgerDao.insert(ledger, SYSTEM);
                RefundTrans updateTrans = new RefundTrans();
                updateTrans.setId(id);
                updateTrans.setLedgerId(newLedger.getId());
                updateTrans.setStatus(PayTrans.WRONG);
                this.refundTransDao.save(updateTrans, SYSTEM);
                this.setLedgerId(newLedger.getId());
                this.setStatus(PayTrans.WRONG);
                ret = false;
            }
        }
        return ret;
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

    @Override
    public Byte getStatus() {
        return this.status;
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
