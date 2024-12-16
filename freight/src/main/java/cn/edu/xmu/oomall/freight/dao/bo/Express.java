package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.ContractDao;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptor;
import cn.edu.xmu.oomall.freight.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.freight.mapper.po.ExpressPo;
import cn.edu.xmu.oomall.freight.mapper.po.Shop;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import cn.edu.xmu.javaee.core.aop.CopyFrom;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static cn.edu.xmu.javaee.core.model.Constants.SYSTEM;

/**
 * 2023-dgn3-009
 *
 * @author huangzian
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CopyFrom(ExpressPo.class)
@Slf4j
public class Express extends OOMallObject implements Serializable {

    private String billCode;

    private Long sendRegionId;

    private Region sendRegion;

    private String sendAddress;

    private String sendMobile;

    private Long receivRegionId;

    private Region receivRegion;

    private String receivAddress;

    private String receivMobile;

    private Byte status;

    private String orderCode;

    private Long shopId;

    private Shop shop;

    private Long contractId;

    private Contract contract;

    private String goodsType;

    private Long weight;

    /*暂定sendId为Express中的属性*/
    private Long sendId;

    /*暂定sendName为Express中的属性*/
    private String sendName;

    /*暂定receivName为Express中的属性*/
    private String receivName;

    /*暂定cancelReason是Express中的属性*/
    private String cancelReason;

    /*暂定secStatus是Express中的属性*/
    private String secStatus;
    /**
     * 揽件上门开始时间
     */
    private LocalDateTime startTime;
    /**
     * 揽件上门结束时间
     */
    private LocalDateTime endTime;

    private final Byte QUALIFIED_ACCEPTANCE = 1;
    private final Byte UNQUALIFIED_ACCEPTANCE = 0;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ContractDao contractDao;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ExpressDao expressDao;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private RegionDao regionDao;

    @ToString.Exclude
    @JsonIgnore
    private LogisticsAdaptor logisticsAdaptor;

    /**
     * 未发货
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte UNSHIPPED = 0;
    /**
     * 在途
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte SHIPPED = 1;
    /**
     * 签收
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte SIGNED = 2;
    /**
     * 取消
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte CANCELED = 3;
    /**
     * 拒收
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte REJECTED = 4;
    /**
     * 已退回
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte RETURNED = 5;
    /**
     * 丢失
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte LOST = 6;
    /**
     * 回收
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte RECEIVED = 7;
    /**
     * 破损
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte BROKEN = 8;

    /**
     * 状态和名称的对应
     */
    public static final Map<Byte, String> STATUSNAMES = new HashMap() {
        {
            put(UNSHIPPED, "未发货");
            put(SHIPPED, "在途");
            put(SIGNED, "签收");
            put(CANCELED, "取消");
            put(REJECTED, "拒收");
            put(RETURNED, "已退回");
            put(LOST, "丢失");
            put(RECEIVED, "回收");
            put(BROKEN, "破损");
        }
    };

    /**
     * 允许的状态迁移
     */
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>() {
        {
            put(UNSHIPPED, new HashSet<>() {
                {
                    add(SHIPPED);
                    add(CANCELED);
                    add(SIGNED);
                    add(REJECTED);
                }
            });
            put(SHIPPED, new HashSet<>() {
                {
                    add(SIGNED);
                    add(REJECTED);
                    add(LOST);
                }
            });
            put(REJECTED, new HashSet<>() {
                {
                    add(LOST);
                    add(RETURNED);
                }
            });
            put(RETURNED, new HashSet<>() {
                {
                    add(BROKEN);
                    add(RECEIVED);
                }
            });
        }
    };

    public Contract getContract() throws RuntimeException {
        if (Objects.isNull(this.contract) && Objects.nonNull(this.contractDao)) {
            log.debug("getShopLogistics: this.shopLogisticsId = {}", this.contractId);
            this.contract = this.contractDao.findById(this.shopId, this.contractId);
        }
        return this.contract;
    }

    public Region getSendRegion() {
        if (Objects.isNull(this.sendRegion) && Objects.nonNull(this.regionDao)) {
            this.sendRegion = this.regionDao.findById(this.sendRegionId);
        }
        return this.sendRegion;
    }

    public Region getReceivRegion() {
        if (Objects.isNull(this.receivRegion) && Objects.nonNull(this.regionDao)) {
            this.receivRegion = this.regionDao.findById(this.receivRegionId);
        }
        return this.receivRegion;
    }

    /**
     * 是否允许状态迁移
     *
     * @param status
     * @return
     */
    public boolean allowStatus(Byte status) {
        boolean ret = false;
        if (Objects.nonNull(status) && Objects.nonNull(this.status)) {
            Set<Byte> allowStatusSet = toStatus.get(this.status);
            if (Objects.nonNull(allowStatusSet)) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    /**
     * 获得当前状态名称
     *
     * @return
     */
    public String getStatusName() {
        return STATUSNAMES.get(this.status);
    }

    private void changeStatus(Byte status, UserDto user) {
        log.debug("changeStatus: id = {}, status = {}", this.id, status);
        if (!this.allowStatus(status)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "物流单", this.id, STATUSNAMES.get(this.status)));
        }
        this.setStatus(status);
        this.expressDao.save(this, user);
    }

    public void send(UserDto user, LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.changeStatus(SHIPPED, user);
        this.logisticsAdaptor.sendPackage(this.contract, billCode, orderCode);
    }

    public void cancel(UserDto user) {
        this.changeStatus(CANCELED, user);
        this.logisticsAdaptor.cancelPackage(contract, this);
    }

    public void getNewStatus() {
        Express newExpress = this.logisticsAdaptor.getPackage(contract, billCode);
        if (!Objects.equals(newExpress.getStatus(), status)) {
            changeStatus(newExpress.getStatus(), SYSTEM);
        }
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void confirm(Byte status, UserDto user) {
        if (Objects.equals(status, UNQUALIFIED_ACCEPTANCE)) {
            changeStatus(BROKEN, user);
        } else changeStatus(RECEIVED, user);
    }


    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /*暂定orderType是Express中的属性*/
    private String orderType;

    public Long getSendRegionId() {
        return sendRegionId;
    }

    public void setSendRegionId(Long sendRegionId) {
        this.sendRegionId = sendRegionId;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendMobile() {
        return sendMobile;
    }

    public void setSendMobile(String sendMobile) {
        this.sendMobile = sendMobile;
    }

    public Long getReceivRegionId() {
        return receivRegionId;
    }

    public void setReceivRegionId(Long receivRegionId) {
        this.receivRegionId = receivRegionId;
    }

    public String getReceivAddress() {
        return receivAddress;
    }

    public void setReceivAddress(String receivAddress) {
        this.receivAddress = receivAddress;
    }

    public String getReceivMobile() {
        return receivMobile;
    }

    public void setReceivMobile(String receivMobile) {
        this.receivMobile = receivMobile;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getReceivName() {
        return receivName;
    }

    public void setReceivName(String receivName) {
        this.receivName = receivName;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
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

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getShopId() {
        return shopId;
    }

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getSecStatus() {
        return secStatus;
    }

    public void setSecStatus(String secStatus) {
        this.secStatus = secStatus;
    }

    public void setSendRegion(Region sendRegion) {
        this.sendRegion = sendRegion;
    }

    public void setReceivRegion(Region receivRegion) {
        this.receivRegion = receivRegion;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public LogisticsAdaptor getLogisticsAdaptor() {
        return logisticsAdaptor;
    }

    public void setLogisticsAdaptor(LogisticsAdaptorFactory factory) throws RuntimeException{
        this.logisticsAdaptor = factory.createAdaptor(this.getContract().getLogistics());
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
