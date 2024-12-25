package cn.edu.xmu.oomall.customer.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
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
@CopyFrom({Cart.class})

public class CartPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
    /*
    购物车内商品种类个数，并非购物车商品总数
     */
    private int quantity  = 0;

}