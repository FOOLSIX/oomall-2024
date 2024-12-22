package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
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
public class Customer extends OOMallObject {

    /**
     * 封禁状态，1为封禁，0为正常，2为注销
     */
    private Byte invalid;
    private CustomerDao customerDao;
    private String userName;
    private String name;
    private OrderPoMapper orderPoMapper;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}
