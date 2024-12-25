package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@CopyFrom({CustomerPo.class, CartPo.class, CartItem.class})
@Data

public class Cart extends OOMallObject {
    private  int quantity;

    private CartDao cartDao;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setQuantity(int quantity) { this.quantity = quantity;}
}
