package cn.edu.xmu.oomall.shop.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * @author chenyz
 * @date 2022-11-26 21:08
 */
@Data
@NoArgsConstructor
public class ShopAuditDto {
    @NotNull(message = "审核结果不能为空")
    private Boolean conclusion;

    public Boolean getConclusion() {
        return this.conclusion;
    }
}
