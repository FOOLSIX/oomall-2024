package cn.edu.xmu.oomall.alipay.service;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.alipay.controller.vodto.*;
import cn.edu.xmu.oomall.alipay.service.bo.*;
import cn.edu.xmu.oomall.alipay.dao.SettlementDao;
import cn.edu.xmu.oomall.alipay.exception.AlipayBusinessException;


import cn.edu.xmu.oomall.alipay.service.bo.Payment;
import cn.edu.xmu.oomall.alipay.service.bo.Settlement;
import cn.edu.xmu.oomall.alipay.util.ParseToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.edu.xmu.oomall.alipay.dao.*;

import cn.edu.xmu.oomall.alipay.util.AlipayReturnNo;

import java.util.Objects;


/**
 * @author lianshuquan
 * @date 2023/12/13
 */
@Service
public class SettlementService {

    private Logger logger = LoggerFactory.getLogger(AlipayService.class);


    @Value("${oomall.alipay.downloadurl}")
    private String bill_download_url;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private RefundDao refundDao;

    @Autowired
    private SettlementDao settlementDao;

//	@Autowired
//	private RoyaltyRelationMapper relationMapper;



    //@Autowired
    //private RocketMQTemplate rocketMQTemplate;

    private SnowFlakeIdWorker snowFlakeIdWorker;




    public Settlement settleOrder(String authorization, Settlement settlement) {
        // 查询订单是否存在
        Payment payment = paymentDao.retrievePaymentByTradeNo(settlement.getTradeNo());
        if (payment == null) {
            throw new AlipayBusinessException(AlipayReturnNo.TRADE_NOT_EXIST);
        }
        logger.info("settlement in settleOrder:"+ JacksonUtil.toJson(settlement));
        logger.info("payment in settleOrder: " + JacksonUtil.toJson(payment));
        int divAmountSum = 0;

        for (RoyaltyDetail royaltyDetail : settlement.getRoyaltyDetails()) {
            // 收入方是否在分账关系内
            if(payment.getRoyaltyEntities().stream()
                    .noneMatch(receiver -> receiver.getAccount().equals(royaltyDetail.getTransIn())))
                throw new AlipayBusinessException(AlipayReturnNo.ROYALTY_RECEIVER_INVALID);
            divAmountSum += royaltyDetail.getAmount();
            // 分账金额是否超过最大可分账金额
            if(payment.getTotalAmount() < divAmountSum){
                throw new AlipayBusinessException(AlipayReturnNo.ALLOC_AMOUNT_VALIDATE_ERROR);
            }
        }

        // 分账
        settlementDao.settleOrder(settlement);

        return settlement;


    }


    public void royaltyRelationBind(String authorization, CreateDivVo divRelations) {
        String outRequestNo = divRelations.getOutRequestNo();
        if (divRelations.getReceiverList() == null || divRelations.getReceiverList().size() == 0) {
            throw new AlipayBusinessException(AlipayReturnNo.RECEIVER_LIST_EMPTY);
        }
        for (RoyaltyEntity royaltyEntity : divRelations.getReceiverList()) {
            logger.info("RoyaltyEntity in royaltyRelationBind:"+ JacksonUtil.toJson(royaltyEntity));
            if(!Objects.equals(royaltyEntity.getType(), "userId")
                    && !Objects.equals(royaltyEntity.getType(), "loginName")
                    && !Objects.equals(royaltyEntity.getType(), "openId"))
                throw new AlipayBusinessException(AlipayReturnNo.INVALID_RECEIVER_TYPE);
            settlementDao.insertRelation(outRequestNo, ParseToken.parseUserAccount(authorization),royaltyEntity);
        }
    }

	public void royaltyRelationUnBind(CancelDivVo param) {
        logger.info("CancelDivVo in royaltyRelationUnBind:"+ JacksonUtil.toJson(param));
        if(param.getOutRequestNo()==null
                || settlementDao.retrieveRoyalEntityByOutRequestNo(param.getOutRequestNo()).isEmpty())
            throw new AlipayBusinessException(AlipayReturnNo.INVALID_PARAMETER);
        for (RoyaltyEntity royaltyEntity : param.getReceiverList()) {
            settlementDao.deleteRelation(param.getOutRequestNo() ,royaltyEntity);
        }
	}


}
