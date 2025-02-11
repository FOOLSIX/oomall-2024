//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.shop.mapper.openfeign.po.RegionPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.rocketmq.common.filter.impl.Op;

import java.util.List;
import java.util.Optional;

@ToString(doNotUseGetters = true)
@NoArgsConstructor
@Data
@CopyFrom({RegionPo.class})
public class Region {

    /**
     * 共三种状态
     */
    //有效
    @ToString.Exclude
    @JsonIgnore
    public static final Byte VALID = 0;
    //停用
    @ToString.Exclude
    @JsonIgnore
    public static final Byte SUSPENDED = 1;
    //废弃
    @ToString.Exclude
    @JsonIgnore
    public static final Byte ABANDONED = 2;

    private Long id;

    /**
     * 名称
     */
    private String name;

    private Byte status;

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
}
