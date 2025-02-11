package cn.edu.xmu.oomall.freight.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 2023-dgn3-009
 *
 * @author huangzian
 */
@NoArgsConstructor
@Data
@CopyTo(Express.class)
public class ExpressDto {

    @NotNull(message = "商铺渠道Id不能为空")
    private Long shopLogisticId;

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    private String goodsType;

    private Long weight;

    private Integer payMethod;

    public Long getShopLogisticId() {
        return shopLogisticId;
    }

    public void setShopLogisticId(Long shopLogisticId) {
        this.shopLogisticId = shopLogisticId;
    }

    @NoArgsConstructor
    public class ContactsInfo
    {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Long getRegionId() {
            return regionId;
        }

        public void setRegionId(Long regionId) {
            this.regionId = regionId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @JsonProperty(value = "name")
        @NotBlank(message = "联系人姓名不能为空")
        private String name;
        @JsonProperty(value = "mobile")
        @NotBlank(message = "联系人电话不能为空")
        private String mobile;
        @JsonProperty(value = "regionId")
        @NotNull(message = "联系人的地区Id不能为空")
        private Long regionId;
        @JsonProperty(value = "address")
        @NotBlank(message = "联系人的地址不能为空")
        private String address;
    }

    public ContactsInfo getSender() {
        return sender;
    }

    public void setSender(ContactsInfo sender) {
        this.sender = sender;
    }

    public ContactsInfo getDelivery() {
        return delivery;
    }

    public void setDelivery(ContactsInfo delivery) {
        this.delivery = delivery;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    @JsonProperty(value = "sender")
    @NotNull(message = "寄件人信息不能为空")
    private ContactsInfo sender;
    @JsonProperty(value = "delivery")
    @NotNull(message = "收件人信息不能为空")
    private ContactsInfo delivery;
}
