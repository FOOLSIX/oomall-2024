//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.region.controller;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.model.vo.StatusVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.region.dao.bo.Region;
import cn.edu.xmu.oomall.region.service.RegionService;
import cn.edu.xmu.oomall.region.controller.vo.RegionVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 无需登录的地区API
 */
@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class UnAuthorizedController {
    private final Logger logger = LoggerFactory.getLogger(UnAuthorizedController.class);
    private final RegionService regionService;

    /**
     * 地区状态
     * @return
     */
    @GetMapping("/regions/states")
    public ReturnObject getRegionsState() {
        List<StatusVo> dtoList = this.regionService.retrieveRegionsStates();
        return new ReturnObject(ReturnNo.OK, dtoList);
    }

    /**
     * 查询在地区下的子地区
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/regions/{id}/subregions")
    public ReturnObject retrieveSubRegionsById(@PathVariable Long id,
                                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        List<Region> regions = this.regionService.retrieveValidSubRegionsById(id, page, pageSize);
        return new ReturnObject(new PageVo<>(regions.stream().map(bo -> IdNameTypeVo.builder().id(bo.getId()).name(bo.getName()).build()).collect(Collectors.toList()), page, pageSize));
    }

    /**
     * 根据地区名获取地区
     * @param id 地区id
     * @return 地区对象
     */
    @GetMapping("/regions/{id}")
    public ReturnObject findRegionById(@PathVariable Long id) {
        Region region = this.regionService.findById(id);
        RegionVo vo = CloneFactory.copy(new RegionVo(), region);
        vo.setCreator(IdNameTypeVo.builder().id(region.getCreatorId()).name(region.getCreatorName()).build());
        vo.setModifier(IdNameTypeVo.builder().id(region.getModifierId()).name(region.getModifierName()).build());
        return new ReturnObject(vo);
    }
}
