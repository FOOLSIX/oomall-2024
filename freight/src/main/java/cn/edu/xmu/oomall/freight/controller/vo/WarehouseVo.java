package cn.edu.xmu.oomall.freight.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.dao.bo.WarehouseRegion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import java.time.LocalDateTime;

/**
 * @author 37220222203558
 * 2024-dsg-116
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@CopyFrom(Warehouse.class)
public class WarehouseVo {

    public WarehouseVo(Warehouse warehouse){
        CloneFactory.copy(this, warehouse);
        this.setRegion(IdNameTypeVo.builder().id(warehouse.getRegionId())
                .name(warehouse.getRegionName()).build());
        this.setCreator(IdNameTypeVo.builder().id(warehouse.getCreatorId())
                .name(warehouse.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(warehouse.getModifierId())
                .name(warehouse.getModifierName()).build());
    }

    private String name;
    private String address;
    private IdNameTypeVo region;
    private String senderName;
    private String senderMobile;
    private int type;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameTypeVo creator;
    private IdNameTypeVo modifier;
}
