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
 * @date 2020-11-18
 * modified by zhongyu wang
 * date 2021-11-12
 * @author yuhao shi
 * 2023-12-11
 * dgn2-010-syh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PriceCouponLimitation extends BaseCouponLimitation implements Serializable {


	public PriceCouponLimitation(Long value) {
		super(value);
	}
	public PriceCouponLimitation(){
		super();
	}

	@Override
	public boolean pass(List<Item> items) {
		long t = 0;
		for (Item oi : items) {
			t += oi.getQuantity() * oi.getPrice();
		}
		return t >= value;
	}

}
