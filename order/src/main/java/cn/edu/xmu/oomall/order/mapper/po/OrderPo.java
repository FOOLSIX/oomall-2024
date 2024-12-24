//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CopyFrom({Order.class})
public class OrderPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 创建者id
     */
    private Long creatorId;

    /**
     * 创建者
     */
    private String creatorName;

    /**
     * 修改者id
     */
    private Long modifierId;

    /**
     * 修改者
     */
    private String modifierName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    private Byte status;

    private Long customerId;

    private Long shopId;

    private String orderSn;

    private Long pid;

    private String consignee;

    private Long regionId;

    private String address;

    private String mobile;

    private String message;

    private Long activityId;

    private Long packageId;

    /**
     * 无效态0
     * 待接受1
     * 待发货2
     * 已发货3
     * 已完成4
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte INVALID = 0;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte PENDING_ACCEPTANCE = 1;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte PENDING_DISPATCH = 2;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte DISPATCHED = 3;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte COMPLETED = 4;

}
