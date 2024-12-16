//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;

import cn.edu.xmu.oomall.shop.dao.bo.divide.DivideStrategy;
import cn.edu.xmu.oomall.shop.dao.bo.divide.PackAlgorithm;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import cn.edu.xmu.oomall.shop.mapper.TemplatePoMapper;

import cn.edu.xmu.oomall.shop.mapper.po.TemplatePo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Repository
@Slf4j
public class TemplateDao {
    private static final String KEY = "t%d";

    @Value("${oomall.shop.region-template.timeout}")
    private Long timeout;
    @Value("${oomall.shop.region-template.strategy}")
    private String strategy;
    @Value("${oomall.shop.region-template.algorithm}")
    private String algorithm;

    private final TemplatePoMapper templatePoMapper;

    private final RegionTemplateDao regionTemplateDao;
    private final RegionDao regionDao;
    private final RedisUtil redisUtil;

    @Lazy
    public TemplateDao(TemplatePoMapper templatePoMapper, RegionTemplateDao regionTemplateDao, RegionDao regionDao, RedisUtil redisUtil) {
        this.templatePoMapper = templatePoMapper;
        this.regionTemplateDao = regionTemplateDao;
        this.regionDao = regionDao;
        this.redisUtil = redisUtil;
    }

    public Template build(TemplatePo po, Optional<String> redisKey) {
        Template bo = CloneFactory.copy(new Template(), po);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return build(bo);
    }

    public Template build(Template template){
        template.setRegionTemplateDao(this.regionTemplateDao);
        template.setRegionDao(this.regionDao);
        DivideStrategy divideStrategy;
        PackAlgorithm packAlgorithm;

        String algorithm = Objects.isNull(template.getPackAlgorithm())?this.algorithm:template.getPackAlgorithm();
        String strategy = Objects.isNull(template.getDivideStrategy())?this.strategy:template.getDivideStrategy();

        try {
            packAlgorithm = (PackAlgorithm) Class.forName(algorithm).getDeclaredConstructor().newInstance();
            log.debug("build: packAlgorithm = {}", packAlgorithm);
            try {
                divideStrategy = (DivideStrategy) Class.forName(strategy).getDeclaredConstructor(PackAlgorithm.class).newInstance(packAlgorithm);
                template.setStrategy(divideStrategy);
            } catch (Exception e) {
                log.error("build: message = {}", e.getMessage());
                throw new BusinessException(ReturnNo.APPLICATION_PARAM_ERR, String.format(ReturnNo.APPLICATION_PARAM_ERR.getMessage(), "oomall.shop.region-template.strategy"));
            }

        } catch (Exception e) {
            log.error("build: message = {}", e.getMessage());
            throw new BusinessException(ReturnNo.APPLICATION_PARAM_ERR, String.format(ReturnNo.APPLICATION_PARAM_ERR.getMessage(), "oomall.shop.region-template.algorithm"));
        }
        return template;
    }

    /**
     * 查询运费模板
     *
     * @param shopId
     * @param name
     * @param page
     * @param pageSize
     * @throws RuntimeException
     */
    public List<Template> retrieveTemplateByName(Long shopId, String name, Integer page, Integer pageSize) throws RuntimeException {
        Pageable pageable = PageRequest.of(page-1, pageSize);
        List<TemplatePo> pos = null;
        if(name.trim().isEmpty()) {
            pos = this.templatePoMapper.findByShopId(shopId,pageable);
        }else {
            pos = this.templatePoMapper.findByNameAndShopId(name,shopId,pageable);
        }

        return pos.stream().map(po -> CloneFactory.copy(new Template(),po)).collect(Collectors.toList());
    }

    /**
     * 新增模板
     *
     * @param template
     * @param user
     * @throws RuntimeException
     */
    public Template insert(Template template, UserDto user) throws RuntimeException {
        template.setCreator(user);
        template.setGmtCreate(LocalDateTime.now());
        TemplatePo po = CloneFactory.copy(new TemplatePo(), template);
        log.debug("insertTemplate: po = {}", po);
        TemplatePo newPo = templatePoMapper.save(po);
        log.debug("insertTemplate: newPo = {}", newPo);
        template.setId(newPo.getId());
        return template;
    }

    /**
     * 通过id来查找模板
     */
    public Template findById(Long shopId, Long id) {
        log.debug("findTemplateById: id = {}", id);
        String key = String.format(KEY, id);
        Template template = (Template) redisUtil.get(key);
        if (Objects.isNull(template)) {
            Optional<TemplatePo> ret = this.templatePoMapper.findById(id);
            if (ret.isEmpty()) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "运费模板", id));
            }
            TemplatePo po = ret.get();
            if (!po.getShopId().equals(shopId) && !shopId.equals(PLATFORM)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "模板", po.getId(), shopId));
            }
            return this.build(po, Optional.ofNullable(key));
        }else{
            return this.build(template);
        }
    }

    /**
     * 修改运费模板
     *
     * @param bo
     * @param user
     * @throws RuntimeException
     */
    public void save(Template bo, UserDto user) throws RuntimeException {
        log.debug("saveTemplateById: bo ={}, user = {}", bo, user);
        if (bo.getId().equals(null)) {
            throw new IllegalArgumentException("save: template id is null");
        }
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        TemplatePo po = CloneFactory.copy(new TemplatePo(), bo);
        TemplatePo retPo = this.templatePoMapper.save(po);
        if (retPo.getId().equals(IDNOTEXIST)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "运费模板", bo.getId()));
        }
        log.debug("saveTemplateById: po ={}", po);
    }


}
