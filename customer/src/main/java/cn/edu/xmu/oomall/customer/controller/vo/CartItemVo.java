package cn.edu.xmu.oomall.customer.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import lombok.Setter;

import java.time.LocalDateTime;

@CopyFrom({CartItem.class, CartItemPo.class})

public class CartItemVo {
    @Setter
    private Long productId;
    @Setter
    private Long price;
    @Setter
    private Long quantity;

    private LocalDateTime gmtModified;

    public CartItemVo(CartItem cartItem) {
        super();
        CloneFactory.copy(this,cartItem);
        this.gmtModified = LocalDateTime.now();
    }
}