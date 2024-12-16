package cn.edu.xmu.oomall.freight.mapper.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionPo {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
