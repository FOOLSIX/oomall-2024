//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.controller.dto;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static cn.edu.xmu.javaee.core.model.Constants.*;

/**
 * 支付的vo
 */
@NoArgsConstructor
public class PayTransDto {

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timeBegin = LocalDateTime.now();

    /**
     * alipay: time_expire 绝对超时时间，格式为yyyy-MM-dd HH:mm:ss。超时时间
     * wepay: time_expire 订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，
     */
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime timeExpire = BEGIN_TIME.plusMinutes(30L);

    /**
     * alipay: total_amount 商户网站唯一订单号
     * wepay: amount.total 订单总金额，单位为分。
     */
    @Min(value = 0, message="付款金额需大于等于0")
    private Long amount;

    @Min(value = 0, message="付款分账金额需大于等于0")
    private Long divAmount;

    /**
     * alipay: out_trade_no 商户网站唯一订单号
     * wepay: out_trade_no 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。
     */
    @NotBlank( message="订单号不能为null")
    private String outNo;

    /**
     * alipay: subject 订单标题。
     * wepay: description 商品描述
     */
    @NotBlank( message="描述不能为null")
    private String description;

    /**
     * alipay:
     * wepay: sp_openid 用户在服务商appid下的唯一标识。 下单前需获取到用户的Openid
     */
    private String spOpenId;

    public String getSpOpenId() {
        return spOpenId;
    }

    public void setSpOpenId(String spOpenId) {
        this.spOpenId = spOpenId;
    }

    public LocalDateTime getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(LocalDateTime timeBegin) {
        this.timeBegin = timeBegin;
    }

    public LocalDateTime getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(LocalDateTime timeExpire) {
        this.timeExpire = timeExpire;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getDivAmount() {
        return divAmount;
    }

    public void setDivAmount(Long divAmount) {
        this.divAmount = divAmount;
    }


    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
