package cn.edu.xmu.oomall.ztoexpress.mapper.po;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.actuate.audit.listener.AuditListener;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "zto_express")
@EntityListeners(AuditListener.class)
public class ExpressPo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "zto_order_id")
    private Long ztoOrderId;
    @Column(name = "bill_code")
    private String billcode;
    @Column(name = "scan_Date")
    private LocalDate scandate;
    @Column(name = "scan_Site_Id")
    private Long scansiteid;
    @Column(name = "parcel_Weight")
    private Integer parcelweight;
    @Column(name = "parcel_Packing_Fee")
    private Integer parcelpackingfee;
    @Column(name = "parcel_Size")
    private Integer parcelsize;
    @Column(name = "parcel_Quantity")
    private Integer parcelquantity;
    @Column(name = "parcel_Other_Fee")
    private Integer parcelotherfee;
    @Column(name = "express_Status")
    private Integer expressStatus;//0：已取消 1：进行中 2：已完成

}