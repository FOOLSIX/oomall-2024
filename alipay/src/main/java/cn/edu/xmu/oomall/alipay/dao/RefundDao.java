package cn.edu.xmu.oomall.alipay.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.alipay.mapper.AlipayRefundPoMapper;
import cn.edu.xmu.oomall.alipay.mapper.AlipayRefundRoyaltyDetailMapper;
import cn.edu.xmu.oomall.alipay.mapper.po.*;
import cn.edu.xmu.oomall.alipay.service.bo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RefundDao {
    @Autowired
    private AlipayRefundPoMapper alipayRefundPoMapper;
    @Autowired
    private AlipayRefundRoyaltyDetailMapper alipayRefundRoyaltyDetailMapper;

    private Logger logger = LoggerFactory.getLogger(RefundDao.class);

    public void insertRefund(Refund refund) {
        AlipayRefundPo refundPo = CloneFactory.copy(new AlipayRefundPo(), refund);
        alipayRefundPoMapper.insertSelective(refundPo);
    }

    public void insertRefundRoyaltyDetail(RefundRoyaltyDetail refundRoyaltyDetail) {
        logger.info("insertRefundRoyaltyDetail :"+ JacksonUtil.toJson(refundRoyaltyDetail));
        AlipayRefundRoyaltyDetail refundRoyaltyDetailPo = CloneFactory.copy(new AlipayRefundRoyaltyDetail(), refundRoyaltyDetail);
        alipayRefundRoyaltyDetailMapper.insertSelective(refundRoyaltyDetailPo);
    }

    public Refund retrieveByOutTradeNo(String outTradeNo) {
        AlipayRefundPoExample alipayRefundPoExample = new AlipayRefundPoExample();
        alipayRefundPoExample.createCriteria().andOutTradeNoEqualTo(outTradeNo);
        List<AlipayRefundPo> alipayRefundPos = alipayRefundPoMapper.selectByExample(alipayRefundPoExample);
        Refund refund = null;
        if(alipayRefundPos.size() > 0){
            refund = CloneFactory.copy(new Refund(), alipayRefundPos.get(0));
        }
        // 查找退款分账
        AlipayRefundRoyaltyDetailExample alipayRefundRoyaltyDetailExample = new AlipayRefundRoyaltyDetailExample();
        alipayRefundRoyaltyDetailExample.createCriteria().andOutTradeNoEqualTo(outTradeNo);
        List<AlipayRefundRoyaltyDetail> alipayRefundRoyaltyDetails = alipayRefundRoyaltyDetailMapper.selectByExample(alipayRefundRoyaltyDetailExample);
        if(alipayRefundRoyaltyDetails.size() > 0){
            List<RefundRoyaltyDetail> refundRoyaltyDetails = new ArrayList<>();
            for(AlipayRefundRoyaltyDetail ard : alipayRefundRoyaltyDetails){
                RefundRoyaltyDetail refundRoyaltyDetail = CloneFactory.copy(new RefundRoyaltyDetail(), ard);
                refundRoyaltyDetails.add(refundRoyaltyDetail);
            }
            if (refund != null) {
                refund.setRefundRoyaltyDetails(refundRoyaltyDetails);
            }
        }
        return refund;
    }

}
