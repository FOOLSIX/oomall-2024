package cn.edu.xmu.oomall.alipay.controller.vodto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发起分账参数
 * @author Wenbo Li
 * */

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDivPayVo {
    /*必选*/
    /**
     * 结算请求流水号，由商家自定义
     * divpayTrans.out_no
     * */
    @JsonProperty("out_request_no")
    String outRequestNo;
    /**
     * 支付宝订单号
     * divpayTrans.payTrans.trans_no
     * */
    @JsonProperty("trade_no")
    String tradeNo;
    /**
     * 分账明细信息
     * */
    List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters;

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public List<OpenApiRoyaltyDetailInfoPojo> getRoyaltyParameters() {
        return royaltyParameters;
    }

    public void setRoyaltyParameters(List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters) {
        this.royaltyParameters = royaltyParameters;
    }
}

