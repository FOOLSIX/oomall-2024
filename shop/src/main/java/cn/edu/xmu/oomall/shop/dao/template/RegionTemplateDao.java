//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.template;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.shop.dao.TemplateDao;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.shop.dao.bo.divide.DivideStrategy;
import cn.edu.xmu.oomall.shop.dao.bo.divide.PackAlgorithm;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.mapper.RegionTemplatePoMapper;
import cn.edu.xmu.oomall.shop.mapper.TemplatePoMapper;
import cn.edu.xmu.oomall.shop.mapper.po.RegionTemplatePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;
import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;

/**
 * 运费模板的dao
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class RegionTemplateDao {
    private static final String KEY = "R%dT%d";

    @Value("${oomall.shop.region-template.timeout}")
    private Long timeout;

    private final RegionTemplatePoMapper regionTemplatePoMapper;
    private final TemplatePoMapper templatePoMapper;
    private final TemplateDao templateDao;
    private final ApplicationContext context;
    private final RegionDao regionDao;
    private final RedisUtil redisUtil;

    /**
     * 返回Bean对象
     *
     * @param template
     * @return
     * @author Ming Qiu
     * <p>
     * date: 2022-11-22 16:11
     */
    private TemplateDaoInf findTemplateBean(Template template) {
        return (TemplateDaoInf) context.getBean(template.getTemplateBean());
    }

    /**
     * 方便测试将build方法的类型改为public
     * 设置RegionTemplate的分包策略和打包属性
     *
     * @param template
     * @param po
     * @param redisKey
     * @return
     * @author ZhaoDong Wang
     * 2023-dgn1-009
     */
    public RegionTemplate build(Template template, RegionTemplatePo po, Optional<String> redisKey) {
        TemplateDaoInf dao = this.findTemplateBean(template);
        RegionTemplate bo = dao.getRegionTemplate(po);
        this.build(bo);
        log.debug("getBo: bo = {}", bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    public RegionTemplate build(RegionTemplate bo) {
        bo.setRegionDao(this.regionDao);
        bo.setTemplateDao(this.templateDao);
        return bo;
    }

    /**
     * 根据关键字找到运费模板
     *
     * @param id
     * @return
     * @throws RuntimeException
     * @author Ming Qiu
     * <p>
     * date: 2022-11-22 12:22
     */
    public RegionTemplate findById(Template template, Long id) throws RuntimeException {
        String key = String.format(KEY, id);
        RegionTemplate bo = (RegionTemplate) redisUtil.get(key);
        if(!Objects.isNull(bo)){
            this.build(bo);
            return bo;
        }
        log.debug("findById: id = {}", id);

        Optional<RegionTemplatePo> ret = regionTemplatePoMapper.findById(id);
        if (ret.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "运费模板", id));
        } else {
            return build(template, ret.get(), Optional.ofNullable(key));
        }
    }

    /**
     * 根据运费模板id和地区id来查找地区模板信息
     * 如果没有与rid对应的地区模板，不会继续查询上级地区模板
     *
     * @param template 运费模板
     * @param rid      地区id
     * @throws RuntimeException
     */
    public Optional<RegionTemplate> retrieveByTemplateAndRegionId(Template template, Long rid) throws RuntimeException {
        String key = String.format(KEY, rid, template.getId());
        //先用rid和tid的redisKey来寻找对应的地区模板id
        RegionTemplate regionTemplate = (RegionTemplate) this.redisUtil.get(key);
        if (Objects.isNull(regionTemplate)) {
            Optional<RegionTemplatePo> ret = this.regionTemplatePoMapper.findByTemplateIdAndRegionId(template.getId(), rid);
            if (ret.isPresent()) {
                regionTemplate = this.build(template, ret.get(), Optional.ofNullable(key));
                log.debug("ret.get = {}", ret.get());
            }
        } else {
            regionTemplate = this.build(regionTemplate);
        }

        log.debug("retrieveByTemplateIdAndRegionId: regionTemplate={}", regionTemplate);
        return Optional.ofNullable(regionTemplate);
    }

    /**
     * 根据模板id查找所有的地区模板信息
     *
     * @param template 模板
     * @param page
     * @param pageSize
     * @throws RuntimeException
     */
    public List<RegionTemplate> retrieveByTemplate(Template template, Integer page, Integer pageSize) {
        List<RegionTemplatePo> ret = null;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        log.debug("retrieveByTemplateId:page={},pageSize={}", pageable.getPageNumber(), pageable.getPageSize());
        ret = this.regionTemplatePoMapper.findByTemplateId(template.getId(), pageable);

        log.debug("retrieveRegionTemplateById: po = {}", ret);
        List<RegionTemplate> templateList = ret.stream()
                .map(po -> this.build(template, po, Optional.ofNullable(null)))
                .collect(Collectors.toList());
        return templateList;
    }

    public List<RegionTemplate> retrieveByTemplate(Template template) {
        List<RegionTemplatePo> ret = null;
        ret = this.regionTemplatePoMapper.findByTemplateId(template.getId());
        log.debug("retrieveRegionTemplateById: po = {}", ret);
        List<RegionTemplate> templateList = ret.stream()
                .map(po -> this.build(template, po, Optional.ofNullable(null)))
                .collect(Collectors.toList());
        return templateList;
    }

    /**
     * 修改模板
     *
     * @param bo
     * @param user
     * @throws RuntimeException
     * @author Ming Qiu
     * <p>
     * date: 2022-11-22 17:14
     */
    public String save(Template template, RegionTemplate bo, UserDto user) throws RuntimeException {
        log.debug("save: bo ={}, user = {}", bo, user);

        String key = String.format(KEY, bo.getRegionId(), bo.getTemplateId());
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        RegionTemplatePo po = CloneFactory.copy(new RegionTemplatePo(), bo);
        RegionTemplatePo savePo = this.regionTemplatePoMapper.save(po);
        if (savePo.getId().equals(IDNOTEXIST)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "运费模板", bo.getId()));
        }
        TemplateDaoInf dao = this.findTemplateBean(template);
        dao.save(bo);
        return key;
    }

    /**
     * 删除地区模板
     *
     * @param template
     * @param rid
     * @throws RuntimeException
     */
    public List<String> delRegionByTemplateIdAndRegionId(Template template, Long rid) {
        log.debug("delRegionByTemplateIdAndRegionId: template ={},rid={}", template, rid);
        List<String> delKeys = new ArrayList<>();
        Optional<RegionTemplatePo> ret = regionTemplatePoMapper.findByTemplateIdAndRegionId(template.getId(), rid);
        log.debug("delRegionByTemplateIdAndRegionId: ret ={}", ret);
        if (ret.isPresent()) {
            RegionTemplatePo po = ret.get();
            String key = String.format(KEY, rid,template.getId());
            TemplateDaoInf dao = this.findTemplateBean(template);
            regionTemplatePoMapper.deleteById(po.getId());
            if (redisUtil.hasKey(key))
                redisUtil.del(key);
            delKeys.add(key);
            dao.delete(po.getObjectId());
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
        return delKeys;
    }

    /**
     * 删除运费模板，同步删除该模板所拥有的所有地区模板
     *
     * @param template
     * @throws RuntimeException
     */
    public List<String> deleteTemplate(Template template) throws RuntimeException {
        log.debug("deleteTemplate: template ={}", template);
        List<String> delKeys = new ArrayList<>();
        this.templatePoMapper.deleteById(template.getId());
        TemplateDaoInf dao = this.findTemplateBean(template);

        Integer page = 0, pageSize = MAX_RETURN;
        while (pageSize.equals(MAX_RETURN)) {
            Pageable pageable = PageRequest.of(page, pageSize);
            List<RegionTemplatePo> ret = regionTemplatePoMapper.findByTemplateId(template.getId(), pageable);
            for (RegionTemplatePo po : ret) {
                String key = String.format(KEY, po.getRegionId(), template.getId());
                delKeys.add(key);
                dao.delete(po.getObjectId());
            }
            page = page + 1;
            pageSize = ret.size();
        }
        return delKeys;
    }

    /**
     * 新增模板
     *
     * @param bo
     * @param user
     * @throws RuntimeException
     * @author Ming Qiu
     * <p>
     * date: 2022-11-22 17:14
     */
    public RegionTemplate insert(Template template, RegionTemplate bo, UserDto user) throws RuntimeException {
        log.debug("insert: bo ={}, user = {}", bo, user);
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());
        RegionTemplatePo po = CloneFactory.copy(new RegionTemplatePo(), bo);

        TemplateDaoInf dao = this.findTemplateBean(template);
        String objectId = dao.insert(bo);
        log.debug("objectId:{}",objectId);
        po.setObjectId(objectId);
        bo.setObjectId(objectId);
        log.debug("save: po = {}", po);
        try {
            RegionTemplatePo newPo = this.regionTemplatePoMapper.save(po);
            log.debug("save: newPo = {}", newPo);
            bo.setId(newPo.getId());
        } catch (DuplicateKeyException exception) {
            throw new BusinessException(ReturnNo.FREIGHT_REGIONEXIST, String.format(ReturnNo.FREIGHT_REGIONEXIST.getMessage(), bo.getRegionId()));
        }
        log.debug("after sale-bo:{}",bo);
        return bo;
    }
}
