package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 顺丰api返回报文
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 */
@NoArgsConstructor
@Data
public class ApiResultData<T> {
    /**
     * true 请求成功，false 请求失败
     */
    @JsonProperty("success")
    private Boolean success;
    /**
     * 错误编码，S0000成功
     */
    @JsonProperty("errorCode")
    private String errorCode;
    /**
     * 错误描述
     */
    @JsonProperty("errorMsg")
    private String errorMsg;
    /**
     * 返回的详细数据
     */
    @JsonProperty("msgData")
    private T msgData;
}
