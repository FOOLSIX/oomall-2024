//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.DivPayTransDao;
import cn.edu.xmu.oomall.payment.dao.DivRefundTransDao;
import cn.edu.xmu.oomall.payment.dao.PayTransDao;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptor;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.dao.channel.vo.PostRefundAdaptorVo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.DivPayTransPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 支付分账交易
 */
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@CopyFrom({DivPayTransPo.class})
@CopyTo(DivPayTransPo.class)
public class DivPayTrans extends Transaction{

    private static  final Logger logger = LoggerFactory.getLogger(DivPayTrans.class);


    /**
     * 未分账
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte NEW = 0;
    /**
     * 已分账
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
     * 失败
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Byte FAIL = 4;

    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(NEW, "未分账");
            put(SUCCESS, "已分账");
            put(WRONG, "错账");
            put(FAIL, "失败");
        }
    };

    protected static Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(NEW, new HashSet<>(){
                {
                    add(SUCCESS);
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

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private DivPayTransDao divPayTransDao;
    @Override
    public void adjust(UserDto user) {
        if (!this.allowStatus(DivPayTrans.SUCCESS) || !this.status.equals(WRONG)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "支付分账交易", this.id, STATUSNAMES.get(this.status)));
        }else {
            DivPayTrans trans = new DivPayTrans();
            trans.setId(this.id);
            trans.setAdjustId(user.getId());
            trans.setAdjustName(user.getName());
            trans.setStatus(DivPayTrans.SUCCESS);
            this.divPayTransDao.save(trans, user);
        }
    }

    @Override
    public String getTransName() {
        return "支付分账交易";
    }

    @Setter
    @Getter
    private Long payTransId;

    /**
     * 关联的支付交易
     */
    @ToString.Exclude
    @JsonIgnore
    private PayTrans trans;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private PayTransDao payTransDao;

    public PayTrans getTrans(){
        if (Objects.isNull(trans) && !Objects.isNull(this.payTransDao)){
            this.trans = payTransDao.findById(this.shopId, this.payTransId);
        }
        return this.trans;
    }

    @ToString.Exclude
    @JsonIgnore
    private List<DivRefundTrans> divRefundTransList;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private DivRefundTransDao divRefundTransDao;

    public List<DivRefundTrans> getDivRefundTransList(){
        if (Objects.isNull(this.divRefundTransList) && !Objects.isNull(this.divRefundTransDao)){
            this.divRefundTransList = this.divRefundTransDao.retrieveByDivPayTransId(this.id);
        }
        return this.divRefundTransList;
    }

    /**
     * 已经退回和正在处理中的分账退回总额
     * @author Ming Qiu
     * <p>
     * date: 2022-11-15 15:24
     * @return
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    public Long getRefundAmount(){
        return this.getDivRefundTransList().stream()
                .filter(trans -> DivRefundTrans.CANCEL != trans.getStatus() && DivRefundTrans.FAIL != trans.getStatus())
                .map(DivRefundTrans::getAmount)
                .reduce((x,y)->x + y).orElse(0L);
    }

    @ToString.Exclude
    @JsonIgnore
    private PayAdaptor payAdaptor;

    public void setPayAdaptor(PayAdaptorFactory factory){
        this.payAdaptor = factory.createPayAdaptor(this.getChannel());
    }

    /**
     * 退分账
     * @param refundTrans 退款交易
     * @param user
     * @return
     */
    public void refund(RefundTrans refundTrans, UserDto user){
        //需要先调用分账回退API
        DivRefundTrans divRefundTrans = new DivRefundTrans(refundTrans, this);
        DivRefundTrans newObj = this.divRefundTransDao.insert(divRefundTrans, user);
        DivRefundTrans newDivRefundTrans = this.divRefundTransDao.findById(this.shopId, newObj.getId());
        DivRefundTrans updateTrans = new DivRefundTrans();
        try{
            PostRefundAdaptorVo vo = this.payAdaptor.createDivRefund(newDivRefundTrans);
            updateTrans.setStatus(DivRefundTrans.SUCCESS);
            updateTrans.setId(newDivRefundTrans.getId());
            updateTrans.setTransNo(vo.getTransNo());
            updateTrans.setAmount(vo.getAmount());
            updateTrans.setSuccessTime(vo.getSuccessTime());
            this.divRefundTransDao.save(updateTrans, user);
        }catch (Exception e){
            updateTrans.setStatus(DivRefundTrans.FAIL);
            this.divRefundTransDao.save(updateTrans, user);
            throw e;
        }
    }

    public DivPayTrans(PayTrans payTrans) {
        this.status = NEW;
        this.payTransId = payTrans.getId();
        this.amount = payTrans.getDivAmount();
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

    @Override
    public Long getLedgerId() {
        return this.ledgerId;
    }

    @Override
    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
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
