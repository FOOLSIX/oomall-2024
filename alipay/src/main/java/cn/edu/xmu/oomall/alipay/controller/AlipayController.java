package cn.edu.xmu.oomall.alipay.controller;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.alipay.controller.vodto.*;
import cn.edu.xmu.oomall.alipay.exception.AlipayBusinessException;
import cn.edu.xmu.oomall.alipay.service.bo.Payment;

import cn.edu.xmu.oomall.alipay.util.AlipayReturnObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cn.edu.xmu.oomall.alipay.service.AlipayService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;

/**
 * @author lianshuquan
 * @date 2023/12/11
 */
//@Api(value = "支付宝接口", tags = "支付宝接口")
@RestController
@RefreshScope
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class AlipayController {


	private final AlipayService alipayService;

	private Logger logger = LoggerFactory.getLogger(AlipayController.class);

	@Autowired
	public AlipayController(AlipayService alipayService) {
		this.alipayService = alipayService;
	}



	/**
	 * 手机网站支付接口2.0接口
	 * https://opendocs.alipay.com/open-v3/09d1et?scene=21&pathHash=f2618d9f
	 * @param param
	 * @return
	 * 测试数据规则：
	 * 支付金额为9901分，回调支付失败
	 * 支付金额为9902分，不回调
	 * 其他回调成功
	 */
	@PostMapping("/internal/v3/alipay/trade/wap/pay")
	public AlipayReturnObj pay(@RequestHeader(name = "authorization",required = true) String authorization, @RequestBody PostPayVo param){
		Payment payment = CloneFactory.copy(new Payment(), param);
		logger.info("payment："+JacksonUtil.toJson(payment));
		payment.setTotalAmount((int) (param.getTotalAmount()*100));
		payment = alipayService.pay(authorization,payment);
		PostPayDto postPayDto = CloneFactory.copy(new PostPayDto(), payment);
		postPayDto.setTotalAmount(payment.getTotalAmount().doubleValue()/100.0);
		postPayDto.setReceiverAccount(payment.getReceiverAccount());
		return new AlipayReturnObj(postPayDto);
	}


	/**
	 * 统一收单交易关闭接口
	 * https://opendocs.alipay.com/open-v3/09d1ev?scene=common&pathHash=2124a438
	 */
	@PostMapping("/internal/v3/alipay/trade/close")
	AlipayReturnObj cancelOrder(@RequestHeader(name = "authorization",required = true) String authorization,
					   @RequestBody CancelOrderVo param){
		CancelOrderDto retObj =  alipayService.cancelOrder(authorization,param);
		return new AlipayReturnObj(retObj);
	}

	/**
	 * 查询支付单
	 * https://opendocs.alipay.com/open-v3/09d1ex?scene=common&pathHash=06def985
	 * @return
	 */
	@PostMapping("/internal/v3/alipay/trade/query")
	AlipayReturnObj retrieveOrder(@RequestBody GetTransVo param){
		Payment payment = alipayService.getOrder(param);

		GetTransRetDto getTransRetObj = new GetTransRetDto();
		getTransRetObj.setTotalAmount(payment.getTotalAmount().doubleValue()/100.0);
		getTransRetObj.setTradeNo(payment.getTradeNo());
		getTransRetObj.setOutTradeNo(payment.getOutTradeNo());
		getTransRetObj.setBuyerLogonId(payment.getBuyerLogonId());
		getTransRetObj.setTradeStatus(payment.getTradeStatus().getDescription());

		return new AlipayReturnObj(getTransRetObj);

	}



}
