package cn.edu.xmu.oomall.freight.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
/**
 * 2023-dgn3-009
 *
 * @author huangzian
 */
@AllArgsConstructor
@NoArgsConstructor
@CopyFrom(Express.class)
public class SimpleExpressVo {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    private Long id;
    private String billCode;

}
