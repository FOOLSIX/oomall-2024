//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.alipay.controller.vodto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoyaltyDetailInfoPojo {
    /*必选*/
    /**
     * 收入方账户(商家)
     * shop.submchid
     */
    private String transIn;

    /*可选*/
    private String transInType = "userId"; //userId表示是支付宝账号对应的支付宝唯一用户号;cardAliasNo表示是卡编号;loginName表示是支付宝登录号；
    private String transTnName; // 分账收款方姓名

    /**
     * 支出方账户(支付宝)
     * channel.spmchid
     */
    private String transOut;
    private String transOutType = "userId"; // 支出方账户类型,userId表示是支付宝账号对应的支付宝唯一用户号;loginName表示是支付宝登录号

    private String royaltyType = "transfer"; // 默认为transfer 分账
    private String desc; // 分账描述
    private String royaltyScene; // 可选值：达人佣金、平台服务费、技术服务费、其他
    /**
     *
     */
    private Double amount; // 分账金额

    public String getTransIn() {
        return transIn;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn;
    }

    public String getTransInType() {
        return transInType;
    }

    public void setTransInType(String transInType) {
        this.transInType = transInType;
    }

    public String getTransTnName() {
        return transTnName;
    }

    public void setTransTnName(String transTnName) {
        this.transTnName = transTnName;
    }

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut;
    }

    public String getTransOutType() {
        return transOutType;
    }

    public void setTransOutType(String transOutType) {
        this.transOutType = transOutType;
    }

    public String getRoyaltyType() {
        return royaltyType;
    }

    public void setRoyaltyType(String royaltyType) {
        this.royaltyType = royaltyType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRoyaltyScene() {
        return royaltyScene;
    }

    public void setRoyaltyScene(String royaltyScene) {
        this.royaltyScene = royaltyScene;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
