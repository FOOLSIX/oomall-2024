//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.dao.bo.Transaction;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.mapper.generator.PayTransPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.po.PayTransPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.PayTransPoExample;
import cn.edu.xmu.oomall.payment.mapper.manual.PayTransPoManualExample;
import cn.edu.xmu.oomall.payment.mapper.manual.PayTransPoManualMapper;
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

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Repository
@Slf4j
public class PayTransDao extends TransactionDao {

    private final AccountDao accountDao;
    private final RefundTransDao refundTransDao;
    private final DivPayTransDao divPayTransDao;
    private final LedgerDao ledgerDao;
    private final PayTransPoMapper payTransPoMapper;
    private final PayTransPoManualMapper payTransPoManualMapper;
    private final PayAdaptorFactory factory;

    @Autowired
    @Lazy
    public PayTransDao(AccountDao accountDao, RefundTransDao refundTransDao, DivPayTransDao divPayTransDao, LedgerDao ledgerDao,cn.edu.xmu.oomall.payment.mapper.generator.PayTransPoMapper payTransPoMapper, PayTransPoManualMapper payTransPoManualMapper, PayAdaptorFactory factory) {
        this.accountDao = accountDao;
        this.refundTransDao = refundTransDao;
        this.divPayTransDao = divPayTransDao;
        this.payTransPoMapper = payTransPoMapper;
        this.payTransPoManualMapper = payTransPoManualMapper;
        this.ledgerDao = ledgerDao;
        this.factory = factory;
    }

    /**
    *增加缺失的ledgerDao
    *@Author 37220222203851
    */
    private PayTrans build(PayTransPo po) {
        PayTrans ret;
        ret = CloneFactory.copy(new PayTrans(), po);
        ret.setRefundTransDao(this.refundTransDao);
        ret.setAccountDao(this.accountDao);
        ret.setDivPayTransDao(this.divPayTransDao);
        ret.setLedgerDao(this.ledgerDao);
        ret.setPayAdaptor(this.factory);
        ret.setPayTransDao(this);
        return ret;
    }


    /**
     * 由id返回支付交易对象
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 23:31
     * @param id
     * @return
     * @throws RuntimeException
     * @throws BusinessException 无此对象
     */
    public PayTrans findById(Long shopId, Long id) throws RuntimeException{

        PayTransPo po = payTransPoMapper.selectByPrimaryKey(id);
        if (Objects.isNull(po)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "支付交易", id));
        }
        if (!PLATFORM.equals(shopId) && !shopId.equals(po.getShopId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "支付交易", id, shopId));
        }
        return build(po);
    }

    /**
     * 按照accountIds查询
     * @param accountIds account id list
     * @param transNo 渠道交易号
     * @param status 状态
     * @param beginTime 交易起时间
     * @param endTime 交易止时间
     * @param page 页
     * @param pageSize 每页数目
     * @return
     * @throws RuntimeException
     */
    public List<PayTrans> retrieveByAccountId(List<Long> accountIds, String transNo, Integer status, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) throws RuntimeException{

        PayTransPoExample example = new PayTransPoExample();
        PayTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andAccountIdIn(accountIds);
        if (Objects.nonNull(transNo)) {
            criteria.andTransNoEqualTo(transNo);
        }
        if (Objects.nonNull(status)) {
            criteria.andStatusEqualTo(status.byteValue());
        }
        if (Objects.nonNull(beginTime)) {
            criteria.andTimeBeginGreaterThanOrEqualTo(beginTime);
        }
        if (Objects.nonNull(endTime)) {
            criteria.andTimeExpireLessThanOrEqualTo(endTime);
        }

        return retrievePayTrans(example, page, pageSize);
    }

    public List<PayTrans> retrieveByChannelId(Long channelId, String transNo, Integer status, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) throws RuntimeException{

        PayTransPoManualExample example = new PayTransPoManualExample();
        PayTransPoManualExample.Criteria criteria = example.createCriteria();
        criteria.andChannelIdEqualTo(channelId);
        if (Objects.nonNull(transNo)) {
            criteria.andTransNoEqualTo(transNo);
        }
        if (Objects.nonNull(status)) {
            criteria.andStatusEqualTo(status.byteValue());
        }
        if (Objects.nonNull(beginTime)) {
            criteria.andTimeBeginGreaterThanOrEqualTo(beginTime);
        }
        if (Objects.nonNull(endTime)) {
            criteria.andTimeExpireLessThanOrEqualTo(endTime);
        }

        PageHelper.startPage(page, pageSize, false);
        List<PayTransPo> poList = this.payTransPoManualMapper.selectByExample(example);
        return poList.stream().map(po -> this.build(po)).collect(Collectors.toList());

    }

    /**
     * 查询支付交易
     * @param example 查询条件
     * @param page 页
     * @param pageSize 每页数目
     * @return 支付交易
     */
    private List<PayTrans> retrievePayTrans(PayTransPoExample example, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize, false);
        List<PayTransPo> poList = this.payTransPoMapper.selectByExample(example);
        return poList.stream().map(po -> this.build(po)).collect(Collectors.toList());
    }

    public Optional<PayTrans> findByOutNo(String outNo){
        PayTrans ret = null;
        PayTransPoExample example = new PayTransPoExample();
        PayTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andOutNoEqualTo(outNo);
        List<PayTransPo> poList = this.payTransPoMapper.selectByExample(example);

        if(!poList.isEmpty()) {
            ret = build(poList.get(0));
        }
        return Optional.ofNullable(ret);
    }

    /**
     * 查询支付成功的交易
     * @param endTime successTime在endTime之前的
     * @param accountId 支付渠道
     * @param page 页
     * @param pageSize 每页数目
     * @return
     */
    public List<PayTrans> retrieveSuccessPayTransBySuccessTimeBefore(Long accountId, LocalDateTime endTime, Integer page, Integer pageSize){

        PayTransPoExample example = new PayTransPoExample();
        PayTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andAccountIdEqualTo(accountId);
        criteria.andStatusEqualTo(PayTrans.SUCCESS);
        criteria.andSuccessTimeLessThanOrEqualTo(endTime);
        PageHelper.startPage(page, pageSize, false);
        List<PayTransPo> poList = this.payTransPoMapper.selectByExample(example);
        return poList.stream().map(this::build).collect(Collectors.toList());
    }

    @Override
    protected Integer save(Transaction trans) {
        PayTrans obj;
        if (trans instanceof PayTrans) {
            obj = (PayTrans) trans;
        }else{
            throw new IllegalArgumentException("PayTransDao.save: trans should be Paytrans.");
        }
        PayTransPo po = CloneFactory.copy(new PayTransPo(), obj);
        log.debug("save: po = {}", po);
        return payTransPoMapper.updateByPrimaryKeySelective(po);
    }

    @Override
    protected Transaction insert(Transaction trans) {
        log.debug("insert: obj = {}", trans);
        PayTrans obj;
        if (trans instanceof PayTrans) {
            obj = (PayTrans) trans;
        }else{
            throw new IllegalArgumentException("PayTransDao.save: trans should be Paytrans.");
        }
        PayTransPo po = CloneFactory.copy(new PayTransPo(), obj);
        log.debug("insert: po = {}", po);
        payTransPoMapper.insertSelective(po);
        obj.setId(po.getId());
        return obj;
    }
}
