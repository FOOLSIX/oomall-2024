//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.shop.mapper.openfeign.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegionPo {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Byte status;
}
