//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.shop.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.shop.controller.dto.ShopConsigneeDto;
import cn.edu.xmu.oomall.shop.controller.dto.ShopModifyDto;
import cn.edu.xmu.oomall.shop.controller.dto.ShopDto;
import cn.edu.xmu.oomall.shop.dao.ShopDao;
import cn.edu.xmu.oomall.shop.dao.TemplateDao;
import cn.edu.xmu.oomall.shop.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.shop.mapper.po.ShopPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 商铺对象
 */
@ToString(callSuper = true, doNotUseGetters = true)
@NoArgsConstructor
@CopyFrom({ShopDto.class, ShopModifyDto.class, ShopConsigneeDto.class, ShopPo.class})
public class Shop extends OOMallObject implements Serializable {

    /**
     * 申请
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte NEW = 0;
    /**
     * 下线
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte OFFLINE = 1;
    /**
     * 上线
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte ONLINE = 2;
    /**
     * 停用
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte ABANDON = 3;

    /**
     * 状态和名称的对应
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Map<Byte, String> STATUSNAMES = new HashMap() {
        {
            put(NEW, "申请");
            put(OFFLINE, "下线");
            put(ONLINE, "上线");
            put(ABANDON, "停用");
        }
    };

    /**
     * 允许的状态迁移
     */
    @JsonIgnore
    @ToString.Exclude
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>() {
        {
            put(NEW, new HashSet<>() {
                {
                    add(OFFLINE);
                }
            });
            put(OFFLINE, new HashSet<>() {
                {
                    add(ONLINE);
                    add(ABANDON);
                }
            });
            put(ONLINE, new HashSet<>() {
                {
                    add(OFFLINE);
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     *
     * @param status
     * @return
     * @author Ming Qiu
     * <p>
     * date: 2022-11-13 0:25
     */
    public boolean allowStatus(Byte status) {
        boolean ret = false;

        if (null != status && null != this.status) {
            Set<Byte> allowStatusSet = toStatus.get(this.status);
            if (null != allowStatusSet) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    /**
     * 获得当前状态名称
     *
     * @return
     * @author Ming Qiu
     * <p>
     * date: 2022-11-13 0:43
     */
    @JsonIgnore
    public String getStatusName() {
        return STATUSNAMES.get(this.status);
    }

    /**
     * 服务商
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte SERVICE = 1;
    /**
     * 电商
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte RETAILER = 0;

    /**
     * 商铺名称
     */
    private String name;

    /**
     * 商铺保证金
     */
    private Long deposit;

    /**
     * 商铺保证金门槛
     */
    private Long depositThreshold;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系人
     */
    private String consignee;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 免邮门槛
     */
    private Integer freeThreshold;

    /**
     * 地区
     */
    private Long regionId;


    @ToString.Exclude
    @JsonIgnore
    private Region region;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private RegionDao regionDao;


    public Region getRegion() {
        if (this.region.equals(null) && !this.regionDao.equals(null)) {
            this.region = this.regionDao.findById(this.regionId);
        }
        return this.region;
    }

    /**
     * 商铺id
     */


    @ToString.Exclude
    @JsonIgnore
    private Shop shop;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ShopDao shopDao;


    @ToString.Exclude
    @JsonIgnore
    @Setter
    private TemplateDao templateDao;

    /**
     * 商铺审核通过
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void approved(UserDto user) {
        if (!this.status.equals(Shop.NEW)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "商铺", this.getId(), user.getDepartId()));
        }
        this.changeStatus(Shop.OFFLINE, user);
    }

    /**
     * 关闭商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void abandon(UserDto user) {
        if (!this.status.equals(Shop.OFFLINE)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "商铺", this.getId(), user.getDepartId()));
        }
        this.changeStatus(Shop.ABANDON, user);
    }

    /**
     * 上线商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void online(UserDto user) {
        if (!this.status.equals(Shop.OFFLINE)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "商铺", this.getId(), user.getDepartId()));
        }
        this.changeStatus(Shop.ONLINE, user);
    }

    /**
     * 下线商铺
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    public void offline(UserDto user) {
        if (!this.status.equals(Shop.ONLINE)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "商铺", this.getId(), user.getDepartId()));
        }
        this.changeStatus(Shop.OFFLINE, user);
    }

    /**
     * 修改商铺状态
     *
     * @author WuTong
     * @task 2023-dgn1-007
     */
    private void changeStatus(Byte status, UserDto user) {
        if (!this.allowStatus(status)) {
            // 状态不允许变动
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "商铺", this.id, STATUSNAMES.get(this.status)));
        }
        Shop shop = new Shop();
        shop.setStatus(status);
        shop.setId(this.id);
        this.shopDao.save(shop, user);
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public Long getDepositThreshold() {
        return depositThreshold;
    }

    public void setDepositThreshold(Long depositThreshold) {
        this.depositThreshold = depositThreshold;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getFreeThreshold() {
        return freeThreshold;
    }

    public void setFreeThreshold(Integer freeThreshold) {
        this.freeThreshold = freeThreshold;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
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
}
