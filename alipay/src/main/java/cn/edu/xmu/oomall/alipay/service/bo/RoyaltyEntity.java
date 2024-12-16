package cn.edu.xmu.oomall.alipay.service.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
/**
 * 分账方参数
 * @author huangzian
 * 2023-dgn1-006
 */
@NoArgsConstructor
@AllArgsConstructor
@CopyFrom({AlipayDivReceiver.class})
public class RoyaltyEntity {

    private String outRequestNo;
    /**
     * 分账接收方方类型
     */
    private String type="userId";
    /**
     * 分账接收方账号
     * shopChannel.channel.SpMchid
     */
    private String account;

    private String name;

    private String bindLogInName;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBindLogInName() {
        return bindLogInName;
    }

    public void setBindLogInName(String bindLogInName) {
        this.bindLogInName = bindLogInName;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }
}
