//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.alipay.controller.vodto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OpenApiRoyaltyDetailInfoPojo {
    /*必选*/
    /**
     * 收入方支付宝账号(平台支付宝账号)
     * channel.spmchid
     */
    private String transIn;

    /*可选*/
    private String royaltyType = "transfer";
    /**
     * 支出方账号
     * channel.submchid
     */
    private String transOut;
    private String transInType = "userId";
    private String transOutType = "userId";
    /**
     * 分账的金额，单位为元
     */
    private Double amount;

    public String getTransIn() {
        return transIn;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn;
    }

    public String getRoyaltyType() {
        return royaltyType;
    }

    public void setRoyaltyType(String royaltyType) {
        this.royaltyType = royaltyType;
    }

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut;
    }

    public String getTransInType() {
        return transInType;
    }

    public void setTransInType(String transInType) {
        this.transInType = transInType;
    }

    public String getTransOutType() {
        return transOutType;
    }

    public void setTransOutType(String transOutType) {
        this.transOutType = transOutType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
