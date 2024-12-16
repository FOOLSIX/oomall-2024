//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.shop.mapper.openfeign.po.RegionPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.rocketmq.common.filter.impl.Op;

import java.util.List;
import java.util.Optional;

@ToString(doNotUseGetters = true)
@NoArgsConstructor
@CopyFrom({RegionPo.class})
public class Region {
    private Long id;

    /**
     * 名称
     */
    private String name;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private RegionDao regionDao;

    public Optional<List<Region>> getAncestors(){
        List<Region> ret = null;
        if (!this.regionDao.equals(null)){
            ret = this.regionDao.retrieveParentRegionsById(this.id);
        }
        return Optional.ofNullable(ret);
    }

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
