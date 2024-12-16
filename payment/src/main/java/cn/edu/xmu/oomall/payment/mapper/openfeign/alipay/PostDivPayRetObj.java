package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 发起分账返回值
 * @author Wenbo Li
 * */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDivPayRetObj{
    /**
     * 支付宝交易号
     */
    private String trade_no;
    /**
     * 支付宝分账单号，可以根据该单号查询单次分账请求执行结果
     */
    private String settle_no; // 退还账号
}
