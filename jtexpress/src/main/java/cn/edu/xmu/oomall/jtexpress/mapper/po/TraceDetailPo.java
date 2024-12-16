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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public LocalDateTime getScanTime() {
        return scanTime;
    }

    public void setScanTime(LocalDateTime scanTime) {
        this.scanTime = scanTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffContact() {
        return staffContact;
    }

    public void setStaffContact(String staffContact) {
        this.staffContact = staffContact;
    }

    public Long getScanNetworkId() {
        return scanNetworkId;
    }

    public void setScanNetworkId(Long scanNetworkId) {
        this.scanNetworkId = scanNetworkId;
    }

    public Long getNextNetworkId() {
        return nextNetworkId;
    }

    public void setNextNetworkId(Long nextNetworkId) {
        this.nextNetworkId = nextNetworkId;
    }

    public Integer getRebackStatus() {
        return rebackStatus;
    }

    public void setRebackStatus(Integer rebackStatus) {
        this.rebackStatus = rebackStatus;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getSignByOthersType() {
        return signByOthersType;
    }

    public void setSignByOthersType(String signByOthersType) {
        this.signByOthersType = signByOthersType;
    }

    public String getSignByOthersName() {
        return signByOthersName;
    }

    public void setSignByOthersName(String signByOthersName) {
        this.signByOthersName = signByOthersName;
    }

    public String getSignByOthersTel() {
        return signByOthersTel;
    }

    public void setSignByOthersTel(String signByOthersTel) {
        this.signByOthersTel = signByOthersTel;
    }

    public String getPickCode() {
        return pickCode;
    }

    public void setPickCode(String pickCode) {
        this.pickCode = pickCode;
    }
}
