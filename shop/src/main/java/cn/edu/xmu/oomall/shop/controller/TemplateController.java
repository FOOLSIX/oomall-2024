//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.shop.controller.vo.SimpleTemplateVo;
import cn.edu.xmu.oomall.shop.controller.vo.TemplateVo;
import cn.edu.xmu.oomall.shop.controller.dto.*;
import cn.edu.xmu.oomall.shop.dao.bo.template.*;
import cn.edu.xmu.oomall.shop.service.RegionTemplateService;
import cn.edu.xmu.oomall.shop.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/shops/{shopId}/templates", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;

    private final RegionTemplateService regionTemplateService;

    /**
     * 管理员定义运费模板
     */
    @Audit(departName = "shops")
    @PostMapping("")
    public ReturnObject createTemplate(
            @PathVariable("shopId") Long shopId,
            @Validated @RequestBody TemplateDto dto,
            @LoginUser UserDto user
    ) {
        Template template = CloneFactory.copy(new Template(), dto);
        if (dto.getType().equals(TemplateDto.WEIGHT)) {
            template.setTemplateBean(Template.WEIGHT);
        } else if (dto.getType().equals(TemplateDto.PIECE)) {
            template.setTemplateBean(Template.PIECE);
        }

        Template ret = this.templateService.createTemplate(shopId, template, user);
        IdNameTypeVo vo1 = IdNameTypeVo.builder().id(ret.getId()).name(ret.getName()).build();
        return new ReturnObject(ReturnNo.CREATED, ReturnNo.CREATED.getMessage(), vo1);
    }

    /**
     * 获得商品的运费模板
     */
    @Audit(departName = "shops")
    @GetMapping("")
    public ReturnObject retrieveTemplateByName(
            @PathVariable("shopId") Long shopId,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        List<Template> templates = this.templateService.retrieveTemplateByName(shopId, name, page, pageSize);
        List<SimpleTemplateVo> voList = templates.stream().map(bo -> CloneFactory.copy(new SimpleTemplateVo(), bo)).collect(Collectors.toList());
        return new ReturnObject(new PageVo<>(voList, page, pageSize));
    }

    /**
     * 管理员克隆运费模板
     */
    @Audit(departName = "shops")
    @PostMapping("/{id}/clone")
    public ReturnObject cloneTemplate(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @LoginUser UserDto user
    ) {
        Template ret = regionTemplateService.cloneTemplate(id, shopId, user);

        IdNameTypeVo vo = IdNameTypeVo.builder().id(ret.getId()).name(ret.getName()).build();

        return new ReturnObject(ReturnNo.CREATED, ReturnNo.CREATED.getMessage(), vo);

    }

    /**
     * 获得运费模板详情
     */
    @Audit(departName = "shops")
    @GetMapping("/{id}")
    public ReturnObject findTemplateById(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id
    ) {

        TemplateVo vo = CloneFactory.copy(new TemplateVo(), templateService.findTemplateById(shopId, id));
        return new ReturnObject(vo);
    }

    /**
     * 管理员修改运费模板
     */
    @Audit(departName = "shops")
    @PutMapping("/{id}")
    public ReturnObject updateTemplateById(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @Validated @RequestBody TemplateDto vo,
            @LoginUser UserDto user
    ) {
        Template template = CloneFactory.copy(new Template(), vo);
        template.setId(id);
        templateService.updateTemplateById(shopId, template, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 删除运费模板，且同步删除与商品的关系
     */
    @Audit(departName = "shops")
    @DeleteMapping("/{id}")
    public ReturnObject deleteTemplate(
            @LoginUser UserDto user,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id
    ) {
        this.templateService.deleteTemplate(shopId, id);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 管理员定义重量模板明细
     */
    @Audit(departName = "shops")
    @PostMapping("/{id}/regions/{rid}/weighttemplate")
    public ReturnObject createWeightTemplate(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @PathVariable("rid") Long rid,
            @Validated @RequestBody WeightTemplateDto vo,
            @LoginUser UserDto user
    ) {
        WeightTemplate bo = CloneFactory.copy(new WeightTemplate(), vo);
        bo.setTemplateId(id);
        bo.setRegionId(rid);

        this.regionTemplateService.insertRegionTemplate(shopId, bo, user, Weight.class);
        return new ReturnObject(ReturnNo.CREATED);
    }

    /**
     * 管理员修改重量模板明细
     */
    @Audit(departName = "shops")
    @PutMapping("/{id}/regions/{rid}/weighttemplate")
    public ReturnObject updateWeightTemplate(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @PathVariable("rid") Long rid,
            @Validated @RequestBody WeightTemplateDto vo,
            @LoginUser UserDto user
    ) {
        WeightTemplate bo = CloneFactory.copy(new WeightTemplate(), vo);
        bo.setTemplateId(id);
        bo.setRegionId(rid);
        this.regionTemplateService.saveRegionTemplate(shopId, bo, user, Weight.class);
        return new ReturnObject();
    }

    /**
     * 管理员删除地区模板
     */
    @Audit(departName = "shops")
    @DeleteMapping("/{id}/regions/{rid}")
    public ReturnObject deleteRegionTemplate(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @PathVariable("rid") Long rid
    ) {
        regionTemplateService.deleteRegionTemplate(shopId, id, rid);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 管理员定义件数模板明细
     */
    @Audit(departName = "shops")
    @PostMapping("/{id}/regions/{rid}/piecetemplates")
    public ReturnObject createPieceTemplate(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @PathVariable("rid") Long rid,
            @Validated @RequestBody PieceTemplateDto vo,
            @LoginUser UserDto user
    ) {
        PieceTemplate bo = CloneFactory.copy(new PieceTemplate(), vo);
        bo.setTemplateId(id);
        bo.setRegionId(rid);
        this.regionTemplateService.insertRegionTemplate(shopId, bo, user, Piece.class);
        return new ReturnObject(ReturnNo.CREATED);
    }

    /**
     * 管理员修改件数模板
     */
    @Audit(departName = "shops")
    @PutMapping("/{id}/regions/{rid}/piecetemplates")
    public ReturnObject updatePieceTemplate(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @PathVariable("rid") Long rid,
            @Validated @RequestBody PieceTemplateDto dto,
            @LoginUser UserDto user
    ) {
        PieceTemplate bo = CloneFactory.copy(new PieceTemplate(), dto);
        bo.setTemplateId(id);
        bo.setRegionId(rid);
        this.regionTemplateService.saveRegionTemplate(shopId, bo, user, Piece.class);
        return new ReturnObject();
    }

    /**
     * 店家或管理员查询运费模板明细
     */
    @Audit(departName = "shops")
    @GetMapping("/{id}/regions")
    public ReturnObject retrieveRegionTemplateById(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        /*
         * RegionTemplateVo是PieceTemplateVo和WeightTemplateVo的父类
         * 使用泛型自动映射
         * */
        List<RegionTemplate> ret = this.regionTemplateService.retrieveRegionTemplateById(shopId, id, page, pageSize);
        return new ReturnObject(new PageVo<>(ret, page, pageSize));
    }

    @Audit(departName = "shops")
    @PostMapping("/{id}/regions/{sid}/clone/regions/{rid}")
    public ReturnObject cloneRegionTemplate(
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @PathVariable("sid") Long sourceRegionId,
            @PathVariable("rid") Long targetRegionId,
            @LoginUser UserDto user
    ) {
        /*
         * RegionTemplateVo是PieceTemplateVo和WeightTemplateVo的父类
         * 使用泛型自动映射
         * */
        this.regionTemplateService.cloneRegionTemplate(shopId, id, sourceRegionId, targetRegionId,user);
        return new ReturnObject();
    }

}
