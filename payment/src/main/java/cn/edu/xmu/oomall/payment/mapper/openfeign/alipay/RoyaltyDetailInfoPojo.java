//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoyaltyDetailInfoPojo {
    /*必选*/
    /**
     * 收入方账户(商家)
     * shop.submchid
     */
    private String trans_in;

    /*可选*/
    private String trans_in_type = "userId"; //userId表示是支付宝账号对应的支付宝唯一用户号;cardAliasNo表示是卡编号;loginName表示是支付宝登录号；
    private String trans_in_name; // 分账收款方姓名

    /**
     * 支出方账户(支付宝)
     * channel.spmchid
     */
    private String trans_out;
    private String trans_out_type = "userId"; // 支出方账户类型,userId表示是支付宝账号对应的支付宝唯一用户号;loginName表示是支付宝登录号

    private String royalty_type = "transfer"; // 默认为transfer 分账
    private String desc; // 分账描述
    private String royalty_scene; // 可选值：达人佣金、平台服务费、技术服务费、其他
    /**
     *
     */
    private Double amount; // 分账金额
}
