//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.controller.vo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.oomall.payment.dao.bo.Account;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({Account.class})
public class FullAccountVo {
    private Long id;
    /**
     * 子商户号
     */
    private String subMchid;
    /**
     * 状态
     */
    private Byte status;
    /**
     * 支付渠道
     */
    private SimpleChannelVo channel;

    private IdNameTypeVo creator;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private IdNameTypeVo modifier;

    public FullAccountVo(Account account){
        super();
        this.channel = CloneFactory.copy(new SimpleChannelVo(), account.getChannel());
        this.creator = IdNameTypeVo.builder().id(account.getCreatorId()).name(account.getCreatorName()).build();
        this.modifier = IdNameTypeVo.builder().id(account.getModifierId()).name(account.getModifierName()).build();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public SimpleChannelVo getChannel() {
        return channel;
    }

    public IdNameTypeVo getCreator() {
        return creator;
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

}
