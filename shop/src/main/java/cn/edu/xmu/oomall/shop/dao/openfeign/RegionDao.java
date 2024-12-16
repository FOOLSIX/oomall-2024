//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.shop.dao.bo.Region;
import cn.edu.xmu.oomall.shop.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.shop.mapper.openfeign.po.RegionPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RegionDao {

    private Logger logger = LoggerFactory.getLogger(RegionDao.class);
    private RegionMapper regionMapper;

    @Autowired
    public RegionDao(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    private Region build(RegionPo po) {
        Region bo = CloneFactory.copy(new Region(), po);
        bo.setRegionDao(this);
        return bo;
    }

    public Region findById(Long id) {
        InternalReturnObject<RegionPo> ret = this.regionMapper.findRegionById(id);
        return this.build(ret.getData());
    }

    public List<Region> retrieveParentRegionsById(Long regionId) {
        InternalReturnObject<List<RegionPo>> ret = this.regionMapper.retrieveParentRegionsById(regionId);
        return ret.getData().stream().map(po -> this.build(po)).collect(Collectors.toList());
    }

}
