package cn.edu.xmu.oomall.freight.dao.openfeign;
//School of Informatics Xiamen University, GPL-3.0 license

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Region;
import cn.edu.xmu.oomall.freight.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 */
@Repository
@RequiredArgsConstructor
public class RegionDao {

    private final RegionMapper regionMapper;

    private Region build(RegionPo po){
        Region bo = CloneFactory.copy(new Region(), po);
        bo.setRegionDao(this);
        return bo;
    }

    public Region findById(Long id){
        InternalReturnObject<RegionPo> ret = this.regionMapper.findRegionById(id);
        return this.build(ret.getData());
    }

    public List<Region> retrieveParentRegionsById(Long id){
        InternalReturnObject<List<RegionPo>> ret= this.regionMapper.retrieveParentRegionsById(id);
        return ret.getData().stream().map(this::build).collect(Collectors.toList());
    }

    public Region retrieveByName(String name){
        InternalReturnObject<RegionPo> ret=this.regionMapper.retriveRegionIdByName(name);
        return CloneFactory.copy(new Region(), ret.getData());
    }
}
