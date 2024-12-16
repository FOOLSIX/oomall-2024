//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.mapper.openfeign.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consignee {
    private String name;
    private String mobile;
    private Long regionId;
    private String address;
}
