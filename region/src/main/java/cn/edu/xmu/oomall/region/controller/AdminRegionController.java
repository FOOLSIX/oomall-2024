//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.region.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.javaee.core.validation.UpdateGroup;
import cn.edu.xmu.oomall.region.controller.dto.RegionDto;
import cn.edu.xmu.oomall.region.dao.bo.Region;
import cn.edu.xmu.oomall.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 地区管理员控制器
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/platforms/{did}", produces = "application/json;charset=UTF-8")
@Transactional
@RequiredArgsConstructor
public class AdminRegionController {
    private final static Logger logger = LoggerFactory.getLogger(AdminRegionController.class);
    private final RegionService regionService;

    /**
     * 管理员查询在地区下的子地区
     * 废弃的地区也会返回
     * @param did 管理员id，PLATFORM
     * @param id 地区id
     * @param page 页码
     * @param pageSize 每页数量
     * @return 子地区对象
     */
    @GetMapping("/regions/{id}/subregions")
    @Audit(departName = "platforms")
    public ReturnObject getShopSubRegionsById(@PathVariable Long did, @PathVariable Long id,
                                              @RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "地区", id, did));
        }
        List<Region> regionList = this.regionService.retrieveSubRegionsById(id, page, pageSize);
        return new ReturnObject(new PageVo<>(regionList.stream().map(o -> IdNameTypeVo.builder().id(o.getId()).name(o.getName()).build()).collect(Collectors.toList()), page, pageSize));
    }

    /**
     * 管理员创建子地区
     * @param did 管理员id，PLATFORM
     * @param id 地区id
     * @param user 登录用户
     * @param dto 地区数据
     * @return
     */
    @PostMapping("/regions/{id}/subregions")
    @Audit(departName = "platforms")
    public ReturnObject createSubRegions(@PathVariable Long did, @PathVariable Long id, @LoginUser UserDto user,
                                         @Validated(NewGroup.class) @RequestBody RegionDto dto) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "地区", id, did));
        }
        Region region = CloneFactory.copy(new Region(), dto);
        Region newRegion = this.regionService.createSubRegions(id, region, user);
        IdNameTypeVo vo = IdNameTypeVo.builder().id(newRegion.getId()).name(newRegion.getName()).build();
        return new ReturnObject(ReturnNo.CREATED, vo);
    }

    /**
     * 管理员修改某个地区
     * 废弃地区不能修改
     * @param did 管理员id，PLATFORM
     * @param id 地区id
     * @param user 登录用户
     * @param dto 修改内容
     * @return
     */
    @PutMapping("/regions/{id}")
    @Audit(departName = "platforms")
    public ReturnObject updateRegionById(@PathVariable Long did, @PathVariable Long id, @LoginUser UserDto user,
                                         @RequestBody @Validated(UpdateGroup.class) RegionDto dto) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "地区", id, did));
        }
        Region region = CloneFactory.copy(new Region(), dto);
        region.setId(id);
        logger.debug("updateRegionById: region = {}", region);
        this.regionService.updateById(region, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 管理员废弃某个地区
     * 下级地区一并废弃
     * @param did 管理员id，PLATFORM
     * @param id 地区id
     * @param user 登录用户
     * @return
     */
    @DeleteMapping("/regions/{id}")
    @Audit(departName = "platforms")
    public ReturnObject deleteRegionById(@PathVariable Long did, @PathVariable Long id, @LoginUser UserDto user) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "地区", id, did));
        }
        this.regionService.abandonRegion(id, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 管理员停用某个地区
     * 下级地区一并停用
     * @param did 管理员id，PLATFORM
     * @param id 地区id
     * @param user 登录用户
     * @return
     */
    @PutMapping("/regions/{id}/suspend")
    @Audit(departName = "platforms")
    public ReturnObject suspendRegionById(@PathVariable Long did, @PathVariable Long id, @LoginUser UserDto user) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "地区", id, did));
        }
        this.regionService.suspendRegion(id, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 管理员恢复某个地区
     * 下级地区一并恢复
     * 上级地区需都是valid状态
     * @param did 管理员id，PLATFORM
     * @param id 地区id
     * @param user 登录用户
     * @return
     */
    @PutMapping("/regions/{id}/resume")
    @Audit(departName = "platforms")
    public ReturnObject resumeRegionById(@PathVariable Long did, @PathVariable Long id, @LoginUser UserDto user) {
        if (!PLATFORM.equals(did)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "地区", id, did));
        }
        this.regionService.resumeRegion(id, user);
        return new ReturnObject(ReturnNo.OK);
    }

}
