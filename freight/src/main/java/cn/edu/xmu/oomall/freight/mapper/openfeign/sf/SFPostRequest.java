package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 顺丰post请求通用格式
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@NoArgsConstructor
@Data
public class SFPostRequest<T> {

    /**
     * 合作伙伴编码（即顾客编码）
     */
    @JsonProperty("partnerID")
    @NotBlank
    private String partnerID;

    /**
     * 请求唯一号UUID
     */
    @JsonProperty("requestID")
    @NotBlank
    private String requestID;

    /**
     * 接口服务代码
     */
    @JsonProperty("serviceCode")
    @NotBlank
    private String serviceCode;

    /**
     * 调用接口时间戳
     */
    @JsonProperty("timestamp")
    @NotBlank
    private String timestamp;

    /**
     * 数字签名,使用数字签名方式认证时必填，不可与accessToken字段同时传参
     * 签名方法参考:
     * https://open.sf-express.com/developSupport/976720?authId=1
     */
    @JsonProperty("msgDigest")
    @NotBlank
    private String msgDigest;

    /**
     * 业务数据报文
     */
    @JsonProperty("msgData")
    @NotBlank
    private T msgData;
}
