//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.alipay.controller.vodto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.service.bo.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 下单返回值
 * */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({Payment.class})
public class PostPayDto{
    /*必选*/
    /**
     * 商家订单号
     * */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 支付宝交易号
     * */
    @JsonProperty("trade_no")
    private String tradeNo;
    /**
     * 交易总金额（元）
     * */
    @JsonProperty("total_amount")
    private Double totalAmount;
    /**
     * 收款方支付宝账号
     * */
    @JsonProperty("receiver_account")
    private String receiverAccount;

    @JsonProperty("seller_id")
    private String sellerId;
    /**
     * 商户原始订单号
     * */
    @JsonProperty("merchant_order_no")
    private String merchantOrderNo;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    @CopyFrom.Exclude({Payment.class})
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }
}
