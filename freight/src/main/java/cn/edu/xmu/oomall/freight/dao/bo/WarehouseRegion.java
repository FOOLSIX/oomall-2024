package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.mapper.po.WarehousePo;
import cn.edu.xmu.oomall.freight.mapper.po.WarehouseRegionPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 37220222203558
 * 2024-dsg116
 */

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@Getter
@Setter
@Slf4j
@CopyFrom({WarehouseRegionPo.class})
@CopyTo({WarehouseRegionPo.class})
public class WarehouseRegion extends OOMallObject implements Serializable {

    private Long id;

    /**
     * 仓库Id
     */
    private Long warehouseId;

    /**
     * 地区Id
     */
    private Long regionId;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}
