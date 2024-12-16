//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign.wepay;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信支付错误返回
 * https://pay.weixin.qq.com/docs/partner/development/interface-rules/basic-rules.html
 */
@NoArgsConstructor
@Data
public class ExceptionRetObj {

    private String code;
    private String message;

    private Detail detail;

    @NoArgsConstructor
    @Data
    public class Detail {
        private String field;
        private String value;
        private String issue;
        private String location;
    }
}
