package cn.edu.xmu.oomall.alipay.service.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.alipay.controller.vodto.PostRefundVo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRefundPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@CopyFrom({PostRefundVo.class, AlipayRefundPo.class})
public class Refund {
    private Integer refundStatus;

    private String tradeNo;

    private String outTradeNo;

    /**
     * 退款单号
     */
    private String outRequestNo;

    /**
     * 对应订单总额
     */
    private Integer totalAmount;

    /**
     * 本次退款请求对应金额
     */
    private Integer refundAmount;

    /**
     *
     * 退款时间
     */
    private LocalDateTime gmtRefundPay;

    private List<RefundRoyaltyDetail> refundRoyaltyDetails;

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus= refundStatus.getCode();
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }



    public LocalDateTime getGmtRefundPay() {
        return gmtRefundPay;
    }

    public void setGmtRefundPay(LocalDateTime gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
    }

    public List<RefundRoyaltyDetail> getRefundRoyaltyDetails() {
        return refundRoyaltyDetails;
    }

    public void setRefundRoyaltyDetails(List<RefundRoyaltyDetail> refundRoyaltyDetails) {
        this.refundRoyaltyDetails = refundRoyaltyDetails;
    }
    @CopyFrom.Exclude({PostRefundVo.class})
    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
    @CopyFrom.Exclude({PostRefundVo.class})
    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }


    public enum RefundStatus {
        REFUND_SUCCESS(0, "REFUND_SUCCESS");
        private static final Map<Integer, RefundStatus> STATE_MAP;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            STATE_MAP = new HashMap();
            for (RefundStatus enum1 : values()) {
                STATE_MAP.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        RefundStatus(int code, String description) {
            this.code=code;
            this.description=description;
        }

        public static RefundStatus getStatusByCode(Integer code){
            return STATE_MAP.get(code);
        }

        public Integer getCode(){
            return code;
        }

        public String getDescription() {return description;}

    }
}
