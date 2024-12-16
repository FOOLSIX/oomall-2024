package cn.edu.xmu.oomall.sfexpress.controller.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class ApiResultData<T> {
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("errorMsg")
    private String errorMsg;
    @JsonProperty("msgData")
    private T msgData;
}
