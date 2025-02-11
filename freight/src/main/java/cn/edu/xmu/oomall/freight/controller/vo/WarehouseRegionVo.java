package cn.edu.xmu.oomall.freight.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 37220222203558
 * 2024-dsg116
 */

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@CopyFrom(WarehouseRegion.class)
public class WarehouseRegionVo {

    private Long id;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private IdNameTypeVo creator;

    private IdNameTypeVo modifier;
}
