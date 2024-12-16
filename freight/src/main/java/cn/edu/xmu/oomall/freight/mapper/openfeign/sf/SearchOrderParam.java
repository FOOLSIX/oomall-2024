package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * createOrder的msgData
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@NoArgsConstructor
@Data
public class SearchOrderParam {

    @JsonProperty("orderId")
    @NotBlank(message = "寄送订单号不能为空")
    private String orderId;
    @JsonProperty("language")
    private String language = "zh-CN";
}
