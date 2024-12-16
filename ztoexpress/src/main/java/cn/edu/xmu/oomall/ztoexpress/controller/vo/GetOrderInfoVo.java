package cn.edu.xmu.oomall.ztoexpress.controller.vo;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.OrderBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderInfoVo {
    private OrderBo orderBo;

}
