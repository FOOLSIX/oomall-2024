//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@CopyFrom({Template.class})
public class SimpleTemplateVo {
    private Long id;
    private String name;
    @JsonProperty(value = "default")
    private Boolean defaultModel;

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

    public Boolean getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(Byte defaultModel) {
        this.defaultModel = defaultModel == 0;
    }
}
