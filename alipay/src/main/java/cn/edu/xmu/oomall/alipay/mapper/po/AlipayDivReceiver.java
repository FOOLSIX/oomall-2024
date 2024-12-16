package cn.edu.xmu.oomall.alipay.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.service.bo.RoyaltyEntity;

@CopyFrom({RoyaltyEntity.class})
public class AlipayDivReceiver {
    private Long id;

    private String outRequestNo;

    private String transOut;

    private String type;

    private String account;

    private String bindLoginName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo == null ? null : outRequestNo.trim();
    }

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut == null ? null : transOut.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getBindLoginName() {
        return bindLoginName;
    }

    public void setBindLoginName(String bindLoginName) {
        this.bindLoginName = bindLoginName == null ? null : bindLoginName.trim();
    }
}