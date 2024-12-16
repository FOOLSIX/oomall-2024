package cn.edu.xmu.oomall.alipay.controller.vodto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 绑定分账响应参数
 * @author huangzian
 * 2023-dgn1-006
 */
@AllArgsConstructor
@Data
public class CreateDivDto {
    /**
     * SUCCESS：分账关系绑定成功； FAIL：分账关系绑定失败。
     */
    private String resultCode;
}
