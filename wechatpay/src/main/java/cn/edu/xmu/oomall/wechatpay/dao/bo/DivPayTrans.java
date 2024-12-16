package cn.edu.xmu.oomall.wechatpay.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.wechatpay.controller.vo.DivPayTransVo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivPayTransPo;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author maguoqi
 * @date 2023/12/4
 * @check orderId, state, successTime
 */
@NoArgsConstructor
@CopyFrom({DivPayTransPo.class})
public class DivPayTrans {

    private String appid;

    private String subMchid;

    private String transactionId;

    private String outOrderNo;

    private Boolean unfreezeUnsplit;

    private String orderId;  // start with "O"

    private String state;

    private LocalDateTime successTime;

    private Collection<DivReceiver> receivers = new ArrayList<>();

    public DivPayTrans(DivPayTransVo divPayTransVo) {
        this.appid = divPayTransVo.getAppid();
        this.subMchid = divPayTransVo.getSubMchid();
        this.transactionId = divPayTransVo.getTransactionId();
        this.outOrderNo = divPayTransVo.getOutOrderNo();
        this.receivers = divPayTransVo.getReceivers().stream().map(DivReceiver::new).collect(Collectors.toCollection(ArrayList::new));
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public Boolean getUnfreezeUnsplit() {
        return unfreezeUnsplit;
    }

    public void setUnfreezeUnsplit(Boolean unfreezeUnsplit) {
        this.unfreezeUnsplit = unfreezeUnsplit;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }

    public Collection<DivReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(Collection<DivReceiver> receivers) {
        this.receivers = receivers;
    }
}
