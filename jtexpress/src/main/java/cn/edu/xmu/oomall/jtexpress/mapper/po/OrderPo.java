package cn.edu.xmu.oomall.jtexpress.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.jtexpress.dao.bo.Order;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "jtexpress_order")
@CopyFrom({Order.class})
public class OrderPo {

    // Getter 方法
    /*
     * 主键 ID，自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * 合作网点编码（没有则不传）
     */
    @Column(name = "network", length = 30)
    private String network;

    /*
     * 客户订单号（传客户自己系统的订单号）
     */
    @Column(name = "tx_logistic_id", length = 50)
    private String txLogisticId;

    /*
     * 快件类型：
     * EZ(标准快递)
     * TYD(兔优达)
     */
    @Column(name = "express_type", length = 30)
    private String expressType;

    /*
     * 订单类型（有客户编号为月结）1、 散客；2、月结；
     */
    @Column(name = "order_type", length = 11)
    private String orderType;

    /*
     * 服务类型：02 门店寄件 ； 01 上门取件
     */
    @Column(name = "service_type", length = 30)
    private String serviceType;

    /*
     * 派送类型： 06 代收点自提 05 快递柜自提 04 站点自提 03 派送上门
     */
    @Column(name = "delivery_type", length = 30)
    private String deliveryType;

    /*
     * 支付方式：PP_PM("寄付月结"), CC_CASH("到付现结");
     */
    @Column(name = "pay_type", length = 30)
    private String payType;

    /*
     * 寄件信息对象
     */
    @Column(name = "sender_id")
    private Long senderId;

    /*
     * 收件信息对象
     */
    @Column(name = "receiver_id")
    private Long receiverId;

    /*
     * 物流公司上门取货开始时间 yyyy-MM-dd HH:mm:ss
     */
    @Column(name = "send_start_time", length = 30)
    private LocalDateTime sendStartTime;

    /*
     * 客户物流公司上门取货结束时间 yyyy-MM-dd HH:mm:ss
     */
    @Column(name = "send_end_time", length = 30)
    private LocalDateTime sendEndTime;

    /*
     * 物品类型（对应订单主表物品类型）
     */
    @Column(name = "goods_type", length = 30)
    private String goodsType;

    /*
     * 是否实名
     */
    @Column(name = "is_real_name")
    private Boolean isRealName;

    /*
     * 是否申报海关
     */
    @Column(name = "is_customs_declaration")
    private Boolean isCustomsDeclaration;

    /*
     * 长，cm编号
     */
    @Column(name = "length")
    private int length;

    /*
     * 宽，cm
     */
    @Column(name = "width")
    private int width;

    /*
     * 高，cm
     */
    @Column(name = "height")
    private int height;

    /*
     * 重量，单位kg，范围0.01-30，默认0.02
     */
    @Column(name = "weight", length = 12)
    private String weight;

    /*
     * 包裹总票数(必须为1)
     */
    @Column(name = "total_quantity")
    private int totalQuantity;

    /*
     * 代收货款金额 (数值型)
     */
    @Column(name = "items_value", length = 12)
    private String itemsValue;

    /*
     * 代收货款币别（默认本国币别，如：RMB）
     */
    @Column(name = "price_currency", length = 32)
    private String priceCurrency;

    /*
     * 保价金额(数值型)，单位：元
     */
    @Column(name = "offer_fee", length = 12)
    private String offerFee;

    /*
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;


    /*
     * 驿站编码
     */
    @Column(name = "post_site_code", length = 20)
    private String postSiteCode;

    /*
     * 驿站名称
     */
    @Column(name = "post_site_name", length = 100)
    private String postSiteName;

    /*
     * 驿站地址
     */
    @Column(name = "post_site_address", length = 200)
    private String postSiteAddress;

    /**
     * 运单号
     */
    @Column(name = "bill_code",length = 50)
    private String billCode;

    /**
     * 订单状态
     */
    @Column(name = "order_status")
    private int orderStatus;

    /**
     * 顾客code
     */
    @Column(name = "customer_code",length = 30)
    private String customerCode;



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

    public Double getWeight() {
        return Double.parseDouble(weight);
    }

    public void setWeight(Double weight) {
        this.weight = String.format("%.2f", weight);
    }


}
