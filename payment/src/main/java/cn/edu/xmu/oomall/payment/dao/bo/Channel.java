//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.aop.CopyTo;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.ChannelDao;
import cn.edu.xmu.oomall.payment.dao.AccountDao;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptor;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.mapper.generator.po.ChannelPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 商户支付渠道
 */
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({ChannelPo.class})
@CopyTo(ChannelPo.class)
public class Channel extends OOMallObject implements Serializable {

    /**
     * 有效
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte VALID = 0;
    /**
     * 无效
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte INVALID = 1;

    /**
     * 状态和名称的对应
     */
    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(VALID, "有效");
            put(INVALID, "无效");
        }
    };

    /**
     * 允许的状态迁移
     */
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(VALID, new HashSet<>(){
                {
                    add(INVALID);
                }
            });
            put(INVALID, new HashSet<>(){
                {
                    add(VALID);
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     * @author Ming Qiu
     * <p>
     * date: 2022-11-13 0:25
     * @param status
     * @return
     */
    public boolean allowStatus(Byte status){
        boolean ret = false;

        if (null != status && null != this.status){
            Set<Byte> allowStatusSet = toStatus.get(this.status);
            if (null != allowStatusSet) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    /**
     * 平台应用id
     */
    @Setter
    @Getter
    private String spAppid;

    /**
     * 渠道名称
     */
    @Setter
    @Getter
    private String name;

    /**
     * 平台商户号
     */
    @Setter
    @Getter
    private String spMchid;

    /**
     * 开始时间
     */
    @Setter
    @Getter
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @Setter
    @Getter
    private LocalDateTime endTime;

    /**
     *  状态
     */
    @Setter
    @Getter
    private Byte status;

    /**
     * 适配对象名
     */
    @Setter
    @Getter
    private String beanName;

    /**
     * 通知地址
     */
    @Setter
    @Getter
    private String notifyUrl;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private AccountDao accountDao;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private ChannelDao channelDao;

    @ToString.Exclude
    @JsonIgnore
    private PayAdaptor payAdaptor;

    public void setPayAdaptor(PayAdaptorFactory factory){
        this.payAdaptor = factory.createPayAdaptor(this);
    }

    /**
     * 创建一个商铺支付渠道
     * @param account 商铺收款账号
     * @param user 操作者
     * @return
     */
    public Account createAccount(Account account, UserDto user){
        account.setStatus(Account.INVALID);
        account.setChannelId(this.getId());
        Account newOne = this.accountDao.insert(account, user);
        this.payAdaptor.createChannel(account);
        return newOne;
    }

    /**
     * 使支付渠道有效
     * @param user 操作者
     * @return 删除的Redis Key
     */
    public String valid(UserDto user){
        return this.changeStatus(Channel.VALID, user);
    }

    /**
     * 使支付渠道无效
     * @param user 操作者
     * @return 删除的Redis Key
     */
    public String invalid(UserDto user){
        return this.changeStatus(Channel.INVALID, user);
    }

    /**
     * 修改支付渠道的状态
     * @param status 修改的状态
     * @param user 登录用户
     */
    private String changeStatus(Byte status, UserDto user){
        if (!this.allowStatus(status)){
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "支付渠道", this.id, Channel.STATUSNAMES.get(this.status)));
        }

        if(Objects.isNull(this.channelDao)){
            throw new IllegalArgumentException("Channel.changeStatus: channelDao is null");
        }
        Channel newChannel = new Channel();
        newChannel.setId(this.id);
        newChannel.setStatus(status);
        return this.channelDao.save(newChannel, user);
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
