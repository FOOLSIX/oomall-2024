//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.shop.dao.TemplateDao;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.shop.dao.bo.template.*;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class RegionTemplateService {
    private final static Logger logger = LoggerFactory.getLogger(RegionTemplateService.class);

    private final TemplateDao templateDao;
    private final RegionDao regionDao;
    private final RedisUtil redisUtil;
    private final RegionTemplateDao regionTemplateDao;

    /**
     * 管理员修改重量或件数模板明细
     * @param shopId
     * @param bo
     * @param user
     */
    public void saveRegionTemplate(Long shopId, RegionTemplate bo, UserDto user, Class clazz){

        Template template= this.templateDao.findById(shopId, bo.getTemplateId());
        if (!template.gotType().getClass().equals(clazz)){
            throw new BusinessException(ReturnNo.FREIGHT_TEMPLATENOTMATCH);
        }
        String key = template.updateRegionTemplate(bo, user);
        this.redisUtil.del(key);
    }

    /**
     * 管理员定义重量或件数模板明细
     * @param shopId
     * @param regionTemplate
     * @param user
     */
    public void insertRegionTemplate(Long shopId, RegionTemplate regionTemplate, UserDto user, Class clazz){
        logger.debug("insertRegionTemplate: regionTemplate={},user={}",regionTemplate,user);
        this.regionDao.findById(regionTemplate.getRegionId());
        Template template = this.templateDao.findById(shopId, regionTemplate.getTemplateId());
        template.createRegionTemplate(clazz, regionTemplate, user);
    }

    /**
     * 管理员删除地区模板
     * @param shopId
     * @param id  模板id
     * @param rid 地区id
     */

    public void deleteRegionTemplate(Long shopId,Long id,Long rid){
        Template template = templateDao.findById(shopId,id);
        logger.debug("deleteRegionTemplate-template:{}",template);
        regionTemplateDao.delRegionByTemplateIdAndRegionId(template,rid);
    }

    /**
     * 店家或管理员查询运费模板下属所有地区模板明细
     * @param shopId 商铺id
     * @param templateId 模板id
     * @param page
     * @param pageSize
     */
    public List<RegionTemplate> retrieveRegionTemplateById(Long shopId, Long templateId, Integer page, Integer pageSize){
        Template template= this.templateDao.findById(shopId, templateId);
        return this.regionTemplateDao.retrieveByTemplate(template, page, pageSize);
    }

    /**
     * 克隆模板时，其关联的运费模板也需要克隆
     * @param id
     * @param shopId
     * @param user
     */
    public Template cloneTemplate(Long id, Long shopId, UserDto user) {

        Template template = this.templateDao.findById(shopId,id);

        //克隆template
        template.setId(null);
        template.setDefaultModel((byte) 0);
        Template template1 = templateDao.insert(template,user);

        //按template将所有关联的regionTemplate取出，将其id和objectId设为0重新插入数据库
        List<RegionTemplate> regionTemplates = this.regionTemplateDao.retrieveByTemplate(template);
        regionTemplates.stream().forEach(regionTemplate -> {
            regionTemplate.setTemplateId(template1.getId());
            regionTemplate.setObjectId(null);
            regionTemplateDao.insert(template,regionTemplate,user);
        });
        return template1;

    }

    public void cloneRegionTemplate(Long shopId, Long id, Long sourceRegionId, Long targetRegionId, UserDto user) {
        Template template = this.templateDao.findById(shopId,id);
        RegionTemplate regionTemplate = this.regionTemplateDao.findById(template,sourceRegionId);
        logger.debug("sourceRegionTemplate:{}",regionTemplate);
        regionTemplate.setRegionId(targetRegionId);
        regionTemplate.setObjectId(null);
        RegionTemplate regionTemplate1 = regionTemplateDao.insert(template,regionTemplate,user);
        logger.debug("targetRegionTemplate:{}",regionTemplate1);
    }
}
