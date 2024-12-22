package cn.edu.xmu.oomall.customer.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({Customer.class})
@Data
public class CustomerVo {
    private Long id;
    private String userName;
    private Byte inValid;
    private String name;
    private IdNameTypeVo creator;
    private IdNameTypeVo modifier;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public CustomerVo(Customer customer){
        super();
        CloneFactory.copy(this, customer);
        this.creator = IdNameTypeVo.builder().id(customer.getCreatorId()).name(customer.getCreatorName()).build();
        this.modifier = IdNameTypeVo.builder().id(customer.getModifierId()).name(customer.getModifierName()).build();
    }


}
