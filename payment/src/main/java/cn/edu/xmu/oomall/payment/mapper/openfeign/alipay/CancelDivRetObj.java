package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 解绑分账响应参数
 * @author huangzian
 * 2023-dgn1-006
 */
@NoArgsConstructor
@Data
public class CancelDivRetObj {
    /**
     * SUCCESS：分账关系解绑成功； FAIL：分账关系解绑失败。
     */
    private String result_code;
}
