//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.payment.dao.*;
import cn.edu.xmu.oomall.payment.mapper.generator.po.LedgerPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 *  台账
 *  存储的数据均为支付渠道获取的数据
 */
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@CopyFrom({LedgerPo.class})
@CopyTo({LedgerPo.class})
public class Ledger extends OOMallObject {

    /**
     * 商城内部交易号
     */
    @Setter
    @Getter
    private String outNo;

    /**
     * 支付渠道交易号
     */
    @Setter
    @Getter
    private String transNo;

    /**
     * 金额
     */
    @Setter
    @Getter
    private Long amount;

    /**
     * 对账时间
     */
    @Setter
    @Getter
    private LocalDateTime checkTime;

    /**
     * 台账所属商铺渠道
     */
    @ToString.Exclude
    @JsonIgnore
    private Account account;

    @Setter
    @Getter
    private Long accountId;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private AccountDao accountDao;

    public Account getAccount(){
        if (Objects.isNull(this.account) && !Objects.isNull(this.accountDao)) {
            this.account = this.accountDao.findById(PLATFORM, this.accountId);
        }
        return this.account;
    }

    /**
     * 台账所属渠道, 如果account为null，此属性应该有值
     */
    @ToString.Exclude
    @JsonIgnore
    private Channel channel;

    @Setter
    @Getter
    private Long channelId;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ChannelDao channelDao;

    public Channel getChannel(){
        if (this.channel.equals(null)) {
            if (!Objects.isNull(this.channelId) && !Objects.isNull(this.channelDao)) {
                this.channel = this.channelDao.findById(this.channelId);
            }else{
                this.channel = this.getAccount().getChannel();
            }
        }
        return this.channel;
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
