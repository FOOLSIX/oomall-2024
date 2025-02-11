package cn.edu.xmu.oomall.freight.mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "freight_warehouse_region")
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
public class WarehouseRegionPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long warehouseId;

    private Long regionId;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
