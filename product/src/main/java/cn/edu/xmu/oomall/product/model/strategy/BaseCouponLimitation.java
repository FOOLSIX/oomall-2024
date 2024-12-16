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
 * @author xincong yao
 * @date 2020-11-18
 * @author yuhao shi
 * 2023-12-11
 * dgn2-010-syh
 */
@NoArgsConstructor
@Data
@Slf4j
public abstract class BaseCouponLimitation {


	protected Long value;

	protected String className;

	public BaseCouponLimitation(Long value) {
		this.value = value;
		this.className = this.getClass().getName();
	}

	public abstract boolean pass(List<Item> items);

	public BaseCouponLimitation readValues(String jsonString, String className) throws ClassNotFoundException, JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		return (BaseCouponLimitation) mapper.readValue(jsonString, Class.forName(className));
	}

	public static Optional<BaseCouponLimitation> getInstance(String jsonString){
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		BaseCouponLimitation ret = null;
		try {
			root = mapper.readTree(jsonString);
			String className = root.get("className").asText();
			/*实例化对象*/
			ret = (BaseCouponLimitation) Class.forName(className).getConstructor().newInstance();
			ret=ret.readValues(jsonString,className);

		} catch (JsonProcessingException e) {
			log.error("getInstance: JsonProcessingException limitation = {}", jsonString);			;
		} catch (ClassNotFoundException e) {
			log.error("getInstance: ClassNotFoundException limitation = {}", jsonString);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(ret);
	}

}
