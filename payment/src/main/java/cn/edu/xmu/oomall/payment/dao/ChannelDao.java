//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.dao.bo.Channel;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.mapper.generator.ChannelPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.po.ChannelPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.ChannelPoExample;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ChannelDao {
    public static final String KEY = "C%d";

    private final RedisUtil redisUtil;
    private final ChannelPoMapper channelPoMapper;
    private final AccountDao accountDao;
    private final PayAdaptorFactory factory;

    @Autowired
    @Lazy
    public ChannelDao(RedisUtil redisUtil, ChannelPoMapper channelPoMapper, AccountDao accountDao, PayAdaptorFactory factory) {
        this.redisUtil = redisUtil;
        this.channelPoMapper = channelPoMapper;
        this.accountDao = accountDao;
        this.factory = factory;
    }

    private Channel build(ChannelPo po, Optional<String> redisKey) {
        Channel channel = CloneFactory.copy(new Channel(), po);
        //永不过期
        redisKey.ifPresent(key -> redisUtil.set(key, channel, -1));
        this.build(channel);
        return channel;
    }

    private Channel build(Channel bo) {
        bo.setAccountDao(this.accountDao);
        bo.setChannelDao(this);
        bo.setPayAdaptor(this.factory);
        return bo;
    }

    /**
     * 按照主键获得对象
     *
     * @param id
     * @return
     * @throws RuntimeException
     * @author Ming Qiu
     * <p>
     * date: 2022-11-20 11:44
     */
    public Channel findById(Long id) {
        log.debug("findObjById: id = {}", id);
        String key = String.format(KEY, id);
        Channel channel = (Channel) redisUtil.get(key);
        if (Objects.nonNull(channel)) {
            this.build(channel);
            return channel;
        } else {
            ChannelPo po = this.channelPoMapper.selectByPrimaryKey(id);
            if (Objects.isNull(po)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "支付渠道", id));
            }
            return this.build(po, Optional.of(key));
        }
    }


    /**
     * 更新支付渠道的状态
     *
     * @param channel channel对象
     * @param user    操作者
     */
    public String save(Channel channel, UserDto user) {
        channel.setModifier(user);
        channel.setGmtModified(LocalDateTime.now());
        ChannelPo po = CloneFactory.copy(new ChannelPo(), channel);
        int ret = channelPoMapper.updateByPrimaryKeySelective(po);
        if (0 == ret) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "支付渠道", po.getId()));
        }
        return String.format(ChannelDao.KEY, po.getId());
    }

}
