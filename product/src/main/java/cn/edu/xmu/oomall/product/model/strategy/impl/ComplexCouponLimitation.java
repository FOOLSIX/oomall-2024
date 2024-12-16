//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.model.strategy.impl;

import cn.edu.xmu.oomall.product.model.strategy.BaseCouponLimitation;
import cn.edu.xmu.oomall.product.model.strategy.Item;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhongyu wang 22920192204295
 * 复合优惠限制类
 * @author yuhao shi
 *  *  2023-12-11
 *  *  dgn2-010-syh
 *  */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data



public class ComplexCouponLimitation extends BaseCouponLimitation implements Serializable {
    private List<BaseCouponLimitation> couponLimitationList;
    public ComplexCouponLimitation(){
        super();
    }
    public ComplexCouponLimitation(BaseCouponLimitation... couponLimitations) {
        super(0L);
        couponLimitationList = new ArrayList<>(couponLimitations.length);
        couponLimitationList.addAll(Arrays.asList(couponLimitations));
    }


    @Override
    public BaseCouponLimitation readValues(String jsonString,String className) throws ClassNotFoundException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonString);
        ComplexCouponLimitation complexCouponLimitation = new ComplexCouponLimitation();
        List<BaseCouponLimitation> couponLimitationList = new ArrayList<>();

        JsonNode couponLimitationNodeList = root.get("couponLimitationList");
        if (couponLimitationNodeList != null) {
            for (JsonNode couponLimitationNode : couponLimitationNodeList) {
                String limitationString = couponLimitationNode.toString();
                BaseCouponLimitation bl = BaseCouponLimitation.getInstance(limitationString).orElse(null);
                couponLimitationList.add(bl);
            }
        }
        complexCouponLimitation.setCouponLimitationList(couponLimitationList);
        return complexCouponLimitation;
    }

    @Override
    public boolean pass(List<Item> items) {
        for(BaseCouponLimitation couponLimitation : couponLimitationList) {
            if (!couponLimitation.pass(items)) {
                return false;
            }
        }
        return true;
    }
}
