package cn.edu.xmu.oomall.sfexpress.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * searchOrderResp的请求msgData
 *
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostCreateOrderDTO {

    /**
     * 语言，默认zh-CN，必填
     */
    @JsonProperty("language")
    private String language = "zh-CN";

    /**
     * 客户订单号，必填，须唯一
     */
    @JsonProperty("orderId")
    @NotBlank(message = "8013")
    private String orderId;

    /**
     * 货物详细信息
     */
    @JsonProperty("cargoDetails")
    @Valid
    private List<CargoDetailsDTO> cargoDetails;

    /**
     * 拖寄物类型描述,如： 文件，电子产品，衣服等，非必填
     */
    @JsonProperty("cargoDesc")
    private String cargoDesc;

    /**
     * 收寄双方的信息
     */
    @JsonProperty("contactInfoList")
    private List<ContactInfoListDTO> contactInfoList;

    /**
     * 月结卡号
     */
    @JsonProperty("monthlyCard")
    @NotBlank(message = "6126")
    private String monthlyCard;

    /**
     * 付款方式，支持以下值： 1:寄方付 2:收方付 3:第三方付
     * 默认1
     */
    @JsonProperty("payMethod")
    private Integer payMethod = 1;

    /**
     * 快件产品类别， 支持附录 《快件产品类别表》 的产品编码值，仅可使用与顺丰销售约定的快件产品
     * 《快件产品类别表》 :
     * https://open.sf-express.com/developSupport/734349?activeIndex=324604
     */
    @JsonProperty("expressTypeId")
    private Integer expressTypeId;
}
