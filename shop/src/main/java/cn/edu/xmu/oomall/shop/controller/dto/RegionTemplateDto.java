package cn.edu.xmu.oomall.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public abstract class RegionTemplateDto {
    @Min(value = 1, message = "计量单位至少为1")
    protected Integer unit;

    @Min(value = 1, message = "数量上限至少为1")
    protected Integer upperLimit;

    public abstract void setUnit(Integer unit);
    public abstract Integer getUnit();

    public abstract void setUpperLimit(Integer upperLimit);

    public abstract Integer getUpperLimit();
}
