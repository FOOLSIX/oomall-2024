package cn.edu.xmu.oomall.customer.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.controller.vo.CartItemVo;
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
@CopyFrom({CartItem.class, CartItemVo.class})

public class CartItemPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
    /* 对应购物车的id
     */
    private Long creatorId;

    private Long productId;
    private int price;
    private int quantity;

    private String creatorName;
    private Long modifierId;
    private String modifierName;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

}
