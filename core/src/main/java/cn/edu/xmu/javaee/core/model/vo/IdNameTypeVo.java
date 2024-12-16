//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.javaee.core.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于返回id, name和type的视图对象
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdNameTypeVo {
    private Long id;
    private String name;
    private Byte type;
}