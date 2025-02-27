package cn.edu.xmu.oomall.jtexpress.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddOrderReturnVo {
    private String lastCenterName;
    private String txlogisticId;
    private String createOrderTime;
    private String billCode;
    private String sortingCode;
    private String sumFreight;
}
