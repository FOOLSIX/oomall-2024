package cn.edu.xmu.oomall.jtexpress.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CancelOrderDto {
    /**
     * 客户编码
     */
    @NotNull(message = "非法参数")
    @Size(max = 30)
    private String customerCode;

    /**
     * 签名，Base64(Md5(客户编号+密文+privateKey))
     * 密文对应 ShopLogistics的secret
     */
    @NotNull(message = "非法参数")
    @Size(max = 50)
    private String digest;
    /**
     * 订单类型
     * 1（散客），2（协议客户）
     * 固定为2
     */
    @NotNull(message = "非法参数")
    @Size(max = 30)
    @Pattern(regexp = "[1-2]", message = "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法")
    private String orderType;

    /**
     * 客户订单号
     * 传客户自己系统的订单号
     */
    @NotNull(message = "非法参数")
    @Size(max = 50)
    @JsonProperty("txlogisticId")
    private String txLogisticId;

    /**
     * 取消原因
     */
    @NotNull(message = "取消原因不能为空")
    @Size(max = 50)
    private String reason;

}
