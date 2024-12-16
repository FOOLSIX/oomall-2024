package cn.edu.xmu.oomall.sfexpress.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchRoute请求参数
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostSearchRoutesDTO {

    @JsonProperty("language")
    private String language = "zh-CN";

    /**
     * 查询号类别:
     * 1:根据顺丰运单号查询,trackingNumber将被当作顺丰运单号处理
     * 2:根据客户订单号查询,trackingNumber将被当作客户订单号处理
     */
    @JsonProperty("trackingType")
    @Pattern(regexp = "[1-2]", message = "8191")
    private int trackingType;

    /**
     * 查询号:
     * trackingType=1,则此值为顺丰运单号
     * 如果trackingType=2,则此值为客户订单号
     */
    @JsonProperty("trackingNumber")
    @NotBlank(message = "8013")
    private List<String> trackingNumber;
}
