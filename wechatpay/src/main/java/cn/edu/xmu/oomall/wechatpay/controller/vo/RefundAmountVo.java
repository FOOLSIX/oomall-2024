package cn.edu.xmu.oomall.wechatpay.controller.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundAmountVo {
    @NotNull
    private int refund;
    @NotNull
    private int total;
    private final String currency = "CNY";
}
