package cn.edu.xmu.oomall.payment.service;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.ChannelDao;
import cn.edu.xmu.oomall.payment.dao.AccountDao;
import cn.edu.xmu.oomall.payment.dao.bo.Channel;
import cn.edu.xmu.oomall.payment.dao.bo.Account;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static java.lang.Math.min;

/**
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelDao channelDao;
    private final AccountDao accountDao;
    private final RedisUtil redisUtil;

    /**
     * 有效支付渠道
     * @param channelId 支付渠道ID
     * @param user 登录用户
     */
    public void validChannel(Long channelId, UserDto user){
        Channel channel = this.channelDao.findById(channelId);
        String key = channel.valid(user);
        this.redisUtil.del(key);
    }

    /**
     * 无效支付渠道
     * @param channelId 支付渠道ID
     * @param user 登录用户
     */
    public void invalidChannel(Long channelId, UserDto user){
        Channel channel = this.channelDao.findById(channelId);
        String key = channel.invalid(user);
        this.redisUtil.del(key);
    }


    /**
     * 获得有效的支付渠道
     * @param shopId 商铺Id
     * @param page 页码
     * @param pageSize 页大小
     */
    public List<Account> retrieveValidAccount(Long shopId, Integer page, Integer pageSize){
        //获得所有的Account，再判断Account和Channel的状态是否都有效
        List<Account> accounts = this.accountDao.retrieveByShopId(shopId);
        return accounts.stream()
                    .filter(account -> account.getStatus().equals(Account.VALID) && account.getChannel().getStatus().equals(Channel.VALID))
                    .skip(pageSize * (page - 1)).limit(page*pageSize).collect(Collectors.toList());
    }

    /**
     * 获得商铺的所有支付渠道(有效和无效)
     *
     * @param shopId 商户id
     * @param page 页码
     * @param pageSize 页大小
     * @return
     * @Modifier 37720222205040
     */
    public List<Account> retrieveAccount(Long shopId, Integer page, Integer pageSize) {
        List<Account> accountList = this.accountDao.retrieveByShopId(shopId);
        return accountList.subList((page -1)*pageSize, min(page*pageSize,accountList.size()) );
    }

    /**
     * 签约支付渠道
     * @param id 商户id
     * @param account 商户渠道对象
     * @param user 登录用户
     * @return
     */
    public Account createAccount(Long id, Account account, UserDto user){
        Channel channel = this.channelDao.findById(id);
        return channel.createAccount(account, user);
    }

    /**
     * 查询商铺的某一收款账号
     * @param shopId 商户id
     * @param id 渠道id
     * @return
     */
    public Account findAccountById(Long shopId, Long id){
        return this.accountDao.findById(shopId, id);
    }

    /**
     *解约店铺的账户
     * @param shopId 商铺id
     * @param id 渠道id
     */
    public void cancelAccount(Long shopId, Long id, UserDto user){
        //先找到account，判断是否存在和有效
        Account account = this.accountDao.findById(shopId, id);
        String key = account.cancel(user);
        this.redisUtil.del(key);
    }

    /**
     * 有效商铺收款账号
     * @param shopId 商铺id
     * @param id 商铺收款账号id
     * @param user 操作者
     */
    public void validAccount(Long shopId, Long id, UserDto user){
        Account account = this.accountDao.findById(shopId, id);
        String key = account.valid(user);
        this.redisUtil.del(key);
    }

    /**
     * 无效商铺收款账号
     * @param shopId 商铺id
     * @param id 商铺收款账号id
     * @param user 操作者
     */
    public void invalidAccount(Long shopId, Long id, UserDto user){
        Account account = this.accountDao.findById(shopId, id);
        String key = account.invalid(user);
        this.redisUtil.del(key);
    }
}

