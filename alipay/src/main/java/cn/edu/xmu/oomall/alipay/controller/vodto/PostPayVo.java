//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.alipay.controller.vodto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 下单参数
 * @author Wenbo Li
 * */

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostPayVo {
    /**
     * 商户订单号
     * payTrans.out_no
     * */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 订单描述
     * */
    @JsonProperty("subject")
    private String subject = "订单支付";
    /**
     * 订单总金额
     * payTrans.amount
     * */
    @JsonProperty("total_amount")
    private Double totalAmount;

    /**
     * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
     */
    @JsonProperty("notify_url")
    private String notifyUrl;


    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
