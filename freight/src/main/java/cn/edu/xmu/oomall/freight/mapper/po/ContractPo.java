package cn.edu.xmu.oomall.freight.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Entity
@Table(name = "freight_contract")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ContractPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long logisticsId;

    private Long shopId;

    private Integer quota;

    private String secret;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private java.time.LocalDateTime gmtCreate;

    private java.time.LocalDateTime gmtModified;

    private Byte invalid;

    private Integer priority;

    private String account;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Long warehouseId;
}
