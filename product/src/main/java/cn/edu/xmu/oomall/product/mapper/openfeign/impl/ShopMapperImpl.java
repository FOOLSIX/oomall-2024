package cn.edu.xmu.oomall.product.mapper.openfeign.impl;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.product.mapper.jpa.LocalShopMapper;
import cn.edu.xmu.oomall.product.mapper.jpa.LocalTemplateMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopFeignMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Consignee;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
import cn.edu.xmu.oomall.product.mapper.po.ShopPo;
import cn.edu.xmu.oomall.product.mapper.po.TemplatePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Component
public class ShopMapperImpl implements ShopMapper {

    @Value("${shop.mapper.type}")
    private String shopMapperType;

    @Value("${template.mapper.type}")
    private String templateMapperType;

    private final LocalShopMapper localShopMapper;

    private final LocalTemplateMapper localTemplateMapper;

    private final ShopFeignMapper shopFeignMapper;

    private final RedisUtil redisUtil;

    public ShopMapperImpl(LocalShopMapper localShopMapper, LocalTemplateMapper localTemplateMapper, ShopFeignMapper feignMapper, ShopFeignMapper shopFeignMapper, RedisUtil redisUtil) {
        this.localShopMapper = localShopMapper;
        this.localTemplateMapper = localTemplateMapper;
        this.shopFeignMapper = shopFeignMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    public InternalReturnObject<Shop> getShopById(Long id) {
        String key = String.format("shop:%d", id);  // 生成缓存的键

        // 如果是本地调用，需要缓存数据
        if (shopMapperType.equals("local")) {
            // 尝试从 Redis 获取缓存数据
            Shop shop = (Shop) redisUtil.get(key);

            if (shop == null) {  // 如果缓存中没有数据
                Optional<ShopPo> ret = localShopMapper.findById(id);
                if (ret.isPresent()) {
                    ShopPo shopPo = ret.get();
                    shop = copy(new Shop(), shopPo);

                    // 将从数据库中获取到的数据存入 Redis，缓存 1 小时
                    redisUtil.set(key,  shop, 600);  // 3600 秒 = 1 小时
                }
            }

            return new InternalReturnObject<>(0, "成功", shop);
        } else {
            // 如果是远程服务调用，不需要缓存
            return shopFeignMapper.getShopById(id);
        }
    }

    @Override
    public InternalReturnObject<Template> getTemplateById(Long shopId, Long id) {
        String key = String.format("template:%d:%d", shopId, id);  // 生成缓存的键

        // 如果是本地调用，需要缓存数据
        if (templateMapperType.equals("local")) {
            // 尝试从 Redis 获取缓存数据
            Template template = (Template) redisUtil.get(key);

            if (template == null) {  // 如果缓存中没有数据
                Optional<TemplatePo> ret = localTemplateMapper.findById(id);
                if (ret.isEmpty()) {
                    throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST,
                            String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "运费模板", id));
                }
                TemplatePo po = ret.get();
                if (!po.getShopId().equals(shopId) && !shopId.equals(PLATFORM)) {
                    throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,
                            String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "模板", po.getId(), shopId));
                }
                template = copy(new Template(), po);

                // 将从数据库中获取到的数据存入 Redis，缓存 1 小时
                redisUtil.set(key, template, 3600);  // 3600 秒 = 1 小时
            }

            return new InternalReturnObject<>(0, "成功", template);
        } else {
            // 如果是远程服务调用，不需要缓存
            return shopFeignMapper.getTemplateById(shopId, id);
        }
    }


    // 为了防止测试方案的代码入侵生产环境,这里选择直接本地copy进行实验测试

    private Shop copy(Shop target, ShopPo shopPo) {
        // 开始复制字段
        target.setId(shopPo.getId());
        target.setName(shopPo.getName());
        target.setStatus(shopPo.getStatus());
        target.setDeposit(shopPo.getDeposit());
        target.setDepositThreshold(shopPo.getDepositThreshold());

        // 特殊字段需要额外处理，例如 consignee 的赋值（从 String 转换为 Consignee）
        if (shopPo.getConsignee() != null) {
            Consignee consignee = new Consignee();
            consignee.setName(shopPo.getConsignee());
            consignee.setMobile(shopPo.getMobile());
            consignee.setAddress(shopPo.getAddress());
            target.setConsignee(consignee);
        }
        return target; // 返回目标对象
    }

    private Template copy(Template target, TemplatePo source) {
        if (source == null) {
            return null; // 如果源对象为空，返回 null
        }

        target.setId(source.getId());
        target.setName(source.getName());

        return target; // 返回复制后的目标对象
    }

}
