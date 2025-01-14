package cn.edu.xmu.oomall.customer.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({Customer.class})
@Data
public class SimpleCustomerVo {
    private Long id;
    private String name;
    private int price;

    public SimpleCustomerVo(Customer customer){
        super();
        CloneFactory.copy(this, customer);
    }
}
