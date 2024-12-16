package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 绑定分账请求参数
 * @author huangzian
 * 2023-dgn1-006
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDivParam {
    /*必选*/
    /**
     * 子商户号
     */
    private String sub_mchid;
    /**
     * 应用ID
     * account.channel.appid
     */
    private String appid;
    /**
     * 分账接收方类型
     */
    private String type="MERCHANT_ID";
    /**
     * 分账接收方账号
     * account.channel.SpMchid
     */
    private String account;
    /**
     * 与分账方的关系类型
     */
    private String relation_type="SERVICE_PROVIDER";
}
