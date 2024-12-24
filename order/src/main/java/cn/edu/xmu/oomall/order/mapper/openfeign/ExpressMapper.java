package cn.edu.xmu.oomall.order.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.mapper.openfeign.po.Express;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("freight-service")
public interface ExpressMapper {

    /**
     * 获取运单信息
     * @param id
     * @return
     */
    @GetMapping("/packages/{id}")
    InternalReturnObject<Express> findById(@PathVariable Long id);

    /**
     * 取消运单，由于Freight还没有相关API，这里先放空
     * @param express
     * @return
     */
    InternalReturnObject<Void> save(Express express,UserDto userDto);

    /**
     * 创建订单，由于Freight还没有相关API，这里先放空
     * @param order
     * @return
     */
    InternalReturnObject<Long> create(Order order, UserDto userDto);

}
