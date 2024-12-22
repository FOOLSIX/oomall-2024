package cn.edu.xmu.oomall.customer.mapper.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "order")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class OrderPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Byte status;

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
