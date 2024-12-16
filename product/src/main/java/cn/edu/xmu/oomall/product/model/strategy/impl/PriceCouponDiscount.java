//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.model.strategy.impl;

import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponLimitation;
import cn.edu.xmu.oomall.product.model.strategy.Item;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static java.lang.Math.max;

/**
 * @author xincong yao
 * @date 2020-11-18
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PriceCouponDiscount extends BaseCouponDiscount implements Serializable {
	public PriceCouponDiscount(){
		super();
	}
	public PriceCouponDiscount(Long value, BaseCouponLimitation couponLimitation) {
		super(value, couponLimitation);
	}

	@Override
	public void calcAndSetDiscount(List<Item> items) {
		long total = 0L;
		for (Item oi : items) {
			total += oi.getPrice() * oi.getQuantity();
		}

		for (Item oi : items) {
			long discount = oi.getPrice() - (long) ((1.0 * oi.getQuantity() * oi.getPrice() / total) * value / oi.getQuantity());
			discount = max(0, discount);
			oi.setDiscount(discount);
		}
	}
}
