//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;


public interface ShopMapper {

    InternalReturnObject<Shop> getShopById(Long id);

    InternalReturnObject<Template> getTemplateById(Long shopId, Long id);
}
