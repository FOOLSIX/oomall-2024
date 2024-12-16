package cn.edu.xmu.oomall.jtexpress.controller.dto;


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
public class TracesReturnObj {
    String billCode;
    ArrayList<TraceDetail>details;
}
