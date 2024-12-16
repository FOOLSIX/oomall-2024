package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
/**
 * 解绑分账请求参数
 * @author huangzian
 * 2023-dgn1-006
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelDivParam {
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
}
