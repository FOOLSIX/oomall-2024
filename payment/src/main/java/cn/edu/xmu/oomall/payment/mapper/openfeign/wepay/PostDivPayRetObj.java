package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求分账返回值
 * @author Wenbo Li
 * */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDivPayRetObj {
    private String sub_mchid; //微信支付分配的子商户号，即分账的出资商户号。
    private String transaction_id; //微信支付订单号
    private String out_order_no; //商户系统内部的分账单号，在商户系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@
    private String order_id; // 微信分账单号，微信支付系统返回的唯一标识
    /**
     * 分账单状态（每个接收方的分账结果请查看receivers中的result字段），枚举值：
     * 1、PROCESSING：处理中
     * 2、FINISHED：分账完成
     */
    private String state;
}
