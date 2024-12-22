package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.dao.CartDao;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.mapper.jpa.OrderPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@ToString(callSuper = true, doNotUseGetters = true)
@NoArgsConstructor
@CopyFrom({CustomerPo.class})
@Data

public class Cart extends OOMallObject {
    private  long quantity;

    private CartDao cartDao;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setquantity(Long quantity) { this.quantity = quantity;}
}
