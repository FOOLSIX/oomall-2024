package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * searchOrderResp的请求msgData
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@NoArgsConstructor
@Data
public class CreateOrderParam {

    /**
     * 语言，默认zh-CN
     */
    @JsonProperty("language")
    private String language = "zh-CN";

    /**
     * 客户订单号，必填，须唯一
     */
    @JsonProperty("orderId")
    @NotBlank
    private String orderId;

    /**
     * 货物详细信息
     */
    @JsonProperty("cargoDetails")
    private List<CargoDetailsDTO> cargoDetails;

    /**
     * 拖寄物类型描述,如： 文件，电子产品，衣服等，非必填
     * 这个描述可以随便填，和极兔的类型不一样
     */
    @JsonProperty("cargoDesc")
    private String cargoDesc;

    /**
     * 收寄双方的信息
     */
    @JsonProperty("contactInfoList")
    @NotBlank
    private List<ContactInfoListDTO> contactInfoList;

    /**
     * 月结卡号
     */
    @JsonProperty("monthlyCard")
    @NotBlank
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
     * 1 顺丰特快
     * 2 顺丰标快
     * 6 顺丰即日
     * 一般选择1
     */
    @JsonProperty("expressTypeId")
    private Integer expressTypeId = 1;

    @NoArgsConstructor
    @Data
    public static class CargoDetailsDTO {
        /**
         * 货物名称
         */
        @JsonProperty("name")
        @NotBlank
        private String name;
    }

    @NoArgsConstructor
    @Data
    public static class ContactInfoListDTO {
        /**
         * 详细地址，若有四级行政区划，如镇/街道等信息可拼接至此字段，
         * 格式样例：镇/街道+详细地址。
         * 若province/city 字段的值不传，此字段必须包含省市信息，避免影响原寄地代码识别，
         * 如：广东省深圳市福田区新洲十一街万基商务大厦10楼；
         * 此字段地址必须详细，否则会影响目的地中转识别；
         */
        @JsonProperty("address")
        private String address;

        /**
         * 所在地级行政区名称，必须是标准的城市称谓 如：北京市、 深圳市、大理白族自治州等；
         * 此字段影响原寄地代码识别， 建议尽可能传该字段的值
         */
        @JsonProperty("city")
        private String city;

        /**
         * 联系人姓名
         */
        @JsonProperty("contact")
        private String contact;

        /**
         * 地址类型： 1，寄件方信息 2，到件方信息
         */
        @JsonProperty("contactType")
        @NotBlank
        private Integer contactType;

        /**
         * 国家或地区代码 例如：内地件CN 香港852，默认CN
         */
        @JsonProperty("country")
        private String country = "CN";

        /**
         * 手机
         */
        @JsonProperty("mobile")
        @NotBlank
        private String mobile;
    }
}
