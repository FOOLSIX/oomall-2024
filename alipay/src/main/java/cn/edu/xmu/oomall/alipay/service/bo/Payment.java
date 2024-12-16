package cn.edu.xmu.oomall.alipay.service.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiver;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPo;
import cn.edu.xmu.oomall.alipay.controller.vodto.PostPayVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@CopyFrom({PostPayVo.class,AlipayPaymentPo.class})
@ToString
public class Payment {

    private String appid;

    private String buyerLogonId;


    /**
     * 商户订单号
     */
    private String outTradeNo;

    private String tradeNo;

    @JsonIgnore
    private Integer tradeStatus;

    /**
     * 订单总金额。
     * 单位为分
     */
    private Integer totalAmount;

    /**
     * 收款方支付宝账号
     * */
    private String receiverAccount;

    /**
     * 支付时间，如果没有支付那么这里为空
     */
    private LocalDateTime successTime;

    private List<RoyaltyEntity> royaltyEntities;

    private Settlement settlement;


    private Refund refund;





    public TradeStatus getTradeStatus() {
        return TradeStatus.getStatusByCode(tradeStatus);
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus=tradeStatus.getCode();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

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

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    @CopyFrom.Exclude({PostPayVo.class})
    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(String receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public List<RoyaltyEntity> getRoyaltyEntities() {
        return royaltyEntities;
    }

    public void setRoyaltyEntities(List<RoyaltyEntity> royaltyEntities) {
        this.royaltyEntities = royaltyEntities;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "appid='" + appid + '\'' +
                ", buyerLogonId='" + buyerLogonId + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", tradeStatus=" + tradeStatus +
                ", totalAmount=" + totalAmount +
                ", receiverAccount='" + receiverAccount + '\'' +
                ", successTime=" + successTime +
                ", royaltyEntities=" + royaltyEntities +
                ", settlement=" + settlement +
                ", refund=" + refund +
                '}';
    }


    public enum TradeStatus {
        TRADE_CLOSED(0, "TRADE_CLOSED"),
        TRADE_SUCCESS(1, "TRADE_SUCCESS"),
        WAIT_BUYER_PAY(2, "WAIT_BUYER_PAY"),
        TRADE_FINISHED(3, "TRADE_FINISHED");
        private static final Map<Integer, TradeStatus> STATE_MAP;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            STATE_MAP = new HashMap();
            for (TradeStatus enum1 : values()) {
                STATE_MAP.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        TradeStatus(int code, String description) {
            this.code=code;
            this.description=description;
        }

        public static TradeStatus getStatusByCode(Integer code){
            return STATE_MAP.get(code);
        }

        public Integer getCode(){
            return code;
        }

        public String getDescription() {return description;}

    }
}
