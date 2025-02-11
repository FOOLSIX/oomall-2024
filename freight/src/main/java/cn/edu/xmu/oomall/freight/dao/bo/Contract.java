package cn.edu.xmu.oomall.freight.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.dao.LogisticsDao;

import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptor;
import cn.edu.xmu.oomall.freight.mapper.po.ContractPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true, doNotUseGetters = true)
@Getter
@Setter
@Slf4j
@CopyFrom({ContractPo.class})
@CopyTo({ContractPo.class})
public class Contract extends OOMallObject implements Serializable {
    public static Byte VALID = 0;
    public static Byte INVALID = 1;

    public void setValidity(Byte status){
        this.invalid = status;
    }

    private Long logisticsId;

    @JsonIgnore
    @ToString.Exclude
    @Setter
    private LogisticsDao logisticsDao;

    @JsonIgnore
    @ToString.Exclude
    private Logistics logistics;

    public Logistics getLogistics(){
        if (Objects.isNull(this.logistics) && Objects.nonNull(this.logisticsDao)){
            this.logistics = this.logisticsDao.findById(this.logisticsId);
        }
        return this.logistics;
    }

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ExpressDao expressDao;

    @ToString.Exclude
    @JsonIgnore
    private LogisticsAdaptor logisticsAdaptor;

    public void setLogisticsAdaptor(LogisticsAdaptorFactory factory) throws RuntimeException{
        this.logisticsAdaptor = factory.createAdaptor(this.getLogistics());
    }

    public Express createExpress(Long shopId, Express express, UserDto user){
        express.setShopId(shopId);
        express.setStatus(Express.UNSHIPPED);
        log.debug("createExpress: logisticsAdaptor = {}", logisticsAdaptor);
        PostCreatePackageAdaptorDto adaptorDto = this.logisticsAdaptor.createPackage(this, express);
        if (adaptorDto.getBillCode() != null) {
            express.setBillCode(adaptorDto.getBillCode());
        }
        this.expressDao.insert(express, user);
        log.debug("createExpress: dto = {}", adaptorDto);
        return express;
    }

    //假设account为属性
    private String account;

    private Long shopId;

    private String secret;

    private Byte invalid;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer priority;

    private Integer quota;

    private Long warehouseId;

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
