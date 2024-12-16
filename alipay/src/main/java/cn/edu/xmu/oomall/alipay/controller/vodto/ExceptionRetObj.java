//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.alipay.controller.vodto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付宝接口异常返回
 * https://opendocs.alipay.com/open-v3/054oog?pathHash=7834d743
 */
@NoArgsConstructor
@Data
public class ExceptionRetObj {
    private String code;
    private String message;
    private Detail details;

    @NoArgsConstructor
    @Data
    public class Detail {
        private String field;
        private String value;
        private String issue;
        private String location;
        private String description;
    }
}
