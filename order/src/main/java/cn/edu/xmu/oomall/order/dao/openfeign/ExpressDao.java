package cn.edu.xmu.oomall.order.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.mapper.openfeign.ExpressMapper;
import cn.edu.xmu.oomall.order.mapper.openfeign.po.Express;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static cn.edu.xmu.javaee.core.model.ReturnNo.INTERNAL_SERVER_ERR;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ExpressDao {

    private ExpressMapper expressMapper;

    private Express build(Express express){
        express.setExpressMapper(this.expressMapper);
        return express;
    }

    @Autowired
    public ExpressDao(ExpressMapper expressMapper) {
        this.expressMapper = expressMapper;
    }

    public Express findById(Long id) {
        InternalReturnObject<Express> expressInternalReturnObject = expressMapper.findById(id);
        if (!expressInternalReturnObject.getErrno().equals(200)){
            throw new BusinessException(INTERNAL_SERVER_ERR,"openfeign调用失败");
        }else {
            return build(expressInternalReturnObject.getData());
        }
    }

    public Long createExpress(Order order) {
        InternalReturnObject<Long> result = expressMapper.create(order);
        if (!result.getErrno().equals(200)) {
            throw new BusinessException(INTERNAL_SERVER_ERR, "openfeign调用失败");
        } else {
            return result.getData();
        }
    }



}
