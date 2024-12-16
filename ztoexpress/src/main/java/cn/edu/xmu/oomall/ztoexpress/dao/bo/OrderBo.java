package cn.edu.xmu.oomall.ztoexpress.dao.bo;

import cn.edu.xmu.oomall.ztoexpress.mapper.po.ExpressPo;
import cn.edu.xmu.oomall.ztoexpress.mapper.po.OrderPo;
import cn.edu.xmu.oomall.ztoexpress.mapper.po.PersoninfoPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;


@Data
@AllArgsConstructor
@Repository
public class OrderBo {
    private ExpressPo expressPo;
    private OrderPo orderPo;
    private PersoninfoPo senderPo;
    private PersoninfoPo receiverPo;
    private String cancelType;
    public OrderBo(){
        this.orderPo = new OrderPo();
        this.senderPo = new PersoninfoPo();
        this.receiverPo = new PersoninfoPo();
        this.expressPo = new ExpressPo();
    }
}