package cn.edu.xmu.oomall.sfexpress.controller.vo;

import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 顺丰返回参数
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 * @param <T>
 */
@NoArgsConstructor
@Data
public class SFResponseVo<T> {

    @JsonProperty("apiErrorMsg")
    private String apiErrorMsg;
    @JsonProperty("apiResponseID")
    private String apiResponseID;
    @JsonProperty("apiResultCode")
    private String apiResultCode;
    @JsonProperty("apiResultData")
    private ApiResultData<T> apiResultData;

}
