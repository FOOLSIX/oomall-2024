package cn.edu.xmu.oomall.alipay.controller.vodto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发起退款(退分账)参数
 * @author LianShuQuan
 * */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRefundVo {
    /*必选*/
    /**
     * 退款金额
     * */
    private Double refundAmount;

    /*二选一*/
    /**
     * 支付交易号
     * refundTrans.payTrans.trans_no
     * */
    private String tradeNo;

    private String outTradeNo;

    /*可选*/
    /**
     * 退款请求号。 标识一次退款请求，需要保证在交易号下唯一
     * refundTrans.outNo
     * */
    private String outRequestNo;

    /*退分账*/
    /**
     * 退分账明细
     * */
    private List<RoyaltyDetailInfoPojo> refundRoyaltyParameters;

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
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

    public List<RoyaltyDetailInfoPojo> getRefundRoyaltyParameters() {
        return refundRoyaltyParameters;
    }

    public void setRefundRoyaltyParameters(List<RoyaltyDetailInfoPojo> refundRoyaltyParameters) {
        this.refundRoyaltyParameters = refundRoyaltyParameters;
    }
}

