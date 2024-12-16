//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.service;

import cn.edu.xmu.oomall.shop.controller.vo.FreightPriceVo;
import cn.edu.xmu.oomall.shop.dao.TemplateDao;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.oomall.shop.dao.template.RegionTemplateDao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreightService {

    private final static Logger logger = LoggerFactory.getLogger(FreightService.class);

    private final RegionTemplateDao regionTemplateDao;
    private final TemplateDao templateDao;

    /**
     * 计算一批商品的运费
     *
     * @param items
     * @param templateId 模板id
     * @param regionId   地区id
     */
    public FreightPriceVo cacuFreightPrice(List<ProductItem> items, Long templateId, Long regionId) {
//        Template template = this.templateDao.findById(PLATFORM, templateId);
//        RegionTemplate regionTemplate = template.findRegionTemplate(regionId);
//        logger.debug("getFreight: regionTemplate={}", regionTemplate);
//
//        Collection<TemplateResult> ret = regionTemplate.calculate(items);
//
//        long fee = ret.stream().mapToLong(pack -> pack.getFee()).sum();
//        List<List<ProductItemVo>> packs = ret.stream().map(pack -> pack.getPack().stream().map(bo -> CloneFactory.copy(new ProductItemVo(), bo)).collect(Collectors.toList())).collect(Collectors.toList());
//        packs = packs.stream().map(pack -> pack.stream().sorted(Comparator.comparingLong(ProductItemVo::getOrderItemId)).collect(Collectors.toList())).collect(Collectors.toList());
//        return FreightPriceVo.builder().freightPrice(fee).pack(packs).build();
        return null;
    }
}
