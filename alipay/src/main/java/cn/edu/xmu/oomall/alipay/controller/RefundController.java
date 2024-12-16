package cn.edu.xmu.oomall.alipay.controller;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.alipay.controller.vodto.*;

import cn.edu.xmu.oomall.alipay.service.*;
import cn.edu.xmu.oomall.alipay.service.bo.*;
import cn.edu.xmu.oomall.alipay.util.AlipayReturnObj;
import cn.edu.xmu.oomall.alipay.util.ParseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
public class RefundController {
    private Logger logger = LoggerFactory.getLogger(AlipayService.class);

    @Autowired
    private RefundService refundService;
    /**
     * 统一收单交易退款接口
     * https://opendocs.alipay.com/open-v3/09d1eu?scene=common&pathHash=19d27b3b
     * @param param
     * @return
     * 测试数据规则：
     * 退款金额为901分，退款失败
     * 其他退款成功
     */
    @PostMapping("/internal/v3/alipay/trade/refund")
    AlipayReturnObj refund(@RequestHeader(name = "authorization",required = true) String authorization,
                           @RequestBody PostRefundVo param){
        Refund refund = CloneFactory.copy(new Refund(),param);
        refund.setRefundAmount((int)(param.getRefundAmount()*100));
        String shopAccount = ParseToken.parseUserAccount(authorization);

        if(param.getRefundRoyaltyParameters()==null){
            refundService.refundWithoutDiv(shopAccount,refund);
        }else{
            List<RefundRoyaltyDetail> refundRoyaltyDetails = new ArrayList<>();
            for(RoyaltyDetailInfoPojo royaltyDetailInfoPojo:param.getRefundRoyaltyParameters()){
                RefundRoyaltyDetail refundRoyaltyDetail = CloneFactory.copy(new RefundRoyaltyDetail(),royaltyDetailInfoPojo);
                refundRoyaltyDetail.setRefundAmount((int)(royaltyDetailInfoPojo.getAmount()*100));
                refundRoyaltyDetails.add(refundRoyaltyDetail);
            }
            refund.setRefundRoyaltyDetails(refundRoyaltyDetails);
            refundService.refundDiv(shopAccount,refund);
        }

        PostRefundRetDto ret = PostRefundRetDto.builder()
                .outTradeNo(refund.getOutTradeNo())
               .buyerLoginId("buyerLoginId")
               .refundFee(refund.getRefundAmount().doubleValue()/100.0)
               .fundChange("Y")
               .tradeNo(refund.getTradeNo())
               .build();
        logger.info("PostRefundRetDto: "+ ret.toString());
        logger.info("PostRefundRetDto: "+ JacksonUtil.toJson(ret));
        return new AlipayReturnObj(ret);
    }


    /**
     * 查询退款单
     * https://opendocs.alipay.com/open-v3/09d1ew?scene=common&pathHash=feed2164
     */
    @PostMapping("/internal/v3/alipay/trade/fastpay/refund/query")
    AlipayReturnObj getRefund(@RequestHeader(name = "authorization",required = true) String authorization,
                     @RequestBody GetRefundParam param){
        Refund refund = refundService.retrieveByOutTradeNo(param.getOutTradeNo());
        if(refund==null) return null;
        GetRefundRetDto ret = GetRefundRetDto.builder().tradeNo(refund.getTradeNo())
               .outTradeNo(refund.getOutTradeNo()).outRequestNo(refund.getOutRequestNo())
               .refundAmount(refund.getRefundAmount().doubleValue()/100.0)
                .totalAmount(refund.getTotalAmount().doubleValue()/100.0).refundStatus("SUCCESS")
                .build();

        List<RefundRoyaltyResult> refundRoyaltys = new ArrayList<>();
        ret.setRefundRoyaltys(refundRoyaltys);

        refund.getRefundRoyaltyDetails().forEach(refundRoyaltyDetail -> {
            RefundRoyaltyResult refundRoyalty = RefundRoyaltyResult.builder()
                   .transOut(refundRoyaltyDetail.getTransOut()).transIn(refundRoyaltyDetail.getTransIn())
                   .refundAmount(refundRoyaltyDetail.getRefundAmount().doubleValue()/100.0)
                    .royaltyType("transfer").resultCode("SUCCESS").build();
            refundRoyaltys.add(refundRoyalty);
        });
        return new AlipayReturnObj(ret);
    }



}
