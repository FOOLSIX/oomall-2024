package cn.edu.xmu.oomall.jtexpress.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.jtexpress.dao.bo.Order;
import cn.edu.xmu.oomall.jtexpress.exception.JTException;
import cn.edu.xmu.oomall.jtexpress.exception.ReturnError;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderDto {
    @NotNull(message = "非法参数")
    @Size(max = 30, message = "非法参数")
    private String customerCode;

    @NotNull(message = "非法参数")
    @Size(max = 50, message = "非法参数")
    private String digest;

    @Size(max = 30, message = "非法参数")
    private String network;

    @NotNull(message = "客户订单号不能为空或过长")
    @Size(max = 50, message = "客户订单号不能为空或过长")
    @JsonProperty("txlogisticId")
    private String txLogisticId;

    @NotNull(message = "非法参数")
    @Size(max = 30, message = "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法")
    @Pattern(regexp = "EZ|TYD", message = "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法")
    private String expressType;

    @NotNull(message = "非法参数")
    @Size(max = 11)
    @Pattern(regexp = "[1-2]", message = "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法")
    private String orderType;

    @NotNull(message = "非法参数")
    @Size(max = 30)
    @Pattern(regexp = "02|01", message = "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法")
    private String serviceType;

    @NotNull(message = "非法参数")
    @Size(max = 30)
    @Pattern(regexp = "06|05|04|03", message = "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法")
    private String deliveryType;

    @NotNull(message = "非法参数")
    @Size(max = 30)
    @Pattern(regexp = "PP_PM|CC_CASH", message = "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法")
    private String payType;

    @Valid
    @NotNull(message = "发件人信息不全")
    private PersonInfoDto sender;

    @Valid
    @NotNull(message = "收件人信息不全")
    private PersonInfoDto receiver;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sendStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sendEndTime;

    @NotNull(message = "非法参数")
    @Size(max = 30)
    @Pattern(regexp = "bm00000[1-9]", message = "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法")
    private String goodsType;

    private Boolean isRealName;

    private Boolean isCustomsDeclaration;

    private int length;

    private int width;

    private int height;

    @NotNull(message = "重量信息不合法")
    @DecimalMin(value = "0.01", message = "重量信息不合法")
    @DecimalMax(value = "30.0", message = "重量信息不合法")
    private Double weight;

    private int totalQuantity;

    @Size(max = 12)
    private String itemsValue;

    @Size(max = 32)
    private String priceCurrency;

    @Size(max = 12)
    private String offerFee;

    @Size(max = 200)
    private String remark;

    @Size(max = 20)
    private String postSiteCode;

    @Size(max = 100)
    private String postSiteName;

    @Size(max = 200)
    private String postSiteAddress;

    @Size(max = 30)
    private String billCode;

    public void setSendStartTime(String sendStartTime) {
        if(sendStartTime==null)return;
        try {
            this.sendStartTime = LocalDateTime.parse(sendStartTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            validateTimeRange();
        } catch (DateTimeParseException e) {
            throw new JTException(ReturnError.DOOR_SERVICE_TIME_INCOMPLETE);
        }
    }

    public void setSendEndTime(String sendEndTime) {
        if(sendEndTime==null)return;
        try {
            this.sendEndTime = LocalDateTime.parse(sendEndTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            validateTimeRange();
        } catch (DateTimeParseException e) {
            throw new JTException(ReturnError.DOOR_SERVICE_TIME_INCOMPLETE);
        }
    }



    @CopyFrom.Exclude({Order.class})
    public PersonInfoDto getSender() {
        return sender;
    }

    @CopyFrom.Exclude({Order.class})
    public PersonInfoDto getReceiver() {
        return receiver;
    }

    public void setSendEndTime(LocalDateTime sendEndTime) {
        this.sendEndTime = sendEndTime;
        validateTimeRange();
    }

    @AssertTrue(message = "上门服务时间信息不全")
    private boolean isValidTimeRange() {
        return sendStartTime == null || sendEndTime == null || sendStartTime.isBefore(sendEndTime);
    }

    private void validateTimeRange() {
        if (!isValidTimeRange()) {
            throw new JTException(ReturnError.DOOR_SERVICE_TIME_INCOMPLETE);
        }
    }


    public void setWeight(String weight) {
        try {
            this.weight = Double.parseDouble(weight);
        } catch (Exception e) {
            throw new JTException(ReturnError.INVALID_WEIGHT);
        }

    }

    public Boolean getRealName() {
        return isRealName;
    }

    public void setRealName(Boolean realName) {
        isRealName = realName;
    }
}
