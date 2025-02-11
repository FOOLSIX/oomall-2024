package cn.edu.xmu.oomall.jtexpress.controller.vo;


import cn.edu.xmu.oomall.jtexpress.dao.bo.TraceDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TracesReturnVo {
    String billCode;
    ArrayList<TraceDetail>details;
}
