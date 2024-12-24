package cn.edu.xmu.oomall.customer.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_cartitem")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CopyFrom({CartItem.class})

public class CartItemPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cartId;

    private Long productId;
    private Long price;
    private Long quantity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

}
