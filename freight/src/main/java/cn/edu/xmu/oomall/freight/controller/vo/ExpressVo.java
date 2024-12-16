package cn.edu.xmu.oomall.freight.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * 2023-dgn3-009
 * @author huangzian
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({Express.class})
public class ExpressVo {
    private Long id;
    private String billCode;
    private IdNameTypeVo logistics;
    private WarehouseVo shipper;
    private WarehouseVo receiver;
    private Byte status;
    private IdNameTypeVo creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameTypeVo modifier;

    public ExpressVo(Express express){
        super();
        CloneFactory.copy(this, express);
        this.creator = IdNameTypeVo.builder().id(express.getCreatorId()).name(express.getCreatorName()).build();
        this.modifier = IdNameTypeVo.builder().id(express.getModifierId()).name(express.getModifierName()).build();
        this.shipper = WarehouseVo.builder().name(express.getSendName()).mobile(express.getSendMobile())
                .address(express.getSendAddress()).regionId(express.getSendRegionId()).build();
        this.receiver = WarehouseVo.builder().name(express.getReceivName()).mobile(express.getReceivMobile())
                .address(express.getReceivAddress()).regionId(express.getReceivRegionId()).build();
        this.logistics = IdNameTypeVo.builder().id(express.getContractId())
                .name(express.getContract().getLogistics().getName()).build();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public IdNameTypeVo getLogistics() {
        return logistics;
    }

    public void setLogistics(IdNameTypeVo logistics) {
        this.logistics = logistics;
    }

    public WarehouseVo getShipper() {
        return shipper;
    }

    public void setShipper(WarehouseVo shipper) {
        this.shipper = shipper;
    }

    public WarehouseVo getReceiver() {
        return receiver;
    }

    public void setReceiver(WarehouseVo receiver) {
        this.receiver = receiver;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public IdNameTypeVo getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeVo creator) {
        this.creator = creator;
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

    public IdNameTypeVo getModifier() {
        return modifier;
    }

    public void setModifier(IdNameTypeVo modifier) {
        this.modifier = modifier;
    }

}
