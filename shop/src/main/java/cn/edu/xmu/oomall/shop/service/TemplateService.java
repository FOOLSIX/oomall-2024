//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.shop.dao.ShopDao;
import cn.edu.xmu.oomall.shop.dao.TemplateDao;
import cn.edu.xmu.oomall.shop.dao.bo.Shop;
import cn.edu.xmu.oomall.shop.dao.bo.template.Template;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class TemplateService {
    private final static Logger logger = LoggerFactory.getLogger(TemplateService.class);

    private final RedisUtil redisUtil;
    private final TemplateDao templateDao;
    private final RegionTemplateDao regionTemplateDao;
    private final ShopDao shopDao;
    private final RocketMQTemplate rocketMQTemplate;

        /**
     * 获得商品的运费模板
     * @author Zhanyu Liu
     * @date 2022/11/30 7:42
     * @param shopId 商铺id
     * @param name 模板名称
     * @param page
     * @param pageSize
     */
    public List<Template> retrieveTemplateByName(Long shopId, String name, Integer page, Integer pageSize){
        return this.templateDao.retrieveTemplateByName(shopId,name,page,pageSize);
    }

    /**
     * 管理员定义运费模板
     * @param shopId
     * @param template
     * @param user
     */
    public Template createTemplate(Long shopId, Template template, UserDto user){
        Shop shop = this.shopDao.findById(shopId);
        template.setShopId(shopId);
        return templateDao.insert(template,user);
    }


    /**
     * 获得运费模板详情
     * @param shopId
     * @param id
     */
    public Template findTemplateById(Long shopId,Long id){
        return this.templateDao.findById(shopId, id);
    }

    /**
     * 管理员修改运费模板
     * @param shopId
     * @param template
     * @param user
     */

    public void updateTemplateById(Long shopId, Template template, UserDto user){
        templateDao.findById(shopId,template.getId());
        templateDao.save(template,user);
    }

    /**
     * 删除运费模板，且同步删除与商品的关系
     * @param id
     * @param shopId
     */
    public void sendDelTemplateMsg(UserDto user, Long shopId, Long id){
        Template template= this.templateDao.findById(shopId, id);
        /*
            向goods模块发Transaction消息，将商品纪录中的对应TemplateId也一并删除
         */
        String json = String.format("{'shopId':%d, 'id':%d}", shopId, id);
        Message msg = MessageBuilder.withPayload(json).setHeader("user", user).build();
        //rocketMQTemplate.sendMessageInTransaction("Del-Template", msg, null);

    }

    /**
     * 删除运费模板
     * @param shopId 商铺id
     * @param id 运费模板id
     */
    public void deleteTemplate(Long shopId, Long id){
        Template template = this.templateDao.findById(shopId, id);
        List<String> delKeys = regionTemplateDao.deleteTemplate(template);
        this.redisUtil.del(delKeys.toArray(new String[0]));
    }
}
