//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 请求退款返回值
 * */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDivRefundRetObj {
    /*必选*/
    private String sub_mchid; //分账回退的接收商户，对应原分账出资的分账方商户，填写微信支付分配的商户号
    private String order_id; //微信分账单号，微信支付系统返回的唯一标识。
    private String out_order_no; //商户系统内部的分账单号，在商户系统内部唯一，同一分账单号多次请求等同一次。
    private String out_return_no; //此回退单号是商户在自己后台生成的一个新的回退单号，在商户后台唯一
    private String return_id; //微信分账回退单号，微信支付系统返回的唯一标识
    private String return_mchild; //只能对原分账请求中成功分给商户接收方进行回退
    private String channel; // ORIGINAL：原路退款;BALANCE：退回到余额;OTHER_BALANCE：原账户异常退到其他余额账户;OTHER_BANKCARD：原银行卡异常退到其他银行卡
    /**
     * 取当前退款单的退款入账方，有以下几种情况：
     * 1）退回银行卡：{银行名称}{卡类型}{卡尾号} 招商银行信用卡0403
     * 2）退回支付用户零钱:支付用户零钱
     * 3）退还商户:商户基本账户商户结算银行账户
     * 4）退回支付用户零钱通:支付用户零钱通
     */
    private String user_received_account;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime create_time; //分账回退创建时间
    /**
     * 如果请求返回为处理中，则商户可以通过调用回退结果查询接口获取请求的最终处理结果。如果查询到回退结果在处理中，请勿变更商户回退单号，使用相同的参数再次发起分账回退，否则会出现资金风险。在处理中状态的回退单如果5天没有成功，会因为超时被设置为已失败。
     * 枚举值：
     * PROCESSING：处理中
     * SUCCESS：已成功
     * FAILED：已失败
     */
    private String result;
    private Long amount; //需要从分账接收方回退的金额，单位为分，只能为整数

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime finish_time; //分账回退完成时间
}
