package cn.edu.xmu.oomall.sfexpress.controller.dto;

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
public class CustomsInfoDTO {
    @JsonProperty("declaredValue")
    private Double declaredValue;
}
