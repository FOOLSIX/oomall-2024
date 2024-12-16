//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.template;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.shop.dao.bo.template.RegionTemplate;
import cn.edu.xmu.oomall.shop.dao.bo.template.WeightTemplate;
import cn.edu.xmu.oomall.shop.mapper.po.RegionTemplatePo;
import cn.edu.xmu.oomall.shop.mapper.WeightTemplatePoMapper;
import cn.edu.xmu.oomall.shop.mapper.po.WeightTemplatePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class WeightTemplateDao implements TemplateDaoInf {

    private final WeightTemplatePoMapper mapper;

    @Override
    public RegionTemplate getRegionTemplate(RegionTemplatePo po) throws RuntimeException {
        WeightTemplate bo = CloneFactory.copy(new WeightTemplate(), po);
        Optional<WeightTemplatePo> wPo = this.mapper.findById(po.getObjectId());
        wPo.ifPresent(templatePo -> {
            CloneFactory.copy(bo, templatePo);
            bo.setObjectId(templatePo.getObjectId());
        });
        return bo;
    }

    @Override
    public void save(RegionTemplate bo) throws RuntimeException {
        Optional<WeightTemplatePo> ret = this.mapper.findById(bo.getObjectId());
        if(ret.isEmpty()){
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA);
        }
        WeightTemplatePo savedPo = ret.get();
        WeightTemplatePo po = CloneFactory.copy(new WeightTemplatePo(), (WeightTemplate) bo);

        if(Objects.isNull(po.getFirstWeight())){
            po.setFirstWeight(savedPo.getFirstWeight());
        }
        if(Objects.isNull(po.getFirstWeightPrice())){
            po.setFirstWeightPrice(savedPo.getFirstWeightPrice());
        }
        if(Objects.isNull(po.getThresholds())){
            po.setThresholds(savedPo.getThresholds());
        }

        WeightTemplatePo retp = this.mapper.save(po);
        log.debug("WeightTemplatePo:{}",retp);
    }

    @Override
    public void delete(String id) throws RuntimeException {
        this.mapper.deleteById(id);
    }

    @Override
    public String insert(RegionTemplate bo) throws RuntimeException {
        WeightTemplatePo po = CloneFactory.copy(new WeightTemplatePo(), (WeightTemplate)bo);
        WeightTemplatePo newPo = this.mapper.insert(po);
        return newPo.getObjectId();
    }

}
