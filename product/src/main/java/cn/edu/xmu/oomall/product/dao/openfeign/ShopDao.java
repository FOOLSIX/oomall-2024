//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.service.GrouponActService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ShopDao {
    private final ShopMapper shopMapper;

    /**
     * @modify Rui Li
     * @task 2023-dgn2-007
     */
    public Shop findById(Long id) {
        InternalReturnObject<Shop> ret = this.shopMapper.getShopById(id);
        return ret.getData();
    }
}
