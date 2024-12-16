package cn.edu.xmu.oomall.alipay.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.alipay.exception.AlipayBusinessException;
import cn.edu.xmu.oomall.alipay.mapper.AlipayDivReceiverMapper;
import cn.edu.xmu.oomall.alipay.mapper.AlipayPaymentPoMapper;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiver;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiverExample;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPoExample;
import cn.edu.xmu.oomall.alipay.service.bo.Payment;
import cn.edu.xmu.oomall.alipay.service.bo.RoyaltyEntity;
import cn.edu.xmu.oomall.alipay.util.AlipayReturnNo;
import org.apache.rocketmq.common.filter.impl.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class PaymentDao {

    private AlipayPaymentPoMapper alipayPaymentPoMapper;

    private AlipayDivReceiverMapper alipayDivReceiverMapper;

    private SnowFlakeIdWorker snowFlakeIdWorker;
    private SettlementDao settlementDao;

    private Logger logger = LoggerFactory.getLogger(PaymentDao.class);

    @Autowired
    public PaymentDao(AlipayPaymentPoMapper alipayPaymentPoMapper, AlipayDivReceiverMapper alipayDivReceiverMapper,
                       SnowFlakeIdWorker snowFlakeIdWorker, SettlementDao settlementDao) {
        this.snowFlakeIdWorker = snowFlakeIdWorker;
        this.settlementDao = settlementDao;
        this.alipayPaymentPoMapper = alipayPaymentPoMapper;
        this.alipayDivReceiverMapper = alipayDivReceiverMapper;
    }

    public boolean insertPayment(Payment payment)
    {
        AlipayPaymentPo alipayPaymentPo= CloneFactory.copy(new AlipayPaymentPo(),payment);
        alipayPaymentPo.setTradeStatus(payment.getTradeStatus().getCode());
        alipayPaymentPoMapper.insertSelective(alipayPaymentPo);
        return true;
    }

    // 根据tradeNo查询完整的支付宝支付记录，如果未分账，则settlement为null,如果为绑定，则royaltyEntities为null
    public Payment retrievePaymentByTradeNo(String tradeNo) {
        //  payment表
        AlipayPaymentPoExample alipayPaymentPoExample = new AlipayPaymentPoExample();
        alipayPaymentPoExample.createCriteria().andTradeNoEqualTo(tradeNo);
        List<AlipayPaymentPo> alipayPaymentPos = alipayPaymentPoMapper.selectByExample(alipayPaymentPoExample);
        if(alipayPaymentPos.isEmpty()) return null;
        Payment payment = CloneFactory.copy(new Payment(),alipayPaymentPos.get(0));

        //  royaltyEntities   通过商户支付宝账号找到记录
        payment.setRoyaltyEntities(settlementDao.retrieveRoyalEntityByShopAccount(payment.getReceiverAccount()));

        // settlement
        payment.setSettlement(settlementDao.retrieveSettlementByTradeNo(tradeNo));

        // refund TODO

        return payment;
    }

//    public Payment selectPaymentByTradeNoOrOutTradeNo(String tradeNo, String outTradeNo) {
//        return Optional.ofNullable(tradeNo)
//                .map(this::selectPaymentByTradeNo)
//                .orElseGet(() -> selectPaymentByOutTradeNo(outTradeNo));
//    }

    private Payment selectPaymentByExample(AlipayPaymentPoExample alipayPaymentPoExample){
        List<AlipayPaymentPo> alipayPaymentPoList= alipayPaymentPoMapper.selectByExample(alipayPaymentPoExample);
        if(alipayPaymentPoList.isEmpty()) return null;
        return CloneFactory.copy(new Payment(),alipayPaymentPoList.get(0));
    }

    public Payment selectPaymentByOutTradeNo(String outTradeNo)
    {
        AlipayPaymentPoExample alipayPaymentPoExample = new AlipayPaymentPoExample();
        AlipayPaymentPoExample.Criteria criteria = alipayPaymentPoExample.createCriteria();
        criteria.andOutTradeNoEqualTo(outTradeNo);
        return selectPaymentByExample(alipayPaymentPoExample);
    }

    public Payment selectPaymentByTradeNo(String tradeNo) {
        AlipayPaymentPoExample alipayPaymentPoExample = new AlipayPaymentPoExample();
        AlipayPaymentPoExample.Criteria criteria = alipayPaymentPoExample.createCriteria();
        criteria.andTradeNoEqualTo(tradeNo);
        return selectPaymentByExample(alipayPaymentPoExample);
    }

    public Payment selectPaymentByTradeNoOrOutTradeNo(String tradeNo,String outTradeNo) {
        if(tradeNo!=null){
            return selectPaymentByTradeNo(tradeNo);
        }
        logger.info("tradeNo is null;selectPaymentByOutTradeNo(outTradeNo):"+outTradeNo);
        return selectPaymentByOutTradeNo(outTradeNo);
    }


    public List<RoyaltyEntity> selectRelationByShopAccount(String shopAccount) {
        AlipayDivReceiverExample alipayDivReceiverExample = new AlipayDivReceiverExample();
        AlipayDivReceiverExample.Criteria criteria = alipayDivReceiverExample.createCriteria();
        criteria.andTransOutEqualTo(shopAccount);
        List<AlipayDivReceiver> alipayDivReceivers = alipayDivReceiverMapper.selectByExample(alipayDivReceiverExample);
        List<RoyaltyEntity> royaltyEntities = new ArrayList<>();
        for (AlipayDivReceiver alipayDivReceiver : alipayDivReceivers) {
            royaltyEntities.add(CloneFactory.copy(new RoyaltyEntity(),alipayDivReceiver));
        }
        return royaltyEntities;
    }

    public void updatePayment(Payment payment) {
        alipayPaymentPoMapper.updateByPrimaryKeySelective(CloneFactory.copy(new AlipayPaymentPo(),payment));
    }
}
