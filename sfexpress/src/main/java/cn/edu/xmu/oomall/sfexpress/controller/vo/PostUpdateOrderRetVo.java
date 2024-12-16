package cn.edu.xmu.oomall.sfexpress.controller.vo;

import cn.edu.xmu.oomall.sfexpress.controller.dto.PostUpdateOrderDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * UpdateOrder响应参数
 *
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostUpdateOrderRetVo {
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("waybillNoInfoList")
    private List<PostUpdateOrderDTO.WaybillNoInfoListDTO> waybillNoInfoList;
    @JsonProperty("resStatus")
    private Integer resStatus;
}

