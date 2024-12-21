package cn.edu.xmu.oomall.product.mapper.jpa;

import cn.edu.xmu.oomall.product.mapper.po.ShopPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalShopMapper extends JpaRepository<ShopPo,Long> {
}
