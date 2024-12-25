package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.mapper.jpa.CartPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class CartDao {

    private final CartPoMapper cartPoMapper;

    @Autowired
    public CartDao(CartPoMapper carPoMapper) {this.cartPoMapper = carPoMapper;}

    public Cart findById(Long id) throws BusinessException{
        if (id == null) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "id is null");
        }
        Optional<CartPo> cartPo = cartPoMapper.findById(id);
        return cartPo.map(this::build).orElse(null);
    }

    public Cart findByCreatorId(Long id){
        if (id == null) {
            throw new IllegalArgumentException("findByCreatorId: id is null");
        }
        Optional<CartPo> cartPo = cartPoMapper.findByCreatorId(id);
        return cartPo.map(this::build).orElse(null);
    }

    public Cart build(CartPo cartPo) {
        Cart cart = CloneFactory.copy(new Cart(), cartPo);
        this.build(cart);
        return cart;
    }

    private Cart build(Cart bo){
        bo.setCartDao(this);
        return bo;
    }

    public void addquantity(Cart cart) {
        cart.setQuantity(cart.getQuantity() + 1);
        CartPo po = CloneFactory.copy(new CartPo(),cart);
        save(po);
    }

    public void reducequantity(Cart cart) {
        cart.setQuantity(cart.getQuantity() - 1);
        CartPo po = CloneFactory.copy(new CartPo(),cart);
        save(po);
    }
    public void save(CartPo po) {cartPoMapper.save(po);}
}
