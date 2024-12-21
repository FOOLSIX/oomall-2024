package cn.edu.xmu.oomall.product.mapper.openfeign.impl;

import cn.edu.xmu.javaee.core.exception.BusinessException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public ShopMapperImpl(LocalShopMapper localShopMapper, LocalTemplateMapper localTemplateMapper, ShopFeignMapper feignMapper, ShopFeignMapper shopFeignMapper) {
        this.localShopMapper = localShopMapper;
        this.localTemplateMapper = localTemplateMapper;
        this.shopFeignMapper = shopFeignMapper;
    }

    @Override
    public InternalReturnObject<Shop> getShopById(Long id) {
        if (shopMapperType.equals("local")) {
            Optional<ShopPo> ret = localShopMapper.findById(id);
            ShopPo shopPo = ret.get();
            Shop shop = copy(new Shop(), shopPo);
            return new InternalReturnObject<>(0, "成功", shop);
        } else {
            return shopFeignMapper.getShopById(id);
        }
    }

    @Override
    public InternalReturnObject<Template> getTemplateById(Long shopId, Long id) {
        if (templateMapperType.equals("local")) {
            Optional<TemplatePo> ret = localTemplateMapper.findById(id);
            if (ret.isEmpty()) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "运费模板", id));
            }
            TemplatePo po = ret.get();
            if (!po.getShopId().equals(shopId) && !shopId.equals(PLATFORM)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "模板", po.getId(), shopId));
            }
            Template template = copy(new Template(), po);
            return new InternalReturnObject<>(0, "成功", template);
        } else {
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
