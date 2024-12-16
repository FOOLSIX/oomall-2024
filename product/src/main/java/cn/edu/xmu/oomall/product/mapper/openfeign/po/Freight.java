package cn.edu.xmu.oomall.product.mapper.openfeign.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import java.util.List;
/**
 *
 * @author jyx
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Freight {
    private Long id;

    private List<Logistics> logistics;

    public void setId(Long id){ this.id = id;}

    public void setLogistics(List<Logistics> logistics) { this.logistics = logistics; }

    public List<Logistics> getLogistics(){return this.logistics;}

    public Long getId(){return this.id;}


}
