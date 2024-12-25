package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.controller.dto.CartItemDto;
import cn.edu.xmu.oomall.customer.dao.CartItemDao;

import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, doNotUseGetters = true)
@NoArgsConstructor
@CopyFrom({CustomerPo.class, CartItemPo.class, CartItemDto.class})
@Data

public class CartItem extends OOMallObject {


    @Setter
    private Long productId;
//    private int price;
//    private byte status;
    private int quantity;

    private CartItemDao cartItemDao;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }



    @Override
    public void setGmtModified(LocalDateTime gmtModified) {this.gmtModified = gmtModified;}

    public CartItem(Cart cart, UserDto userDto) {
        this.creatorId = cart.getId();
        this.setCreatorName(userDto.getName());
        this.setGmtCreate(LocalDateTime.now());
        this.setGmtModified(LocalDateTime.now());
        this.setModifier(userDto);
    }
}
