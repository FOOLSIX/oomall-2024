//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.dto;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.javaee.core.validation.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    @NotBlank(message = "类目名称不能为空", groups = {NewGroup.class})
    private String name;

    @Min(value = 0, message = "抽佣比例不能小于0", groups = {NewGroup.class, UpdateGroup.class})
    @Max(value = 100, message = "抽佣比例不能大于100", groups = {NewGroup.class, UpdateGroup.class})
    private Integer commissionRatio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(Integer commissionRatio) {
        this.commissionRatio = commissionRatio;
    }
}
