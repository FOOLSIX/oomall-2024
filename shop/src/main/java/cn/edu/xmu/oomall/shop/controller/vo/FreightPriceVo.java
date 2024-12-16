package cn.edu.xmu.oomall.shop.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreightPriceVo {
    private Long freightPrice;

    private List<List<ProductItemVo>> pack;
}
