//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.model.strategy.impl;

import cn.edu.xmu.oomall.product.model.strategy.BaseCouponLimitation;
import cn.edu.xmu.oomall.product.model.strategy.Item;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhongyu wang 22920192204295
 * 跨品类优惠
 * @author yuhao shi
 *  2023-12-11
 *  dgn2-010-syh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CrossCategoryLimitation extends BaseCouponLimitation implements Serializable {

    public CrossCategoryLimitation(){
        super();
    }
    public CrossCategoryLimitation(Long value) {
        super(value);
    }

    @Override
    public boolean pass(List<Item> items) {
        long categoryCount = items.stream().map(Item::getCategoryId).distinct().count();
        return categoryCount >= value;
    }
}
