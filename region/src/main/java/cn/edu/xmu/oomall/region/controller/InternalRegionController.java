package cn.edu.xmu.oomall.region.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.region.dao.bo.Region;
import cn.edu.xmu.oomall.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地区内部api控制器
 */
@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class InternalRegionController {
    private final static Logger logger = LoggerFactory.getLogger(AdminRegionController.class);
    private final RegionService regionService;


    /**
     * 查询地区的上级地区
     * @param id 地区id
     * @return
     */
    @GetMapping("/regions/{id}/parents")
    public ReturnObject getParentsRegions(@PathVariable Long id) {
        List<Region> ancestors = this.regionService.retrieveParentsRegionsById(id);
        return new ReturnObject(ancestors.stream().map(o -> IdNameTypeVo.builder().id(o.getId()).name(o.getName()).build()).collect(Collectors.toList()));
    }
}
