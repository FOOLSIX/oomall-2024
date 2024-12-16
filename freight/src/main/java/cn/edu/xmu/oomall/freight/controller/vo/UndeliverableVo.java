package cn.edu.xmu.oomall.freight.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({Undeliverable.class})
public class UndeliverableVo {
    public UndeliverableVo(Undeliverable undeliverable){
        this();
        this.setRegion1(IdNameTypeVo.builder().id(undeliverable.getRegionId())
                .name(undeliverable.getRegion().getName()).build());
        this.setCreator(IdNameTypeVo.builder().id(undeliverable.getCreatorId())
                .name(undeliverable.getCreatorName()).build());
        this.setModifier(IdNameTypeVo.builder().id(undeliverable.getModifierId())
                .name(undeliverable.getModifierName()).build());
    }

    private Long id;

    private IdNameTypeVo region;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private IdNameTypeVo creator;

    private IdNameTypeVo modifier;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdNameTypeVo getRegion() {
        return region;
    }

    public void setRegion1(IdNameTypeVo region) {
        this.region = region;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public IdNameTypeVo getCreator() {
        return creator;
    }

    public void setCreator(IdNameTypeVo creator) {
        this.creator = creator;
    }

    public IdNameTypeVo getModifier() {
        return modifier;
    }

    public void setModifier(IdNameTypeVo modifier) {
        this.modifier = modifier;
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
