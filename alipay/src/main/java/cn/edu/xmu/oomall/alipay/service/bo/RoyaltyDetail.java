package cn.edu.xmu.oomall.alipay.service.bo;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.controller.vodto.OpenApiRoyaltyDetailInfoPojo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRoyaltyDetail;

@CopyFrom({OpenApiRoyaltyDetailInfoPojo.class, AlipayRoyaltyDetail.class})
public class RoyaltyDetail {
    private Long id;

    private String transIn;

    private String transOut;


    private Integer amount;

    private String settleNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransIn() {
        return transIn;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn == null ? null : transIn.trim();
    }

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut == null ? null : transOut.trim();
    }

    public Integer getAmount() {
        return amount;
    }
    @CopyFrom.Exclude({OpenApiRoyaltyDetailInfoPojo.class})
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getSettleNo() {
        return settleNo;
    }

    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo == null ? null : settleNo.trim();
    }
}