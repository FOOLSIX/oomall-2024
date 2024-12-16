package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 绑定分账响应参数
 * @author huangzian
 * 2023-dgn1-006
 */
@NoArgsConstructor
@Data
public class CreateDivRetObj {
    /**
     * SUCCESS：分账关系绑定成功； FAIL：分账关系绑定失败。
     */
    private String result_code;
}
