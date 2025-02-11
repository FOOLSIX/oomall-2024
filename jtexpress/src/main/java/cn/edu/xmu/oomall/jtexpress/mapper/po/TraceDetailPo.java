package cn.edu.xmu.oomall.jtexpress.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.jtexpress.dao.bo.TraceDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jtexpress_trace_detail")
@AllArgsConstructor
@NoArgsConstructor
@CopyFrom({TraceDetail.class})
@Data
public class TraceDetailPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_code", nullable = false)
    private String billCode;
    @Column(name = "scan_time", nullable = false)
    private LocalDateTime scanTime;

    @Column(name = "`desc`")
    private String desc;

    @Column(name = "scan_type", nullable = false)
    private String scanType;

    @Column(name = "problem_type")
    private String problemType;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "staff_contact")
    private String staffContact;

    @Column(name = "scan_network_id")
    private Long scanNetworkId;

    @Column(name = "next_network_id")
    private Long nextNetworkId;

    @Column(name = "reback_status")
    private Integer rebackStatus;

    @Column(name = "network_type")
    private String networkType;

    @Column(name = "sign_by_others_type")
    private String signByOthersType;

    @Column(name = "sign_by_others_name")
    private String signByOthersName;

    @Column(name = "sign_by_others_tel")
    private String signByOthersTel;

    @Column(name = "pick_code")
    private String pickCode;

}
