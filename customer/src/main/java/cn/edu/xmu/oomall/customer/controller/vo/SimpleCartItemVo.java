package cn.edu.xmu.oomall.customer.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({CartItem.class})
@Data

public class SimpleCartItemVo {
    private Long id;
    private Long productid;
    private int quantity;
    private int price = 0;

    public SimpleCartItemVo(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productid = cartItem.getProductId();
        this.quantity = cartItem.getQuantity();

    }

}
