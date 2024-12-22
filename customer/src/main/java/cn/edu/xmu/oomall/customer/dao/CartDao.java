package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Cart;
import cn.edu.xmu.oomall.customer.mapper.jpa.CartPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class CartDao {

    private final RedisUtil redisUtil;

    private final CartPoMapper cartPoMapper;

    @Autowired
    public CartDao(RedisUtil redisUtil, CartPoMapper carPoMapper) {
        this.redisUtil = redisUtil;
        this.cartPoMapper = carPoMapper;
    }

    public Cart findById(Long id){
        if (id.equals(null)) {
            throw new IllegalArgumentException("findById: id is null");
        }
        Optional<CartPo> cartPo = cartPoMapper.findByCreatorId(id);
        Cart cart = cartPo.map(po -> CloneFactory.copy(new Cart(), po)).orElse(null);
        return cart;
    }

}
