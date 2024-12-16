package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.*;

import java.util.List;

/**
 * 发起分账参数
 * @author Wenbo Li
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDivPayParam {
    /*必选*/
    /**
     * 结算请求流水号，由商家自定义
     * divpayTrans.out_no
     * */
    String out_request_no;
    /**
     * 支付宝订单号
     * divpayTrans.payTrans.trans_no
     * */
    String trade_no;
    /**
     * 分账明细信息
     * */
    List<OpenApiRoyaltyDetailInfoPojo> royalty_parameters;
}

