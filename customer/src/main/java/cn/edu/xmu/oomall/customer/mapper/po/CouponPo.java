package cn.edu.xmu.oomall.customer.mapper.po;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_coupon")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class CouponPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String coupon_sn;
    String name;
    Long customerId;
    Long activityId;
    LocalDateTime begin_time;
    LocalDateTime end_time;
    Byte used;
    LocalDateTime gmtCreate;
    LocalDateTime gmtModified;
    String  creator_name;
    Long creator_id;
    String modifier_name;
    Long modifier_id;
}
