package cn.edu.xmu.oomall.sfexpress.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 顺丰post请求通用格式
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class SFPostRequestDTO<T> {

    /**
     * 合作伙伴编码（即顾客编码）
     */
    @JsonProperty("partnerID")
    private String partnerID;

    /**
     * 请求唯一号UUID
     */
    @JsonProperty("requestID")
    private String requestID;

    /**
     * 接口服务代码
     */
    @JsonProperty("serviceCode")
    private String serviceCode;

    /**
     * 调用接口时间戳
     */
    @JsonProperty("timestamp")
    private String timestamp;

    /**
     * 数字签名,使用数字签名方式认证时必填，不可与accessToken字段同时传参
     * 签名方法参考:
     * https://open.sf-express.com/developSupport/976720?authId=1
     */
    @JsonProperty("msgDigest")
    private String msgDigest;

    /**
     * 业务数据报文
     */
    @JsonProperty("msgData")
    private T msgData;
}
