package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
/**
 * 分账方参数
 * @author huangzian
 * 2023-dgn1-006
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoyaltyEntity {
    /**
     * 分账接收方方类型
     */
    private String type="userId";
    /**
     * 分账接收方账号
     * account.channel.SpMchid
     */
    private String account;
}
