//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.model.strategy.impl;

import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponLimitation;
import cn.edu.xmu.oomall.product.model.strategy.Item;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xincong yao
 * @date 2020-11-19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PercentageCouponDiscount extends BaseCouponDiscount implements Serializable {

	public PercentageCouponDiscount(){
		super();
	}
	public PercentageCouponDiscount(Long value, BaseCouponLimitation couponLimitation) {
		super(value, couponLimitation);
	}

	@Override
	public void calcAndSetDiscount(List<Item> items) {
		for (Item oi : items) {
			oi.setDiscount(oi.getPrice() - (long) ((1-1.0*value / 100.0) * oi.getPrice()));
		}
	}
}
