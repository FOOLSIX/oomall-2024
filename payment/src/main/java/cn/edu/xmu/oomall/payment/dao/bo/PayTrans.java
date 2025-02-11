//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.controller.dto.PayTransDto;
import cn.edu.xmu.oomall.payment.dao.DivPayTransDao;
import cn.edu.xmu.oomall.payment.dao.PayTransDao;
import cn.edu.xmu.oomall.payment.dao.RefundTransDao;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptor;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.dao.channel.vo.PostDivPayAdaptorVo;
import cn.edu.xmu.oomall.payment.dao.channel.vo.PostRefundAdaptorVo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.PayTransPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.model.Constants.SYSTEM;

/**
 * 支付交易
 */
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@CopyFrom({PayTransPo.class})
@CopyTo({PayTransPo.class})
public class PayTrans extends Transaction{

    @ToString.Exclude
    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(PayTrans.class);
    /**
     * 未支付
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte NEW = 0;
    /**
     * 已支付
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
     * 支付失败
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte FAIL = 4;
    /**
     * 取消
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte CANCEL = 5;
    /**
     * 分账
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte DIV = 7;

    /**
     * 状态和名称的对应
     */
    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(NEW, "待支付");
            put(SUCCESS, "已支付");
            put(WRONG, "错账");
            put(FAIL, "支付失败");
            put(CANCEL, "取消");
            put(DIV, "分账");
        }
    };

    protected Map<Byte, Set<Byte>> getTransition(){
        return toStatus;
    };

    /**
     * 允许的状态迁移
     */
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(NEW, new HashSet<>(){
                {
                    add(FAIL);
                    add(CANCEL);
                    add(SUCCESS);
                    add(WRONG);
                }
            });
            put(SUCCESS, new HashSet<>(){
                {
                    add(DIV);
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

        if (Objects.nonNull(status) && Objects.nonNull(this.status)){
            Set<Byte> allowStatusSet = toStatus.get(this.status);
            if (Objects.nonNull(allowStatusSet)) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    @ToString.Exclude
    @JsonIgnore
    private PayAdaptor payAdaptor;

    /**
     * modified by ych
     * task 2023-dgn1-004
     */
    public void setPayAdaptor(PayAdaptorFactory factory){
        this.payAdaptor = factory.createPayAdaptor(this.getChannel());
    }


    @ToString.Exclude
    @JsonIgnore
    @Setter
    private PayTransDao payTransDao;

    @Override
    public void adjust(UserDto user) {
        if (!this.allowStatus(PayTrans.SUCCESS) || !this.status.equals(PayTrans.WRONG)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "支付交易", this.id, STATUSNAMES.get(this.status)));
        }else {
            PayTrans trans = new PayTrans();
            trans.setId(this.id);
            trans.setAdjustId(user.getId());
            trans.setAdjustName(user.getName());
            trans.setStatus(PayTrans.SUCCESS);
            this.payTransDao.save(trans, user);
        }
    }

    @Override
    public String getTransName() {
        return "支付交易";
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
     * 退款
     * @param amount 退款金额
     * @param divAmount 退款分账金额
     * @param user 操作者
     * @return
     */
    public RefundTrans refund(Long amount, Long divAmount, UserDto user){

        //先改变支付交易在退款中，不能允许支付交易同时处理两笔退款, 写操作会锁住其他操作
        PayTrans temp = new PayTrans();
        temp.setId(this.id);
        temp.setInRefund(PayTrans.REFUNDING);
        this.payTransDao.save(temp, user);

        //创建退款交易
        RefundTrans trans = new RefundTrans(this, amount, divAmount);
        RefundTrans newTrans = this.refundTransDao.insert(trans, user);
        //获得完整的bo对象
        RefundTrans refundTrans = this.refundTransDao.findById(trans.getShopId(), newTrans.getId());

        try {
            //查询是否分账
            if (this.status.equals(PayTrans.DIV)) {
                DivPayTrans divPayTrans = this.getDivTrans();
                divPayTrans.refund(refundTrans, user);
            }
            PostRefundAdaptorVo vo = this.payAdaptor.createRefund(refundTrans);
            refundTrans.setTransNo(vo.getTransNo());
            refundTrans.setAmount(vo.getAmount());
            refundTrans.setId(newTrans.getId());
            refundTrans.setSuccessTime(LocalDateTime.now());
            this.refundTransDao.save(refundTrans, user);
        }catch (Exception e){
            refundTrans.setStatus(RefundTrans.FAIL);
            this.refundTransDao.save(refundTrans, user);
            throw e;
        }
        return refundTrans;
    }

    /**
     * modified ych
     * task 2023-dgn1-004
     * 分账
     */
    public void divide(){
        if (!this.status.equals(SUCCESS) || !this.allowStatus(DIV)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "支付交易", this.id, STATUSNAMES.get(this.status)));
        }
        DivPayTrans divPayTrans = new DivPayTrans(this);
        DivPayTrans newDivPayTrans = this.divPayTransDao.insert(divPayTrans, SYSTEM);
        String outNo = newDivPayTrans.getId().toString();
        DivPayTrans savedDivPayTran = new DivPayTrans();
        try {
            PostDivPayAdaptorVo dto = this.payAdaptor.createDivPay(this, outNo);
            savedDivPayTran.setId(this.getId());
            savedDivPayTran.setOutNo(outNo);
            savedDivPayTran.setTransNo(dto.getOrderId());
            savedDivPayTran.setStatus(DivPayTrans.SUCCESS);
            this.divPayTransDao.save(savedDivPayTran, SYSTEM);
        }catch (Exception e){
            savedDivPayTran.setStatus(DivPayTrans.FAIL);
            this.divPayTransDao.save(savedDivPayTran, SYSTEM);
            throw e;
        }
    }

    /**
     * modified ych
     * task 2023-dgn1-004
     * 取消支付
     * @param user
     */
    public void cancel(UserDto user){
        if (!this.allowStatus(CANCEL)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "支付交易",id, this.getStatusName()));
        }
        this.payAdaptor.cancelOrder(this);
        PayTrans newTrans = new PayTrans();
        newTrans.setStatus(CANCEL);
        newTrans.setId(this.getId());
        this.payTransDao.save(newTrans,user);
    }

    /**
     * 对账
     * @return 对账结果
     */
    public Boolean check() {
        Boolean ret = true;
        if (this.getStatus().equals(SUCCESS) ||
                this.getStatus().equals(FAIL) ||
                this.getStatus().equals(CANCEL)){
            //只有这两个状态需要对账，
            Account account = this.getAccount();
            PayTrans queryTrans;
            if (Objects.isNull(this.getTransNo())){//检测null 不能用 equals
                //删去outNo的判空 outNo不可能为空
                queryTrans = this.payAdaptor.returnOrderByOutNo(account, this.getOutNo());
            }else{
                queryTrans = this.payAdaptor.returnOrderByTransId(account, this.getTransNo());
            }

            if (queryTrans.getAmount().equals(this.getAmount())){
                queryTrans.setId(this.getId());
            }else{
                //错账
                Ledger ledger = new Ledger();
                ledger.setCheckTime(LocalDateTime.now());
                ledger.setAmount(queryTrans.getAmount());
                ledger.setTransNo(queryTrans.getTransNo());
                ledger.setOutNo(queryTrans.getOutNo());
                ledger.setAccountId(this.getAccountId());
                Ledger newLedger = this.ledgerDao.insert(ledger, SYSTEM);
                PayTrans updateTrans = new PayTrans();
                updateTrans.setId(this.getId());
                updateTrans.setLedgerId(newLedger.getId());
                updateTrans.setStatus(PayTrans.WRONG);
                this.payTransDao.save(updateTrans, SYSTEM);
                this.setLedgerId(newLedger.getId());
                this.setStatus(PayTrans.WRONG);
                ret = false;
            }
        }
        return  ret;
    }
    /**
     * 支付单描述
     */
    @Setter
    @Getter
    private String description;

    /**
     * 支付用户id
     */
    @Setter
    @Getter
    private String spOpenid;

    /**
     * 交易结束时间
     */
    @Setter
    @Getter
    private LocalDateTime timeExpire;

    /**
     * 交易开始时间
     */
    @Setter
    @Getter
    private LocalDateTime timeBegin;

    /**
     * 预支付id
     */
    @Setter
    @Getter
    private String prepayId;

    /**
     * 待分账金额
     */
    @Setter
    @Getter
    private Long divAmount;

    /**
     * 退款中
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte REFUNDING = 1;

    /**
     * 0 正常 1退款中
     */
    @Setter
    @Getter
    private Byte inRefund;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RefundTransDao refundTransDao;
    /**
     * 关联的退款交易
     */
    @ToString.Exclude
    @JsonIgnore
    private List<RefundTrans> refundTransList;

    public List<RefundTrans> getRefundTransList() throws BusinessException {
        if (Objects.isNull(this.refundTransList) && Objects.nonNull(this.refundTransDao)){
            this.refundTransList = this.refundTransDao.retrieveByPayTransId(this.id, 1, MAX_RETURN);
        }
        return this.refundTransList;
    }

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private DivPayTransDao divPayTransDao;
    /**
     * 关联的分账交易
     */
    @ToString.Exclude
    @JsonIgnore
    private DivPayTrans divTrans;

    /**
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    public DivPayTrans getDivTrans() {
        if (Objects.isNull(this.divTrans) && Objects.nonNull(this.divPayTransDao)) {
            this.divTrans = this.divPayTransDao.retrieveByPayTransId(this.id).orElse(null);
        }
        return this.divTrans;
    }

    /**
     * 已经退回和正在处理中的退款总额
     * @author Ming Qiu
     * <p>
     * date: 2022-11-15 15:25
     * @return
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    public Long getRefundAmount(){
        return this.getRefundTransList().stream()
            .filter(trans -> RefundTrans.FAIL != trans.getStatus())
            .map(RefundTrans::getAmount)
            .reduce((x,y)->x + y)
            .orElse(0L);
    }

    /**
     * 已经退回和正在处理中的分账退回总额
     * @author Rui Li
     * task 2023-dgn1-005
     * date: 2022-11-15 15:25
     * @return
     */
    public Long getDivRefundAmount(){
        DivPayTrans divPayTrans = this.getDivTrans();
        if(Objects.isNull(divPayTrans)) {
            return 0L;
        }
        return divPayTrans.getRefundAmount();
    }
    /**
     * 创建支付交易
     * @author Ming Qiu
     * <p>
     * date: 2022-11-14 6:43
     * @param account
     * @param spOpenid
     * @param timeExpire 过期时间，单位秒
     * @param amount
     */
    public PayTrans(LocalDateTime timeBegin, LocalDateTime timeExpire,  String spOpenid,  Long amount, long divAmount, Account account) {
        this.spOpenid = spOpenid;
        this.timeExpire = timeExpire;
        this.spOpenid = spOpenid;
        this.accountId = account.getId();
        this.amount = amount;
        this.timeBegin = timeBegin;
        this.timeExpire = timeExpire;
        this.divAmount = divAmount;
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
