package cn.edu.xmu.oomall.jtexpress.dao.bo;



import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.jtexpress.controller.vo.CancelOrderVo;
import cn.edu.xmu.oomall.jtexpress.controller.vo.OrderVo;
import cn.edu.xmu.oomall.jtexpress.dao.OrderDao;
import cn.edu.xmu.oomall.jtexpress.exception.JTException;
import cn.edu.xmu.oomall.jtexpress.exception.ReturnError;
import cn.edu.xmu.oomall.jtexpress.mapper.po.OrderPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@CopyFrom({OrderVo.class, OrderPo.class})
public class Order {

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private OrderDao orderDao;

    @JsonIgnore
    @ToString.Exclude
    private final Logger logger = LoggerFactory.getLogger(Order.class);


    private Long id;
    private String customerCode;
    private String digest;
    private String network;
    private String txLogisticId;
    private String expressType;
    private String orderType;
    private String serviceType;
    private String deliveryType;
    private String payType;
    private Long senderId;
    private PersonInfo sender;
    private Long receiverId;
    private PersonInfo receiver;
    private LocalDateTime sendStartTime;
    private LocalDateTime sendEndTime;
    private String goodsType;
    private Boolean isRealName;
    private Boolean isCustomsDeclaration;
    private int length;
    private int width;
    private int height;
    private Double weight;
    private int totalQuantity;
    private String itemsValue;
    private String priceCurrency;
    private String offerFee;
    private String remark;
    private String postSiteCode;
    private String postSiteName;
    private String postSiteAddress;
    private String billCode;
    private int orderStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getTxLogisticId() {
        return txLogisticId;
    }

    public void setTxLogisticId(String txLogisticId) {
        this.txLogisticId = txLogisticId;
    }

    public String getExpressType() {
        return expressType;
    }

    public void setExpressType(String expressType) {
        this.expressType = expressType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @CopyFrom.Exclude({OrderPo.class, OrderVo.class})
    public PersonInfo getSender() {
        return sender;
    }

    @CopyFrom.Exclude({OrderVo.class, OrderPo.class})
    public void setSender(PersonInfo sender) {
        this.sender = sender;
    }

    @CopyFrom.Exclude({OrderPo.class, OrderVo.class})
    public PersonInfo getReceiver() {
        return receiver;
    }

    @CopyFrom.Exclude({OrderVo.class, OrderPo.class})
    public void setReceiver(PersonInfo receiver) {
        this.receiver = receiver;
    }


    public LocalDateTime getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(LocalDateTime sendStartTime) {
        this.sendStartTime = sendStartTime;
    }

    public LocalDateTime getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(LocalDateTime sendEndTime) {
        this.sendEndTime = sendEndTime;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public Boolean getRealName() {
        return isRealName;
    }

    public void setRealName(Boolean realName) {
        isRealName = realName;
    }

    public Boolean getCustomsDeclaration() {
        return isCustomsDeclaration;
    }

    public void setCustomsDeclaration(Boolean customsDeclaration) {
        isCustomsDeclaration = customsDeclaration;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getItemsValue() {
        return itemsValue;
    }

    public void setItemsValue(String itemsValue) {
        this.itemsValue = itemsValue;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getOfferFee() {
        return offerFee;
    }

    public void setOfferFee(String offerFee) {
        this.offerFee = offerFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPostSiteCode() {
        return postSiteCode;
    }

    public void setPostSiteCode(String postSiteCode) {
        this.postSiteCode = postSiteCode;
    }

    public String getPostSiteName() {
        return postSiteName;
    }

    public void setPostSiteName(String postSiteName) {
        this.postSiteName = postSiteName;
    }

    public String getPostSiteAddress() {
        return postSiteAddress;
    }

    public void setPostSiteAddress(String postSiteAddress) {
        this.postSiteAddress = postSiteAddress;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order cancelOrder() {
        //判断是否可以取消，不可以取消抛出异常
        checkStatus(this.orderStatus, Order.StatusEnum.CANCELED.getCode());
        this.orderStatus = Order.StatusEnum.CANCELED.getCode();
        return this.save();
    }

    public Order addOrder() {
        //设置订单状态为未完成
        this.orderStatus = Order.StatusEnum.UNCOMPLETED.getCode();

        //为了方便，如果商户申请的上门揽件时间早于当前时间，执行揽件，更改订单状态为已完成
        if (this.sendStartTime != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isAfter(this.sendStartTime) || currentTime.equals(this.sendStartTime)) {
                this.orderStatus = Order.StatusEnum.COMPLETED.getCode();
            }
        }

        //判断订单是否已经存在
        Order oldOrder = orderDao.findOrderByTxLogisticId(this.txLogisticId);


        //判断订单状态是否可以修改
        if (null != oldOrder) {
            //判断订单号是否非法,运单号与客户订单号不同违法
            if (null != this.billCode && !Objects.equals(this.billCode, oldOrder.billCode))
                throw new JTException(ReturnError.INVALID_WAYBILL_NO);

            //状态无法修改会抛出JTException异常
            oldOrder.checkStatus(oldOrder.orderStatus, this.orderStatus);

            //赋值id
            this.id = oldOrder.id;
            this.billCode = oldOrder.billCode;

            //赋值senderId
            this.sender.setId(oldOrder.sender.getId());
            this.senderId=oldOrder.senderId;
            //如果sender没有修改，设置alter为false
            if (oldOrder.sender.equals(this.sender)) {
                this.sender.setAlter(false);
            }

            //赋值receiverId
            this.receiver.setId(oldOrder.receiver.getId());
            this.receiverId=oldOrder.receiverId;
            //如果receiver没有修改，设置alter为false
            if (oldOrder.receiver.equals(this.receiver)) {
                this.receiver.setAlter(false);
            }

            //更新order
            return this.update();
        } else {
            //保存order
            return this.save();
        }
    }

    public enum StatusEnum {
        UNCOMPLETED(0, "未完成"),
        COMPLETED(1, "已完成"),
        CANCELED(2, "已取消");

        private final int code;
        private final String description;

        private static final Map<AbstractMap.SimpleEntry<Integer, Integer>, ReturnError> map = new HashMap<>() {
            {
                //已完成状态不可以更改
                put(new SimpleEntry<>(COMPLETED.code, UNCOMPLETED.code), ReturnError.PICKED_STATUS_NOT_MODIFIABLE);
                put(new SimpleEntry<>(COMPLETED.code, CANCELED.code), ReturnError.PICKED_STATUS_NOT_MODIFIABLE);
                put(new SimpleEntry<>(COMPLETED.code, COMPLETED.code), ReturnError.PICKED_STATUS_NOT_MODIFIABLE);

                //已取消状态不可以更改
                put(new SimpleEntry<>(CANCELED.code, UNCOMPLETED.code), ReturnError.CANCELED_STATUS_NOT_MODIFIABLE);
                put(new SimpleEntry<>(CANCELED.code, COMPLETED.code), ReturnError.CANCELED_STATUS_NOT_MODIFIABLE);
                put(new SimpleEntry<>(CANCELED.code, CANCELED.code), ReturnError.CANCELED_STATUS_NOT_MODIFIABLE);

            }
        };

        StatusEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static ReturnError getByStatus(int origin, int target) {
            return map.get(new AbstractMap.SimpleEntry<>(origin, target));
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    public void checkStatus(int origin, int target) {
        ReturnError returnError = StatusEnum.getByStatus(origin, target);
        if (null != returnError) throw new JTException(returnError);
    }

    public Order update() {
        return this.orderDao.updateOrder(this);
    }

    public Order save() {
        return this.orderDao.saveOrder(this);
    }
}
