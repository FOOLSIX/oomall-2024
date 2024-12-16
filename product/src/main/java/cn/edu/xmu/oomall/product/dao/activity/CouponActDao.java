//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.dao.activity;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.bo.Activity;
import cn.edu.xmu.oomall.product.dao.bo.CouponAct;
import cn.edu.xmu.oomall.product.mapper.mongo.CouponActPoMapper;
import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;
import cn.edu.xmu.oomall.product.mapper.po.CouponActPo;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class CouponActDao implements ActivityInf{


    private CouponActPoMapper actPoMapper;


    @Autowired
    public CouponActDao(CouponActPoMapper actPoMapper) {
        this.actPoMapper = actPoMapper;
    }

    @Override
    public Activity getActivity(ActivityPo po)  throws RuntimeException {
        log.debug("---getActivity: po = {}",po);
        Optional<CouponActPo> actPo = actPoMapper.findById("657302634882bc534d981832");
        log.debug("objid->657302634882bc534d981832:{},po.objid:{}" ,actPo,po.getObjectId());
        CouponAct bo = CloneFactory.copy(new CouponAct(), po);
        log.debug("---copy after getActivity: po = {}",po);
        Optional<CouponActPo> ret = this.actPoMapper.findById(po.getObjectId());
        log.debug("---getActivity: ret = {}",ret);
        ret.ifPresent(couponActPo -> {
            CloneFactory.copy(bo, couponActPo);
            bo.setStrategy(BaseCouponDiscount.getInstance(couponActPo.getStrategy()).orElse(null));
        });
        log.debug("---getActivity: bo = {}",bo);
        return bo;
    }

    @Override
    public String insert(Activity bo) throws RuntimeException{
        log.debug("insert: bo = {}",bo);
        CouponActPo po = CloneFactory.copy(new CouponActPo(), (CouponAct) bo);
        po.setStrategy(serializeStrategy((CouponAct) bo));
        CouponActPo newPo = this.actPoMapper.insert(po);
        log.debug("insert: newPo = {}",newPo);
        return newPo.getObjectId();
    }

    @Override
    public void save(Activity bo) throws RuntimeException{
        Optional<CouponActPo> ret = this.actPoMapper.findById(bo.getObjectId());
        if(ret.isEmpty()){
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA);
        }
        CouponActPo savedPo = ret.get();
        CouponActPo po = CloneFactory.copy(new CouponActPo(), (CouponAct) bo);
        if(po.getCouponTime()==null){
            po.setCouponTime(savedPo.getCouponTime());
        }
        if(po.getQuantity()==null){
            po.setQuantity(savedPo.getQuantity());
        }
        if(po.getQuantityType()==null){
            po.setQuantityType(savedPo.getQuantityType());
        }
        if(po.getValidTerm()==null){
            po.setValidTerm(savedPo.getValidTerm());
        }
        if(po.getStrategy()==null){
            po.setStrategy(savedPo.getStrategy());
        }
        else {
            po.setStrategy(serializeStrategy((CouponAct) bo));
        }
        this.actPoMapper.save(po);
    }

    private String serializeStrategy(CouponAct bo){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStrategy = mapper.writeValueAsString(bo.getStrategy());
            log.debug("insert: jsonStrategy = {}",jsonStrategy);
            return jsonStrategy;
        }
        catch(Exception e){
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }
}
