//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.region.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.region.dao.RegionDao;
import cn.edu.xmu.oomall.region.mapper.po.RegionPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(RegionPo.class)
@CopyTo(RegionPo.class)
public class Region extends OOMallObject implements Serializable {
    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Region.class);

    /**
     * 两种特殊id
     * 0 -- 最高级地区
     * -1 -- 不存在
     */
    @ToString.Exclude
    @JsonIgnore
    public static final Long TOP_ID = 0L;
    @ToString.Exclude
    @JsonIgnore
    public static final Long ROOT_PID = -1L;

    /**
     * 共三种状态
     */
    //有效
    @ToString.Exclude
    @JsonIgnore
    public static final Byte VALID = 0;
    //停用
    @ToString.Exclude
    @JsonIgnore
    public static final Byte SUSPENDED = 1;
    //废弃
    @ToString.Exclude
    @JsonIgnore
    public static final Byte ABANDONED = 2;

    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> STATUSNAMES = new HashMap<>() {
        {
            put(VALID, "有效");
            put(SUSPENDED, "停用");
            put(ABANDONED, "废弃");
        }
    };

    /**
     * 允许的状态迁移
     */
    @JsonIgnore
    @ToString.Exclude
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>() {
        {
            put(VALID, new HashSet<>() {
                {
                    add(SUSPENDED);
                    add(ABANDONED);
                }
            });
            put(SUSPENDED, new HashSet<>() {
                {
                    add(VALID);
                    add(ABANDONED);
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     * @param status 迁移去的状态
     */
    public boolean allowTransitStatus(Byte status) {
        boolean ret = false;
        assert (!Objects.isNull(this.status)):String.format("地区对象(id=%d)的状态为null",this.getId());
        Set<Byte> allowStatusSet = toStatus.get(this.status);
        if (!Objects.isNull(allowStatusSet)) {
            ret = allowStatusSet.contains(status);
        }
        return ret;
    }

    /**
     * 获得当前状态名称
     */
    @JsonIgnore
    public String getStatusName() {
        return STATUSNAMES.get(this.status);
    }


    @Setter
    @Getter
    private Long pid;
    @Setter
    private Byte level;
    @Setter
    @Getter
    private String areaCode;
    @Setter
    @Getter
    private String zipCode;
    @Setter
    @Getter
    private String cityCode;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String shortName;
    @Setter
    @Getter
    private String mergerName;
    @Setter
    @Getter
    private String pinyin;
    @Setter
    @Getter
    private Double lng;
    @Setter
    @Getter
    private Double lat;
    @Setter
    @Getter
    private Byte status;

    @JsonIgnore
    @ToString.Exclude
    private Region parentRegion;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private RegionDao regionDao;

    @JsonIgnore
    public Region getParentRegion() {
        logger.debug("getParentRegion: pid = {}", this.pid);
        if (!ROOT_PID.equals(this.pid) && null == this.parentRegion && null != this.regionDao) {
            this.parentRegion = this.regionDao.findById(pid);
        }
        return this.parentRegion;
    }

    @JsonIgnore
    public Byte getLevel() {
        if (null == this.level) {
            if (TOP_ID.equals(this.pid) || ROOT_PID.equals(this.pid)) {
                this.level = 0;
            } else if (!Objects.isNull(this.pid)) {
                this.level = (byte) (this.getParentRegion().getLevel() + 1);
            }
        }
        return this.level;
    }

    /**
     * 停用地区
     *
     * @param user 操作者
     * @return 删除的redis key
     */
    public List<String> suspend(UserDto user) {
        if (!this.allowTransitStatus(Region.SUSPENDED)) {
            // 状态不允许变动
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "地区", this.id, STATUSNAMES.get(this.status)));
        }
        return this.changeStatus(Region.SUSPENDED, user);
    }

    /**
     * 恢复地区
     *
     * @param user 操作者
     * @return 删除的redis key
     */
    public List<String> resume(UserDto user) {
        if (!this.allowTransitStatus(Region.VALID)) {
            // 状态不允许变动
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "地区", this.id, STATUSNAMES.get(this.status)));
        }

        List<Region> ancestors = this.getAncestors();
        List<Region> invalidRegions = ancestors.stream().filter(o -> !o.getStatus().equals(Region.VALID)).collect(Collectors.toList());
        if (0 == invalidRegions.size()) {
            return this.changeStatus(Region.VALID, user);
        } else {
            throw new BusinessException(ReturnNo.REGION_INVALID, String.format("上级地区%s不是正常的状态", invalidRegions.get(0).getName()));
        }
    }

    /**
     * 废弃地区
     *
     * @param user 操作者
     * @return 删除的redis key
     */
    public List<String> abandon(UserDto user) {
        if (!this.allowTransitStatus(Region.ABANDONED)) {
            // 状态不允许变动
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "地区", this.id, STATUSNAMES.get(this.status)));
        }

        return this.changeStatus(Region.ABANDONED, user);
    }

    /**
     * 递归修改地区状态
     *
     * @param status 状态
     */
    private List<String> changeStatus(Byte status, UserDto user) {
        logger.debug("changeStatus: id = {}, status = {}", this.id, status);

        Region region = new Region();
        region.setStatus(status);
        region.setId(this.id);
        String key = this.regionDao.save(region, user);

        List<Region> subRegions = this.regionDao.retrieveSubRegionsById(this.id, 1, MAX_RETURN);
        logger.debug("changeStatus: subRegion = {}", subRegions);
        List<String> keys = subRegions.stream().filter(o -> !o.getStatus().equals(Region.ABANDONED))
                .flatMap(subRegion -> {
                    List sub = null;
                    if (subRegion.allowTransitStatus(status)) {
                        sub = subRegion.changeStatus(status, user);
                        return (Stream<String>) sub.stream();
                    }else{
                        return new ArrayList<String>(0).stream();
                    }
                }).distinct().collect(Collectors.toList());
        keys.add(key);
        logger.debug("changeStatus: keys = {}", keys);
        return keys;
    }

    /**
     * 获得所有上级地区
     *
     * @return
     */
    @JsonIgnore
    public List<Region> getAncestors() {
        if (Objects.isNull(this.regionDao)){
            return new ArrayList<>(0);
        }else {
            return this.regionDao.retrieveParentsRegions(this);
        }
    }

    /**
     * 创建下级地区
     * 仅仅只能在valid和suspend状态下创建，创建出来的地区是syspend状态
     * @param region 下级地区
     * @param user   创建者
     * @return
     */
    public Region createSubRegion(Region region, UserDto user) {
        if (VALID.equals(this.status) || SUSPENDED.equals(this.status)) {
            region.setStatus(Region.SUSPENDED);
            region.setLevel((byte) (this.getLevel() + 1));
            region.setPid(this.id);
            logger.debug("createSubRegion: region = {}", region);
            return this.regionDao.insert(region, user);
        } else {
            throw new BusinessException(ReturnNo.REGION_ABANDONE, String.format(ReturnNo.REGION_ABANDONE.getMessage(), this.id));
        }

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
