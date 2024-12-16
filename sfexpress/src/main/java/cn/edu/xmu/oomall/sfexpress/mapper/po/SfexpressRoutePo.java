package cn.edu.xmu.oomall.sfexpress.mapper.po;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@Entity
@Table(name = "sfexpress_route", schema = "sfexpress", catalog = "")
public class SfexpressRoutePo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "accept_time")
    private Timestamp acceptTime;
    @Basic
    @Column(name = "accept_address")
    private String acceptAddress;
    @Basic
    @Column(name = "op_code")
    private String opCode;
    @Basic
    @Column(name = "remark")
    private String remark;
    @Basic
    @Column(name = "mail_no")
    private String mailNo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Timestamp acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptAddress() {
        return acceptAddress;
    }

    public void setAcceptAddress(String acceptAddress) {
        this.acceptAddress = acceptAddress;
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SfexpressRoutePo that = (SfexpressRoutePo) o;
        return id == that.id && mailNo == that.mailNo && Objects.equals(acceptTime, that.acceptTime) && Objects.equals(acceptAddress, that.acceptAddress) && Objects.equals(opCode, that.opCode) && Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, acceptTime, acceptAddress, opCode, remark, mailNo);
    }
}
