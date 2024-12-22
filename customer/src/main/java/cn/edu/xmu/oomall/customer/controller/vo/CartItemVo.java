package cn.edu.xmu.oomall.customer.controller.vo;

import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class CartItemVo {
    @Setter
    private Long productId;
    private Long price;
    private Long quantity;
    private LocalDateTime gmtModified;

    public void setPrice(Long price) {this.price = price;}

    public void setQuantity(Long quantity) {this.quantity = quantity;}

    public void setgmtModified(LocalDateTime gmtModified) {this.gmtModified = gmtModified;}

}