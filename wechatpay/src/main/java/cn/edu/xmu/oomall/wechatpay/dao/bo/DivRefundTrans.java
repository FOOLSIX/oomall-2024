package cn.edu.xmu.oomall.wechatpay.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.wechatpay.controller.vo.DivRefundTransVo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivRefundTransPo;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author maguoqi
 * @date 2023/12/4
 * @check orderId, returnId, result, createTime, finishTime
 */
@NoArgsConstructor
@CopyFrom({DivRefundTransPo.class})
public class DivRefundTrans {

    private String subMchid;
    private String orderId;
    private String outOrderNo;
    private String outReturnNo;
    private String returnMchid;
    private Integer amount;
    private String description;
    private String returnId;  // start with "RT"
    private String result;
    private LocalDateTime createTime;
    private LocalDateTime finishTime;

    public DivRefundTrans(DivRefundTransVo divRefundTransVo){
        this.subMchid = divRefundTransVo.getSubMchid();
        this.outOrderNo = divRefundTransVo.getOutOrderNo();
        this.amount = divRefundTransVo.getAmount();
        this.outReturnNo = divRefundTransVo.getOutReturnNo();
        this.returnMchid = divRefundTransVo.getReturnMchid();
        this.description = divRefundTransVo.getDescription();
    }

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getOutReturnNo() {
        return outReturnNo;
    }

    public void setOutReturnNo(String outReturnNo) {
        this.outReturnNo = outReturnNo;
    }

    public String getReturnMchid() {
        return returnMchid;
    }

    public void setReturnMchid(String returnMchid) {
        this.returnMchid = returnMchid;
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

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
}
