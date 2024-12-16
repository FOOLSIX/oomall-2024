package cn.edu.xmu.oomall.alipay.controller.vodto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 发起分账返回值
 * @author Wenbo Li
 * */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class PostDivPayDto{
    /**
     * 支付宝交易号
     */
    private String tradeNo;
    /**
     * 支付宝分账单号，可以根据该单号查询单次分账请求执行结果
     */
    private String settleNo; // 退还账号
}
