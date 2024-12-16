package cn.edu.xmu.oomall.sfexpress.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.sfexpress.dao.bo.Express;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@Entity
@Table(name = "sfexpress_express", schema = "sfexpress", catalog = "")
@CopyFrom({Express.class})
public class SfexpressExpressPo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "order_id")
    private String orderId;
    @Basic
    @Column(name = "cargo_desc")
    private String cargoDesc;
    @Basic
    @Column(name = "monthly_card")
    private String monthlyCard;
    @Basic
    @Column(name = "pay_method")
    private Integer payMethod;
    @Basic
    @Column(name = "express_type_id")
    private Integer expressTypeId;
    @Basic
    @Column(name = "sender_address")
    private String senderAddress;
    @Basic
    @Column(name = "sender_city")
    private String senderCity;
    @Basic
    @Column(name = "sender_contact")
    private String senderContact;
    @Basic
    @Column(name = "sender_mobile")
    private String senderMobile;
    @Basic
    @Column(name = "diliver_address")
    private String diliverAddress;
    @Basic
    @Column(name = "diliver_city")
    private String diliverCity;
    @Basic
    @Column(name = "diliver_contact")
    private String diliverContact;
    @Basic
    @Column(name = "diliver_mobile")
    private String diliverMobile;
    @Basic
    @Column(name = "country")
    private String country;
    @Basic
    @Column(name = "status")
    private int status;
    @Basic
    @Column(name = "total_height")
    private Double totalHeight;
    @Basic
    @Column(name = "total_length")
    private Double totalLength;
    @Basic
    @Column(name = "total_volume")
    private Double totalVolume;
    @Basic
    @Column(name = "total_weight")
    private Double totalWeight;
    @Basic
    @Column(name = "total_width")
    private Double totalWidth;
    @Basic
    @Column(name = "waybill_no")
    private String waybillNo;
    @Basic
    @Column(name = "waybill_type")
    private Integer waybillType;
    @Basic
    @Column(name = "send_start_tm")
    private Timestamp sendStartTm;
    @Basic
    @Column(name = "pickup_appoint_endtime")
    private Timestamp pickupAppointEndtime;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCargoDesc() {
        return cargoDesc;
    }

    public void setCargoDesc(String cargoDesc) {
        this.cargoDesc = cargoDesc;
    }

    public String getMonthlyCard() {
        return monthlyCard;
    }

    public void setMonthlyCard(String monthlyCard) {
        this.monthlyCard = monthlyCard;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getExpressTypeId() {
        return expressTypeId;
    }

    public void setExpressTypeId(Integer expressTypeId) {
        this.expressTypeId = expressTypeId;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderContact() {
        return senderContact;
    }

    public void setSenderContact(String senderContact) {
        this.senderContact = senderContact;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getDiliverAddress() {
        return diliverAddress;
    }

    public void setDiliverAddress(String diliverAddress) {
        this.diliverAddress = diliverAddress;
    }

    public String getDiliverCity() {
        return diliverCity;
    }

    public void setDiliverCity(String diliverCity) {
        this.diliverCity = diliverCity;
    }

    public String getDiliverContact() {
        return diliverContact;
    }

    public void setDiliverContact(String diliverContact) {
        this.diliverContact = diliverContact;
    }

    public String getDiliverMobile() {
        return diliverMobile;
    }

    public void setDiliverMobile(String diliverMobile) {
        this.diliverMobile = diliverMobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(Double totalHeight) {
        this.totalHeight = totalHeight;
    }

    public Double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Double totalLength) {
        this.totalLength = totalLength;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getTotalWidth() {
        return totalWidth;
    }

    public void setTotalWidth(Double totalWidth) {
        this.totalWidth = totalWidth;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public Integer getWaybillType() {
        return waybillType;
    }

    public void setWaybillType(Integer waybillType) {
        this.waybillType = waybillType;
    }

    public Timestamp getSendStartTm() {
        return sendStartTm;
    }

    public void setSendStartTm(Timestamp sendStartTm) {
        this.sendStartTm = sendStartTm;
    }

    public Timestamp getPickupAppointEndtime() {
        return pickupAppointEndtime;
    }

    public void setPickupAppointEndtime(Timestamp pickupAppointEndtime) {
        this.pickupAppointEndtime = pickupAppointEndtime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SfexpressExpressPo that = (SfexpressExpressPo) o;
        return id == that.id && status == that.status && Objects.equals(orderId, that.orderId) && Objects.equals(cargoDesc, that.cargoDesc) && Objects.equals(monthlyCard, that.monthlyCard) && Objects.equals(payMethod, that.payMethod) && Objects.equals(expressTypeId, that.expressTypeId) && Objects.equals(senderAddress, that.senderAddress) && Objects.equals(senderCity, that.senderCity) && Objects.equals(senderContact, that.senderContact) && Objects.equals(senderMobile, that.senderMobile) && Objects.equals(diliverAddress, that.diliverAddress) && Objects.equals(diliverCity, that.diliverCity) && Objects.equals(diliverContact, that.diliverContact) && Objects.equals(diliverMobile, that.diliverMobile) && Objects.equals(country, that.country) && Objects.equals(totalHeight, that.totalHeight) && Objects.equals(totalLength, that.totalLength) && Objects.equals(totalVolume, that.totalVolume) && Objects.equals(totalWeight, that.totalWeight) && Objects.equals(totalWidth, that.totalWidth) && Objects.equals(waybillNo, that.waybillNo) && Objects.equals(waybillType, that.waybillType) && Objects.equals(sendStartTm, that.sendStartTm) && Objects.equals(pickupAppointEndtime, that.pickupAppointEndtime) && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, cargoDesc, monthlyCard, payMethod, expressTypeId, senderAddress, senderCity, senderContact, senderMobile, diliverAddress, diliverCity, diliverContact, diliverMobile, country, status, totalHeight, totalLength, totalVolume, totalWeight, totalWidth, waybillNo, waybillType, sendStartTm, pickupAppointEndtime, createTime);
    }
}
