package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 */
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

    public List<Region> getAncestors(){
        List<Region> ret=null;
        if(Objects.nonNull(this.regionDao)){
            ret=this.regionDao.retrieveParentRegionsById(this.id);
        }
        return ret;
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
