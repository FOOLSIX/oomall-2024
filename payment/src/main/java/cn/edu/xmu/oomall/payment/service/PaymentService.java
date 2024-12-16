//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.ChannelDao;
import cn.edu.xmu.oomall.payment.dao.LedgerDao;
import cn.edu.xmu.oomall.payment.dao.PayTransDao;
import cn.edu.xmu.oomall.payment.dao.AccountDao;
import cn.edu.xmu.oomall.payment.dao.bo.Ledger;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import cn.edu.xmu.oomall.payment.dao.bo.Account;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptor;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.dao.channel.vo.PostPayTransAdaptorVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.*;
import static cn.edu.xmu.oomall.payment.dao.bo.PayTrans.NEW;
/**
 * @author ych
 * task 2023-dgn1-004
 *
 * @modified by Chihua Ying
 * 2023-12-30
 * 增加 retrievePayments 的相关异常判断
 */

/**
 * 支付的服务
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final AccountDao accountDao;
    private final PayTransDao payTransDao;
    private final ChannelDao channelDao;

    /**
     * 创建一个支付交易
     * @param  payTrans 支付交易值对象
     * @param user          当前登录用户
     * @return 支付交易对象
     * @author Ming Qiu
     * <p>
     * date: 2022-11-01 19:18
     */
    public PostPayTransAdaptorVo createPayment(PayTrans payTrans, UserDto user) throws BusinessException {
        Account account = this.accountDao.findById(PLATFORM, payTrans.getAccountId());
        log.debug("createPayment: shop = {}", account);
        return account.createPayment(payTrans, user);
    }

    /**
     * modified By ych
     * Task 2023-dgn1-004
     * 查询支付交易
     * @param shopId 商铺id
     * @param channelId 支付渠道id
     * @param transNo 支付交易号
     * @param beginTime 交易起时间
     * @param endTime 交易止时间
     * @param status 状态
     * @param page 页
     * @param pageSize 每页数目
     * @return
     */
    public List<PayTrans> retrievePayments(Long shopId, Long channelId, String transNo, LocalDateTime beginTime, LocalDateTime endTime, Integer status, Integer page, Integer pageSize){
        if (this.channelDao.findById(channelId) == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "支付渠道", channelId));
        }
        if (PLATFORM.equals(shopId)){//==改为用equals
            return this.payTransDao.retrieveByChannelId(channelId, transNo, status, beginTime, endTime, page, pageSize);
        } else {
            List<Long> accounts = this.accountDao.retrieveByChannelId(channelId, 1, MAX_RETURN).stream().filter(account -> account.getShopId().equals(shopId)).map(Account::getId).collect(Collectors.toList());
            if(accounts.isEmpty()){
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺渠道", channelId));
            }
            return this.payTransDao.retrieveByAccountId(accounts, transNo, status, beginTime, endTime, page, pageSize);
        }
    }

    /**
     * 用outNo更新PayTrans对象
     * @param payTrans 更新值
     * @param user 操作用户
     */
    public void updatePaymentByOutNo(PayTrans payTrans, UserDto user){

        Optional<PayTrans> updatePayTrans = this.payTransDao.findByOutNo(payTrans.getOutNo());
        updatePayTrans.ifPresent(o -> {
            payTrans.setId(o.getId());
            this.payTransDao.save(payTrans, SYSTEM);
        });
    }

    /**
     * 分账
     * @param channelId 支付渠道id
     * @param timeEnd 分账截至时间
     * @return
     */
    public void divPayment(Long channelId, LocalDateTime timeEnd){
        Integer channelPage = 1, channelPageSize = MAX_RETURN;
        while (channelPageSize == MAX_RETURN){
            List<Account> accounts = this.accountDao.retrieveByChannelId(channelId, channelPage, channelPageSize);
            channelPageSize = accounts.size();
            channelPage += 1;

            for (Account account : accounts) {
                Integer transPage = 1, transPageSize = MAX_RETURN;
                while (transPageSize == MAX_RETURN) {
                    List<PayTrans> payTransList = this.payTransDao.retrieveSuccessPayTransBySuccessTimeBefore(account.getId(), timeEnd, transPage, transPageSize);
                    transPage += 1;
                    transPageSize = payTransList.size();

                    for (PayTrans payTrans : payTransList) {
                        payTrans.divide();
                    }
                }
            }
        }
    }


    /**
     * modified By ych
     * task 2023-dgn1-004
     * 查询支付
     * @param shopId 商铺id
     * @param id 支付id
     * @return
     */
    public PayTrans findPayment(Long shopId, Long id){
        PayTrans trans = this.payTransDao.findById(shopId, id);
        trans.check();
        return trans;
    }


    /**
     * 取消支付
     * @param shopId 商铺id
     * @param id 支付id
     * @param user 操作者
     */
    public void cancelPayment(Long shopId, Long id, UserDto user){
        PayTrans payTrans = this.payTransDao.findById(shopId, id);
        payTrans.cancel(user);
    }

    public void adjustPayment(Long shopId, Long id, UserDto user) {
        PayTrans payTrans = this.payTransDao.findById(shopId,id);
        payTrans.adjust(user);
    }
}
