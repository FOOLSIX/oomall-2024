package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.freight.mapper.po.ContractPo;
import cn.edu.xmu.oomall.freight.mapper.po.WarehousePo;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@Getter
@Setter
@Slf4j
@CopyFrom({WarehousePo.class})
@CopyTo({WarehousePo.class})
public class Warehouse extends OOMallObject implements Serializable {
    public static Byte VALID = 0;

    public static Byte INVALID = 1;

    private String address;

    private Long shopId;

    private String name;

    private String senderName;

    private String SenderMobile;

    private Long regionId;

    private String regionName;

    private Integer priority;

    private Byte invalid;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}
