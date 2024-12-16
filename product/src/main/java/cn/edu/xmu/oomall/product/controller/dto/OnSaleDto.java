//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.controller.dto;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.javaee.core.validation.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnSaleDto {
    @Min(value = 0,message = "价格不能小于0!", groups = {NewGroup.class, UpdateGroup.class})
    private Long price = 0L;


    @Min(value = 0,message = "数量不能小于0!", groups = {NewGroup.class, UpdateGroup.class})
    private Integer quantity = 0;

    @Min(value = 1,message = "一次销售数量不能小于1!", groups = {NewGroup.class, UpdateGroup.class})
    private Integer maxQuantity = 1;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "开始时间不能为空!", groups = NewGroup.class)
    private LocalDateTime beginTime;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "结束时间不能为空!", groups = NewGroup.class)
    private LocalDateTime endTime;

    @Min(value = 0,message = "定金不能小于0!", groups = {NewGroup.class, UpdateGroup.class})
    private Long deposit = 0L;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime payTime;

    private Byte type = 0;

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getPayTime() {
        return payTime;
    }

    public void setPayTime(LocalDateTime payTime) {
        this.payTime = payTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
