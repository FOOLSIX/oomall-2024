package cn.edu.xmu.oomall.freight.mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Data
@Entity
@Table(name = "freight_logistics")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogisticsPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String appId;

    private String appAccount;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private String snPattern;

    private String secret;

    private String logisticsClass;
}
