//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.mapper.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeightThresholdPo implements Serializable {

    /**
     * 低于某个值
     */
    private Integer below;

    /**
     * 价格（分）
     */
    private Long price;
}
