package cn.edu.xmu.oomall.freight.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.freight.controller.vo.WarehouseVo;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Entity
@Table(name = "freight_warehouse")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class WarehousePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private Long shopId;

    private String name;

    private String senderName;

    private String senderMobile;

    private Long regionId;

    private Byte invalid;

    protected Long creatorId;

    protected String creatorName;

    protected Long modifierId;

    protected String modifierName;

    protected LocalDateTime gmtCreate;

    protected LocalDateTime gmtModified;
}
