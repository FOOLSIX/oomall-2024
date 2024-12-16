package cn.edu.xmu.oomall.alipay.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.alipay.controller.vodto.CancelOrderDto;
import cn.edu.xmu.oomall.alipay.controller.vodto.CancelOrderVo;
import cn.edu.xmu.oomall.alipay.controller.vodto.GetTransVo;
import cn.edu.xmu.oomall.alipay.exception.AlipayBusinessException;


import cn.edu.xmu.oomall.alipay.service.bo.Payment;

import cn.edu.xmu.oomall.alipay.util.ParseToken;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.xmu.oomall.alipay.dao.PaymentDao;
import cn.edu.xmu.oomall.alipay.service.bo.NotifyBody;
import cn.edu.xmu.oomall.alipay.util.AlipayReturnNo;

/**
 * @author lianshuquan
 * @date 2023/12/13
 */
@Service
public class AlipayService {

	private Logger logger = LoggerFactory.getLogger(AlipayService.class);

	@Value("${oomall.alipay.downloadurl}")
	private String bill_download_url;

	private final PaymentDao paymentDao;


	private final RocketMQTemplate rocketMQTemplate;

	private final SnowFlakeIdWorker snowFlakeIdWorker;

	@Autowired
	AlipayService(PaymentDao paymentDao, RocketMQTemplate rocketMQTemplate, SnowFlakeIdWorker snowFlakeIdWorker) {
		this.paymentDao = paymentDao;
		this.rocketMQTemplate = rocketMQTemplate;
		this.snowFlakeIdWorker = snowFlakeIdWorker;
	}



	private void paySuccess(Payment payment) {
		payment.setSuccessTime(LocalDateTime.now());
		payment.setTradeStatus(Payment.TradeStatus.TRADE_SUCCESS);
		// 默认插入成功，因为支付宝没有服务器错误的状态码
		paymentDao.insertPayment(payment);
	}

	private void payFailed(Payment payment) {
		logger.info("支付失败");
		payment.setTradeStatus(Payment.TradeStatus.WAIT_BUYER_PAY);
		paymentDao.updatePayment(payment);
	}

	private void closePayment(Payment payment) {
		logger.info("支付关闭");
		payment.setTradeStatus(Payment.TradeStatus.TRADE_CLOSED);
		paymentDao.updatePayment(payment);
	}




	@Transactional(rollbackFor = Exception.class)
	public Payment pay(String authorization, Payment payment) {
		Payment existingPayment = paymentDao.selectPaymentByOutTradeNo(payment.getOutTradeNo());
		// 如果此订单号已经存在
		if (existingPayment != null) {
			logger.info("existingPayment:"+ existingPayment.toString());
			// 已支付
			if (existingPayment.getTradeStatus().equals(Payment.TradeStatus.TRADE_SUCCESS)) {
				logger.info("TRADE_HAS_SUCCESS");
				throw new AlipayBusinessException(AlipayReturnNo.TRADE_HAS_SUCCESS);
			}
			// 已关闭
			if (existingPayment.getTradeStatus().equals(Payment.TradeStatus.TRADE_CLOSED)) {
				logger.info("TRADE_FINISHED");
				throw new AlipayBusinessException(AlipayReturnNo.TRADE_HAS_CLOSE);
			}
		}

		// 创建支付
		payment.setReceiverAccount(ParseToken.parseUserAccount(authorization));
		payment.setAppid(ParseToken.parseAppId(authorization));
		payment.setTradeNo(snowFlakeIdWorker.nextId().toString());
		payment.setTradeStatus(Payment.TradeStatus.WAIT_BUYER_PAY);
		paymentDao.insertPayment(payment);

		if(payment.getTotalAmount()==9901){	//支付金额为9901分，模拟支付关闭，回调
			closePayment(payment);
			NotifyBody notifyBody2 = new NotifyBody(LocalDateTime.now(), payment.getOutTradeNo(),
					"TRADE_CLOSED",
					null);
			// TODO
			rocketMQTemplate.sendOneWay("alipay-notify-topic"
					, MessageBuilder.withPayload(JacksonUtil.toJson(notifyBody2)).build());
		} else if (payment.getTotalAmount()==9902) {	//支付金额为9902分，模拟支付失败，不回调
			payFailed(payment);
		}else {
			paySuccess(payment);
			NotifyBody notifyBody1 = new NotifyBody(LocalDateTime.now(), payment.getOutTradeNo(),
					"TRADE_SUCCESS",
					null);
			notifyBody1.setTotal_amount(BigDecimal.valueOf(payment.getTotalAmount()));
			notifyBody1.setGmt_payment(LocalDateTime.now());
			//
			rocketMQTemplate.sendOneWay("alipay-notify-topic"
					, MessageBuilder.withPayload(JacksonUtil.toJson(notifyBody1)).build());
		}

		return payment;
	}

	public CancelOrderDto cancelOrder(String authorization, CancelOrderVo param) {
		logger.info("param:"+ JacksonUtil.toJson(param));
		Payment payment = paymentDao.selectPaymentByTradeNoOrOutTradeNo(param.getTradeNo(), param.getOutTradeNo());
		if(payment==null) throw new AlipayBusinessException(AlipayReturnNo.TRADE_NOT_EXIST);
		if(!payment.getTradeStatus().equals(Payment.TradeStatus.WAIT_BUYER_PAY))
			throw new AlipayBusinessException(AlipayReturnNo.REASON_TRADE_STATUS_INVALID);

		payment.setTradeStatus(Payment.TradeStatus.TRADE_CLOSED);
		paymentDao.updatePayment(payment);
		return new CancelOrderDto(payment.getTradeNo(), payment.getOutTradeNo());
	}

	public Payment getOrder(GetTransVo param){
		logger.info("param:"+ JacksonUtil.toJson(param));
		Payment payment = paymentDao.selectPaymentByTradeNoOrOutTradeNo(param.getTradeNo(), param.getOutTradeNo());
		if(payment!=null){
			return payment;
		}else{
			throw new AlipayBusinessException(AlipayReturnNo.TRADE_NOT_EXIST);
		}
	}

}
