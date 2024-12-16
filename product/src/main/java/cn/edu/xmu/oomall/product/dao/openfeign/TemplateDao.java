//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.product.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TemplateDao {

    private final ShopMapper shopMapper;

    public Template findById(Long shopId, Long id) {
        InternalReturnObject<Template> ret = this.shopMapper.getTemplateById(shopId, id);
        return ret.getData();
    }

}
