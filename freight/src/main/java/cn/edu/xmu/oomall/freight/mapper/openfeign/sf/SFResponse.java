package cn.edu.xmu.oomall.freight.mapper.openfeign.sf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 顺丰返回参数
 * @author Zhouzhe Fan
 * 2023-dgn2-002
 * @param <T>
 */
@NoArgsConstructor
@Data
public class SFResponse<T> {

    /**
     * api错误信息
     */
    @JsonProperty("apiErrorMsg")
    private String apiErrorMsg;

    /**
     * api响应ID
     */
    @JsonProperty("apiResponseID")
    private String apiResponseID;

    /**
     * api响应码，A1000代表成功，A1001代表参数错误
     */
    @JsonProperty("apiResultCode")
    private String apiResultCode;

    /**
     * api响应报文
     */
    @JsonProperty("apiResultData")
    private ApiResultData<T> apiResultData;

}
