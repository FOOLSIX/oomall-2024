//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.dto;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CommissionRatioDto {
    @NotNull
    @Min(value = 0, message = "佣金比例不合法", groups = {NewGroup.class})
    @Max(value = 100, message = "佣金比例不合法", groups = {NewGroup.class})
    private Integer commissionRatio;

    public Integer getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(Integer commissionRatio) {
        this.commissionRatio = commissionRatio;
    }
}
