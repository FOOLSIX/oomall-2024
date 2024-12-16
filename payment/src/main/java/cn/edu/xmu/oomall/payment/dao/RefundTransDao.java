//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.dao.bo.RefundTrans;
import cn.edu.xmu.oomall.payment.dao.bo.Transaction;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.mapper.generator.RefundTransPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPoExample;
import cn.edu.xmu.oomall.payment.mapper.manual.RefundTransPoManualExample;
import cn.edu.xmu.oomall.payment.mapper.manual.RefundTransPoManualMapper;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Repository
@Slf4j
public class RefundTransDao extends TransactionDao {
    private final PayTransDao payTransDao;
    private final RefundTransPoMapper refundTransPoMapper;
    private final AccountDao accountDao;
    private final DivRefundTransDao divRefundTransDao;
    private final LedgerDao ledgerDao;
    private final RefundTransPoManualMapper refundTransPoManualMapper;
    private final PayAdaptorFactory factory;

    /**
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    @Autowired
    @Lazy
    public RefundTransDao(PayTransDao payTransDao, RefundTransPoMapper refundTransPoMapper, AccountDao accountDao, DivRefundTransDao divRefundTransDao, LedgerDao ledgerDao, RefundTransPoManualMapper refundTransPoManualMapper, PayAdaptorFactory factory) {
        this.payTransDao = payTransDao;
        this.refundTransPoMapper = refundTransPoMapper;
        this.accountDao = accountDao;
        this.divRefundTransDao = divRefundTransDao;
        this.ledgerDao = ledgerDao;
        this.refundTransPoManualMapper = refundTransPoManualMapper;
        this.factory = factory;
    }

    /**
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    private RefundTrans build(RefundTransPo po) {
        RefundTrans ret;
        ret = CloneFactory.copy(new RefundTrans(), po);
        ret.setPayTransDao(this.payTransDao);
        ret.setAccountDao(this.accountDao);
        ret.setDivRefundTransDao(this.divRefundTransDao);
        ret.setLedgerDao(this.ledgerDao);
        ret.setRefundTransDao(this);
        ret.setPayAdaptor(this.factory);
        return ret;
    }

    /**
     * 根据支付交易的id，查找对应的退款交易
     *
     * @param id 支付交易id
     * @param page 页
     * @param pageSize 每页数目
     * @return 退款交易对象， 无则返回null
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 21:56
     */
    public List<RefundTrans> retrieveByPayTransId(Long id, Integer page, Integer pageSize) {
        RefundTransPoExample example = new RefundTransPoExample();
        RefundTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andPayTransIdEqualTo(id);
        return retrieveRefundTrans(example, page, pageSize);
    }

    /**
     * 根据id返回退款交易对象
     *
     * @param id     退款id】
     * @param shopId 店铺id
     * @return
     * @throws BusinessException 无此对象
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 23:27
     */
    public RefundTrans findById(Long shopId, Long id) {
        if (null == id || null == shopId) {
            throw new IllegalArgumentException("RefundTransDao.findById: shopId or id is null");
        }
        RefundTransPo po = this.refundTransPoMapper.selectByPrimaryKey(id);
        if (null == po) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "退款交易", id));
        }
        if (!shopId.equals(PLATFORM) && !shopId.equals(po.getShopId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "退款交易", id, shopId));
        }
        return build(po);
    }

    /**
     * 查询退款交易
     * @param accountIds 支付渠道
     * @param transNo 渠道交易号
     * @param beginTime 交易时间起
     * @param endTime 交易时间止
     * @param status 状态
     * @return
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    public List<RefundTrans> retrieveByAccountIdAndTransNo(List<Long> accountIds, String transNo, LocalDateTime beginTime, LocalDateTime endTime, Byte status, Integer page, Integer pageSize) {
        //根据支付完成时间来筛选交易
        if(accountIds.isEmpty()) {
            throw new BusinessException(ReturnNo.PAY_CHANNEL_INVALID,ReturnNo.PAY_CHANNEL_INVALID.getMessage());
        }

        RefundTransPoExample example = new RefundTransPoExample();
        RefundTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andAccountIdIn(accountIds);

        if (null != transNo){
            criteria.andTransNoEqualTo(transNo);
        }
        if (null != beginTime){
            criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        }
        if (null != endTime){
            criteria.andGmtCreateLessThanOrEqualTo(endTime);
        }
        if (null != status){
            criteria.andStatusEqualTo(status);
        }

        return retrieveRefundTrans(example, page, pageSize);
    }

    /**
     * 查询退款交易
     * @param channelId 支付渠道id
     * @param transNo 渠道交易号
     * @param beginTime 交易时间起
     * @param endTime 交易时间止
     * @param status 状态
     * @return
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    public List<RefundTrans> retrieveByChannelIdAndTransNo(Long channelId, String transNo, LocalDateTime beginTime, LocalDateTime endTime, Byte status, Integer page, Integer pageSize) {
        //根据支付完成时间来筛选交易
        RefundTransPoManualExample example = new RefundTransPoManualExample();
        RefundTransPoManualExample.Criteria criteria = example.createCriteria();
        criteria.andChannelIdEqualTo(channelId);

        if (null != transNo){
            criteria.andTransNoEqualTo(transNo);
        }
        if (null != beginTime){
            criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        }
        if (null != endTime){
            criteria.andGmtCreateLessThanOrEqualTo(endTime);
        }
        if (null != status){
            criteria.andStatusEqualTo(status);
        }

        PageHelper.startPage(page, pageSize);
        List<RefundTransPo> poList = this.refundTransPoManualMapper.selectByExample(example);
        return poList.stream().map(o -> this.build(o)).collect(Collectors.toList());
    }


    /**
     * 查询退款交易
     * @param page 页
     * @param pageSize 每页数目
     * @param example 查询条件
     * @return
     */
    private List<RefundTrans> retrieveRefundTrans(RefundTransPoExample example, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize, false);
        List<RefundTransPo> poList = this.refundTransPoMapper.selectByExample(example);
        return poList.stream().map(po -> this.build(po)).collect(Collectors.toList());
    }

    @Override
    protected Integer save(Transaction trans) {
        RefundTrans obj;
        if (trans instanceof RefundTrans) {
            obj = (RefundTrans) trans;
        }else{
            throw new IllegalArgumentException("save: trans should be Refundtrans.");
        }
        RefundTransPo po = CloneFactory.copy(new RefundTransPo(), obj);
        return refundTransPoMapper.updateByPrimaryKeySelective(po);
    }

    @Override
    protected Transaction insert(Transaction trans) {
        RefundTrans obj;
        if (trans instanceof RefundTrans) {
            obj = (RefundTrans) trans;
        }else{
            throw new IllegalArgumentException("insert: trans should be Refundtrans.");
        }
        RefundTransPo po = CloneFactory.copy(new RefundTransPo(), obj);
        refundTransPoMapper.insertSelective(po);
        trans.setId(po.getId());
        return trans;
    }
}
