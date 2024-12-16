//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.freight.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author 张宁坚
 * @Task 2023-dgn3-005
 */
@FeignClient(name = "region-service")
public interface RegionMapper {

    @GetMapping("/regions/{id}")
    InternalReturnObject<RegionPo> findRegionById(@PathVariable Long id);

    @GetMapping("/internal/regions/{id}/parents")
    InternalReturnObject<List<RegionPo>> retrieveParentRegionsById(@PathVariable Long id);

    @GetMapping("/regions/{name}")
    InternalReturnObject<RegionPo> retriveRegionIdByName(@PathVariable String name);
}
