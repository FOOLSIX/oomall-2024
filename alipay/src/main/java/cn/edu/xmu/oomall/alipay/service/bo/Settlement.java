package cn.edu.xmu.oomall.alipay.service.bo;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.controller.vodto.*;
import cn.edu.xmu.oomall.alipay.mapper.po.*;

import java.time.LocalDateTime;

import java.util.List;

@CopyFrom({PostDivPayVo.class, AlipaySettlementPo.class})
public class Settlement {

    private Long id;


    private String settleNo;


    private String outRequestNo;


    private String tradeNo;


    private String outTradeNo;


    private Integer amountTotal;


    private String state;


    private LocalDateTime successTime;

    List<RoyaltyDetail> royaltyDetails;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo == null ? null : settleNo.trim();
    }


    public String getOutRequestNo() {
        return outRequestNo;
    }


    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo == null ? null : outRequestNo.trim();
    }


    public String getTradeNo() {
        return tradeNo;
    }


    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }


    public String getOutTradeNo() {
        return outTradeNo;
    }


    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

     public Integer getAmountTotal() {
        return amountTotal;
    }


    public void setAmountTotal(Integer amountTotal) {
        this.amountTotal = amountTotal;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

     public LocalDateTime getSuccessTime() {
        return successTime;
    }


    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }

    public List<RoyaltyDetail> getRoyaltyDetails() {
        return royaltyDetails;
    }

    public void setRoyaltyDetails(List<RoyaltyDetail> royaltyDetails) {
        this.royaltyDetails = royaltyDetails;
    }


    public String getSettleNo() {
        return settleNo;
    }
}