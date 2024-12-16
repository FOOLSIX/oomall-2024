package cn.edu.xmu.oomall.sfexpress.controller.vo;

import cn.edu.xmu.oomall.sfexpress.dao.bo.RouteResps;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchRoutes返回参数
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostSearchRoutesRetVo {


    @JsonProperty("routeResps")
    private List<RouteResps> routeResps;
}
