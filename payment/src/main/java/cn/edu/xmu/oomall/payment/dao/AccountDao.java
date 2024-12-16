//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.payment.dao.bo.Account;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.mapper.generator.AccountPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.po.AccountPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.AccountPoExample;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * Account的dao对象
 */
@Repository
@Slf4j
public class AccountDao {

    private static final String ACCOUNTKEY = "A%d";
    private static final String SHOPACCOUNTKEY = "SA%d";

    @Value("${oomall.payment.account.timeout}")
    private long timeout;

    private final AccountPoMapper accountPoMapper;

    private final RedisUtil redisUtil;

    private final ChannelDao channelDao;

    private final PayTransDao payTransDao;

    private final PayAdaptorFactory factory;

    @Autowired
    @Lazy
    public AccountDao(AccountPoMapper accountPoMapper, RedisUtil redisUtil, ChannelDao channelDao, PayTransDao payTransDao, PayAdaptorFactory factory) {
        this.accountPoMapper = accountPoMapper;
        this.redisUtil = redisUtil;
        this.channelDao = channelDao;
        this.payTransDao = payTransDao;
        this.factory = factory;
    }


    /**
     * 获得bo对象
     *
     * @param po
     * @param redisKey
     * @return
     * @author Ming Qiu
     * <p>
     * date: 2022-11-20 11:46
     */
    private Account build(AccountPo po, Optional<String> redisKey) {
        Account ret = CloneFactory.copy(new Account(), po);
        redisKey.ifPresent(key -> redisUtil.set(key, ret, timeout));
        this.build(ret);
        return ret;
    }

    /**
     * 把bo中设置dao
     *
     * @param bo
     * @author Ming Qiu
     * <p>
     * date: 2022-11-20 11:46
     */
    private Account build(Account bo) {
        bo.setAccountDao(this);
        bo.setChannelDao(this.channelDao);
        bo.setPayAdaptor(this.factory);
        bo.setAccountDao(this);
        bo.setPayTransDao(this.payTransDao);
        return bo;
    }

    /**
     * 由id获得对象
     * @param shopId 商铺id
     * @param id 商铺渠道id
     * @return 商铺渠道对象
     * <p>
     * date: 2022-11-07 0:38
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    /**
     * 2023-dgn1-006
     *
     * @author huangzian
     * 修改bug
     */
    public Account findById(Long shopId, Long id) {
        Account ret = null;
        String key = String.format(ACCOUNTKEY, id);
        ret = (Account) redisUtil.get(key);
        if (!Objects.isNull(ret)) {
            if (!shopId.equals(ret.getShopId()) && !PLATFORM.equals(shopId)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "支付渠道", id, shopId));
            }
            this.build(ret);
        } else {
            AccountPo po = accountPoMapper.selectByPrimaryKey(id);
            if (Objects.isNull(po)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺支付渠道", id));
            }
            if (!shopId.equals(po.getShopId()) && !PLATFORM.equals(shopId)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "支付渠道", id, shopId));
            }
            ret = this.build(po, Optional.of(key));
        }
        log.debug("findObjById: id = " + id + " ret = " + ret);
        return ret;
    }

    /**
     * 按照id更新商户收款账户
     *
     * @param account
     * @return 要删除的key
     * @author Ming Qiu
     * <p>
     * date: 2022-11-10 6:07
     */
    public String save(Account account, UserDto user) {
        account.setModifier(user);
        account.setGmtModified(LocalDateTime.now());
        this.build(account);
        AccountPo po = CloneFactory.copy(new AccountPo(), account);
        Integer ret = accountPoMapper.updateByPrimaryKeySelective(po);
        if (0 == ret) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺支付渠道", account.getId()));
        }
        return String.format(ACCOUNTKEY, account.getId());
    }


    /**
     * 获得商铺所有的收款账号
     *
     * @param shopId   商铺Id
     */
    public List<Account> retrieveByShopId(Long shopId){
        String key = String.format(SHOPACCOUNTKEY, shopId);
        List<Long> accountIds = (List<Long>) redisUtil.get(key);
        if (Objects.nonNull(accountIds)) {
            return accountIds.stream().map(id -> this.findById(shopId, id)).collect(Collectors.toList());
        }else {
            AccountPoExample accountPoExample = new AccountPoExample();
            AccountPoExample.Criteria criteria = accountPoExample.createCriteria();
            criteria.andShopIdEqualTo(shopId);
            List<Account> accountList = this.retrieve(accountPoExample, 1, MAX_RETURN);
            accountIds = accountList.stream().map(Account::getId).collect(Collectors.toList());
            redisUtil.set(key, (Serializable) accountIds, timeout);
            return accountList;
        }
    }

    /**
     * 获得支付渠道的商铺签约的账户
     *
     * @param channelId 支付渠道Id
     * @param page      页码
     * @param pageSize  页大小
     */
    public List<Account> retrieveByChannelId(Long channelId, Integer page, Integer pageSize) throws RuntimeException {
        AccountPoExample accountPoExample = new AccountPoExample();
        AccountPoExample.Criteria criteria = accountPoExample.createCriteria();
        criteria.andChannelIdEqualTo(channelId);
        return retrieve(accountPoExample, page, pageSize);
    }

    /**
     * 查询商户收款账号
     *
     * @param accountPoExample 查询条件
     * @param page                 页
     * @param pageSize             分页
     * @return
     */
    private List<Account> retrieve(AccountPoExample accountPoExample, Integer page, Integer pageSize) {
        List<Account> ret;
        PageHelper.startPage(page, pageSize, false);
        List<AccountPo> accountPoList = this.accountPoMapper.selectByExample(accountPoExample);
        ret = accountPoList.stream()
                .map(po -> this.build(po, Optional.ofNullable(null)))
                .collect(Collectors.toList());
        return ret;
    }

    /**
     * 插入account对象
     * 修改约束名
     * Date 2024/11/23 8:45
     * @Author 37220222203851
     * @param bo   渠道信息
     * @param user 登录用户
     */
    public Account insert(Account bo, UserDto user) {
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());
        bo.setId(null);
        this.build(bo);
        AccountPo accountPo = CloneFactory.copy(new AccountPo(), bo);
        try {
            accountPoMapper.insertSelective(accountPo);
        } catch (DataAccessException e) {
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
                //错误信息
                String errMsg = ((java.sql.SQLIntegrityConstraintViolationException) cause).getMessage();
                //根据约束名称定位是那个字段
                if (errMsg.indexOf("payment_account_shop_id_channel_id_uindex") != -1) {
                    throw new BusinessException(ReturnNo.PAY_CHANNEL_EXIST, String.format(ReturnNo.PAY_CHANNEL_EXIST.getMessage(), bo.getShopId(), bo.getChannelId()));
                }
            }
        }
        bo.setId(accountPo.getId());
        return bo;
    }
}
