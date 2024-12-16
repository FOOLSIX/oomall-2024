//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.model.strategy.impl;


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
/**
 * @author yuhao shi
 * @date 2023-12-09
 * dgn-010-syh
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmountCouponLimitation extends BaseCouponLimitation implements Serializable {


	public AmountCouponLimitation(){
		super();
	}
	public AmountCouponLimitation(Long value) {
		super(value);
	}

	@Override
	public boolean pass(List<Item> items) {
		long t = 0;
		for (Item oi : items) {
			t += oi.getQuantity();
		}
		return t >= value;
	}
}
