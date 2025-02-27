package cn.edu.xmu.oomall.alipay.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 解绑分账响应参数
 * @author huangzian
 * 2023-dgn1-006
 */
@AllArgsConstructor
@Data
public class CancelDivDto {
    /**
     * SUCCESS：分账关系解绑成功； FAIL：分账关系解绑失败。
     */
    private String resultCode;
}
