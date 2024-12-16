package cn.edu.xmu.oomall.sfexpress.controller.vo;

import cn.edu.xmu.oomall.sfexpress.controller.dto.CargoDetailsDTO;
import cn.edu.xmu.oomall.sfexpress.controller.dto.ContactInfoListDTO;
import cn.edu.xmu.oomall.sfexpress.controller.dto.CustomsInfoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建订单的响应参数中的msgData字段对应的对象
 *
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class PostCreateOrderRetVo {

    @JsonProperty("language")
    private String language;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("customsInfo")
    private CustomsInfoDTO customsInfo;
    @JsonProperty("cargoDetails")
    private List<CargoDetailsDTO> cargoDetails;
    @JsonProperty("cargoDesc")
    private String cargoDesc;
    @JsonProperty("contactInfoList")
    private List<ContactInfoListDTO> contactInfoList;
    @JsonProperty("monthlyCard")
    private String monthlyCard;
    @JsonProperty("payMethod")
    private Integer payMethod;
    @JsonProperty("expressTypeId")
    private Integer expressTypeId;
    @JsonProperty("parcelQty")
    private Integer parcelQty;
    @JsonProperty("totalLength")
    private Integer totalLength;
    @JsonProperty("totalWidth")
    private Integer totalWidth;
    @JsonProperty("totalHeight")
    private Integer totalHeight;
    @JsonProperty("volume")
    private Integer volume;
    @JsonProperty("totalWeight")
    private Double totalWeight;
    @JsonProperty("totalNetWeight")
    private String totalNetWeight;
    @JsonProperty("sendStartTm")
    private String sendStartTm;
    @JsonProperty("isDocall")
    private Integer isDocall;
    @JsonProperty("isSignBack")
    private Integer isSignBack;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public CustomsInfoDTO getCustomsInfo() {
        return customsInfo;
    }

    public void setCustomsInfo(CustomsInfoDTO customsInfo) {
        this.customsInfo = customsInfo;
    }

    public List<CargoDetailsDTO> getCargoDetails() {
        return cargoDetails;
    }

    public void setCargoDetails(List<CargoDetailsDTO> cargoDetails) {
        this.cargoDetails = cargoDetails;
    }

    public String getCargoDesc() {
        return cargoDesc;
    }

    public void setCargoDesc(String cargoDesc) {
        this.cargoDesc = cargoDesc;
    }

    public List<ContactInfoListDTO> getContactInfoList() {
        return contactInfoList;
    }

    public void setContactInfoList(List<ContactInfoListDTO> contactInfoList) {
        this.contactInfoList = contactInfoList;
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

    public Integer getParcelQty() {
        return parcelQty;
    }

    public void setParcelQty(Integer parcelQty) {
        this.parcelQty = parcelQty;
    }

    public Integer getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Integer totalLength) {
        this.totalLength = totalLength;
    }

    public Integer getTotalWidth() {
        return totalWidth;
    }

    public void setTotalWidth(Integer totalWidth) {
        this.totalWidth = totalWidth;
    }

    public Integer getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(Integer totalHeight) {
        this.totalHeight = totalHeight;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getTotalNetWeight() {
        return totalNetWeight;
    }

    public void setTotalNetWeight(String totalNetWeight) {
        this.totalNetWeight = totalNetWeight;
    }

    public String getSendStartTm() {
        return sendStartTm;
    }

    public void setSendStartTm(String sendStartTm) {
        this.sendStartTm = sendStartTm;
    }

    public Integer getIsDocall() {
        return isDocall;
    }

    public void setIsDocall(Integer isDocall) {
        this.isDocall = isDocall;
    }

    public Integer getIsSignBack() {
        return isSignBack;
    }

    public void setIsSignBack(Integer isSignBack) {
        this.isSignBack = isSignBack;
    }
}
