package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.CartItemDao;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, doNotUseGetters = true)
@NoArgsConstructor
@CopyFrom({CustomerPo.class, CartItemPo.class})
@Data

public class CartItem extends OOMallObject {

    @Setter
    private Long productId;
    private Long price;
    private Long quantity;
    private LocalDateTime gmtCreate;

    private CartItemDao cartItemDao;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {this.gmtModified = gmtModified;}

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", gmtCreate=" + gmtCreate +
                ", cartItemDao=" + (cartItemDao != null ? cartItemDao.getClass().getName() : "null") +
                '}';
    }
}
