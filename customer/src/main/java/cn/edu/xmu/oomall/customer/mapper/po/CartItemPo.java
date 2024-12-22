package cn.edu.xmu.oomall.customer.mapper.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_cart")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
