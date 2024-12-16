//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;
import cn.edu.xmu.oomall.product.mapper.po.GrouponActPo;
import cn.edu.xmu.oomall.product.model.Threshold;
import cn.edu.xmu.oomall.product.controller.dto.GrouponActDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 团购活动
 */
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@CopyFrom({GrouponActDto.class,  GrouponActPo.class, ActivityPo.class})
public class GrouponAct extends Activity{

    public static final String ACTCLASS = "grouponActDao";

    private List<Threshold> thresholds;


    public GrouponAct() {
        super();
        this.actClass = ACTCLASS;
    }

    /**
     * 发布活动
     * @return
     */
    public GrouponAct publish(){
        if (NEW != this.status){
            throw new BusinessException(ReturnNo.STATENOTALLOW,String.format(ReturnNo.STATENOTALLOW.getMessage(),"团购", this.id, STATUSNAMES.get(NEW)));
        }
        GrouponAct ret = new GrouponAct();
        ret.setId(this.id);
        ret.setStatus(ACTIVE);
        return ret;
    }

    /**
     * 取消活动
     * @param user 操作者
     * @return
     */
    public List<String> cancel(UserDto user){
        List<OnSale> onsaleList = this.getOnsaleList();
        LocalDateTime now = LocalDateTime.now();
        List<String> keys = new ArrayList<>(onsaleList.size());
        for (OnSale obj : onsaleList){
            String key = obj.cancel(now, user);
            keys.add(key);
        }
        return keys;
    }

    /**
     * @author WuTong
     * @task 2023-dgn2-008
     * 插入商品到活动中
     * @return
     */
    public OnSale addOnsaleOnAct(Product product,OnSale onsale, UserDto user) {
        List<OnSale> onsaleList = this.getOnsaleList();

        // 已经结束的活动不能够新增商品
        LocalDateTime actBeginTime = onsaleList.get(0).getBeginTime();
        LocalDateTime actEndTime = onsaleList.get(0).getEndTime();
        if (actBeginTime.isAfter(actEndTime) || actBeginTime.equals(actEndTime)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "活动", this.getId(), "已结束"));
        }

        onsale.setBeginTime(actBeginTime);
        onsale.setEndTime(actEndTime);
        OnSale newOnsale = product.createOnsale(onsale, user);
        return newOnsale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getActClass() {
        return actClass;
    }

    public void setActClass(String actClass) {
        this.actClass = actClass;
    }

    public List<Threshold> getThresholds() {
        return thresholds;
    }

    public void setThresholds(List<Threshold> thresholds) {
        this.thresholds = thresholds;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }


}
