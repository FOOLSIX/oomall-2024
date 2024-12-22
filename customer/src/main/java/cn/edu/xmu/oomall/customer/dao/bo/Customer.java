package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.controller.vo.SimpleCustomerVo;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({CustomerPo.class, SimpleCustomerVo.class})
@Slf4j
@NoArgsConstructor
public class Customer extends OOMallObject {

    @Setter
    private Byte invalid;
    private String userName;
    private String name;

    // TODO 解决空指针问题
    @Setter
    @ToString.Exclude
    @JsonIgnore
    private CustomerDao customerDao;

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }


    /**
     * 禁用顾客，职责由Customer对象承担
     * @param user
     */
    public void banUser(UserDto user){
        if (this.invalid==0){
            this.changeInvalid((byte) 1,user);
        }
        else
        {
            //抛出异常
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "顾客", this.getId(), user.getDepartId()));
        }
    }

    /**
     * 解禁顾客，职责由Customer对象承担
     * @param user
     */
    public void releaseUser(UserDto user){
        if (this.invalid==1){
            this.changeInvalid((byte) 0,user);
        }
        else
        {
            //抛出异常
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "顾客", this.getId(), user.getDepartId()));
        }
    }

    /**
     * 逻辑删除顾客
     * @param user
     */
    public void delUserById(UserDto user){
        if (this.invalid==0){
            this.changeInvalid((byte) 2,user);
        }
        else
        {
            //抛出异常
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "顾客", this.getId(), user.getDepartId()));
        }
    }

    /**
     * 变更顾客状态
     * @param invalid
     * @param user
     */
    private void changeInvalid(byte invalid,UserDto user){
        Customer customer = new Customer();
        customer.setInvalid(invalid);
        customer.setId(this.id);
        this.customerDao.save(customer, user);
    }

}
