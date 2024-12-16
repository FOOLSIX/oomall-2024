//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.dao.activity;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.product.dao.bo.Activity;
import cn.edu.xmu.oomall.product.dao.bo.GrouponAct;
import cn.edu.xmu.oomall.product.mapper.mongo.GrouponActPoMapper;
import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;
import cn.edu.xmu.oomall.product.mapper.po.GrouponActPo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.util.Common.*;

@Slf4j
@Repository
public class GrouponActDao implements ActivityInf{


    private final GrouponActPoMapper actPoMapper;

    @Autowired
    public GrouponActDao(GrouponActPoMapper actPoMapper) {
        this.actPoMapper = actPoMapper;
    }

    @Override
    public Activity getActivity(ActivityPo po) throws RuntimeException{
        GrouponAct bo = CloneFactory.copy(new GrouponAct(), po);
        Optional<GrouponActPo> ret = this.actPoMapper.findById(po.getObjectId());
        if (ret.isEmpty()){
            log.error("getActivity: can not find in collection activity in mongo. objectId = {}",po.getObjectId());
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA, String.format(ReturnNo.INCONSISTENT_DATA.getMessage(), "活动", po.getId(), "mongoDB not exists data with id: " + po.getObjectId()));
        }

        log.debug("getActivity: actPo = {}", ret.get());
        CloneFactory.copy(bo, ret.get());
        log.debug("getActivity: bo = {}", bo);
        return bo;
    }

    @Override
    public String insert(Activity bo) throws RuntimeException{
        assert (bo instanceof GrouponAct) :"GrouponActDao.insert: must be groupon obj";
        GrouponAct act = (GrouponAct) bo;
        GrouponActPo po = CloneFactory.copy(new GrouponActPo(), act);
        GrouponActPo newPo = this.actPoMapper.insert(po);
        return newPo.getObjectId();
    }

    @Override
    public void save(Activity bo) throws RuntimeException{
        assert (bo instanceof GrouponAct) :"GrouponActDao.insert: must be groupon obj";
        GrouponAct act = (GrouponAct) bo;
        GrouponActPo po = CloneFactory.copy(new GrouponActPo(), act);

        Optional<GrouponActPo> ret = this.actPoMapper.findById(po.getObjectId());
        if (ret.isEmpty()) {
            log.error("save: can not find in collection activity in mongo. objectId = {}",po.getObjectId());
            throw new BusinessException(ReturnNo.INCONSISTENT_DATA, String.format(ReturnNo.INCONSISTENT_DATA.getMessage(), "活动", po.getObjectId()));
        }
        GrouponActPo old = ret.get();
        if (Objects.isNull(po.getThresholds())) {
            po.setThresholds(old.getThresholds());
        }

        this.actPoMapper.save(po);
    }

}
