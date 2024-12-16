//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.model.strategy.impl;

import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponLimitation;
import cn.edu.xmu.oomall.product.model.strategy.Item;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheapestPercentageDiscount extends BaseCouponDiscount implements Serializable {

	public CheapestPercentageDiscount(){
		super();
	}
	public CheapestPercentageDiscount(Long value, BaseCouponLimitation limitation) {
		super(value, limitation);
	}

	@Override
	public void calcAndSetDiscount(List<Item> items) {
		Long cheapest = Long.MAX_VALUE;
		int total = 0;
		for (int i = 0; i < items.size(); i++) {
			Item oi = items.get(i);
			total += oi.getPrice() * oi.getQuantity();
			if (oi.getPrice() < cheapest) {
				cheapest = oi.getPrice();
			}
		}

		double discount =  ((1-1.0 * value / 100) *cheapest);

		for (Item oi : items) {
			oi.setDiscount(oi.getPrice() - (long) ((1.0 * oi.getPrice() * oi.getQuantity()) / total * discount / oi.getQuantity()));
		}
	}
}
