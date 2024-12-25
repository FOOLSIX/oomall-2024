package cn.edu.xmu.oomall.customer.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.CartItem;
import cn.edu.xmu.oomall.customer.mapper.po.CartItemPo;

import java.time.LocalDateTime;

@CopyFrom({CartItem.class, CartItemPo.class})

public class CartItemVo {

    private Long id;

    private Long creatorId;;
    private Long productId;

    private int price;
    private int quantity;

    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    private IdNameTypeVo creator;
    private IdNameTypeVo modifier;
    private IdNameTypeVo product;


    public CartItemVo(CartItem cartItem) {
        super();
        CloneFactory.copy(this,cartItem);
        this.setCreator(IdNameTypeVo.builder().id(cartItem.getCreatorId()).name(cartItem.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(cartItem.getModifierId()).name(cartItem.getModifierName()).build());
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public Long getCreatorId(){return creatorId;}
    public void setCreatorId(Long productId){this.creatorId = creator.getId();}

    public Long getProductId() {return productId;}
    public void setProductId(Long productId) {this.productId = product.getId();}

    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}

    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public LocalDateTime getGmtModified(){return gmtModified;}
    public void setGmtModified(LocalDateTime gmtModified){this.gmtModified = gmtModified;}

    public LocalDateTime getGmtCreate(){return gmtCreate;}
    public void setGmtCreate(LocalDateTime gmtCreate){this.gmtCreate = gmtCreate;}

    public void setCreator(IdNameTypeVo creator) {this.creator = creator;}
    public IdNameTypeVo getCreator() {return this.creator;}

    public void setProduct(IdNameTypeVo product) {this.product = product;}
    public IdNameTypeVo getProduct() {return this.product;}

    public void setModifier(IdNameTypeVo modifier) {
        this.modifier = modifier;
    }
    public IdNameTypeVo getModifier(IdNameTypeVo modifier) {return this.modifier;}
}