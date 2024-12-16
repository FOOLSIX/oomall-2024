//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.model.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;


/**
 *  @author xincong yao

 */
/**
 * @author yuhao shi
 * @date 2023-12-09
 * dgn2-010-syh
 */
@Slf4j
@NoArgsConstructor
@Data
public abstract class BaseCouponDiscount{


	protected Long value;

	protected BaseCouponLimitation couponLimitation;

	protected String className;

	public BaseCouponDiscount(Long value, BaseCouponLimitation limitation) {
		this.value = value;
		this.couponLimitation = limitation;
		this.className = this.getClass().getName();
	}


	public List<Item> compute(List<Item> items) {
		if (!this.couponLimitation.pass(items)) {
			for (Item oi : items) {
				oi.setCouponActivityId(null);
			}
			return items;
		}
		calcAndSetDiscount(items);
		return items;
	}

	public abstract void calcAndSetDiscount(List<Item> items);

	public static Optional<BaseCouponDiscount> getInstance(String jsonString){
		ObjectMapper mapper = new ObjectMapper();
		BaseCouponDiscount bc = null;
		JsonNode root = null;
		try {
			root = mapper.readTree(jsonString);

			String className = root.get("className").asText();
			bc = (BaseCouponDiscount) Class.forName(className).getConstructor().newInstance();

			String limitation = root.get("couponLimitation").toString();
			BaseCouponLimitation bl = BaseCouponLimitation.getInstance(limitation).orElse(null);
			Long value = Long.valueOf(root.get("couponLimitation").get("value").toString());
			bl.setValue(value);

			bc.setCouponLimitation(bl);
			bc.setValue(root.get("value").asLong());
			bc.setClassName(className);
		} catch (JsonProcessingException e) {
			log.error("getInstance: JsonProcessingException strategy = {}", jsonString);
		} catch (ClassNotFoundException e) {
			log.error("getInstance: ClassNotFoundException strategy = {}", jsonString);
		} catch (InvocationTargetException e) {
			log.error("getInstance: InvocationTargetException strategy = {}", jsonString);
		} catch (InstantiationException e) {
			log.error("getInstance: InstantiationException strategy = {}", jsonString);
		} catch (IllegalAccessException e) {
			log.error("getInstance: IllegalAccessException strategy = {}", jsonString);
		} catch (NoSuchMethodException e) {
			log.error("getInstance: NoSuchMethodException strategy = {}", jsonString);
		}
		return Optional.ofNullable(bc);
	}
}
