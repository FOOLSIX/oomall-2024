package cn.edu.xmu.oomall.alipay.controller;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.alipay.controller.vodto.*;
import cn.edu.xmu.oomall.alipay.exception.AlipayBusinessException;


import cn.edu.xmu.oomall.alipay.service.bo.*;

import cn.edu.xmu.oomall.alipay.service.SettlementService;
import cn.edu.xmu.oomall.alipay.util.AlipayReturnObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cn.edu.xmu.oomall.alipay.service.*;

import java.util.ArrayList;
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
public class SettlementController {


    private final AlipayService alipayService;

    private final SettlementService settlementService;

    private final SnowFlakeIdWorker snowFlakeIdWorker;

    @Autowired
    public SettlementController(AlipayService alipayService, SettlementService settlementService
    , SnowFlakeIdWorker snowFlakeIdWorker) {
        this.settlementService = settlementService;
        this.alipayService = alipayService;
        this.snowFlakeIdWorker = snowFlakeIdWorker;
    }


    /**
     * 统一收单交易结算接口
     * https://opendocs.alipay.com/open-v3/c3b24498_alipay.trade.order.settle?scene=common&pathHash=e06e2d2b
     * @param param
     * @return
     */
    @PostMapping("/internal/v3/alipay/trade/order/settle")
    AlipayReturnObj postDivPay(@RequestHeader(name = "authorization",required = true) String authorization
            , @RequestBody PostDivPayVo param){
        // 构造分账信息
        Settlement settlement = CloneFactory.copy(new Settlement(), param);
        settlement.setSettleNo(snowFlakeIdWorker.nextId().toString());
        ArrayList<RoyaltyDetail> royaltyDetails = new ArrayList<>();
        for (OpenApiRoyaltyDetailInfoPojo royaltyParameter : param.getRoyaltyParameters()) {
            RoyaltyDetail royaltyDetail = CloneFactory.copy(new RoyaltyDetail(), royaltyParameter);
            royaltyDetail.setAmount((int)(royaltyParameter.getAmount()*100));
            royaltyDetail.setSettleNo(settlement.getSettleNo());
            royaltyDetails.add(royaltyDetail);
        }
        settlement.setRoyaltyDetails(royaltyDetails);

        settlement = settlementService.settleOrder(authorization,settlement);
        PostDivPayDto postDivPayDto = PostDivPayDto.builder()
               .tradeNo(settlement.getTradeNo())
               .settleNo(settlement.getSettleNo())
               .build();
        return new AlipayReturnObj(postDivPayDto);
    }



    /**
     * 创建分账关系
     * https://opendocs.alipay.com/open-v3/c21931d6_alipay.trade.royalty.relation.bind?scene=common&pathHash=2c8d10e0
     */
    @PostMapping("/internal/v3/alipay/trade/royalty/relation/bind")
    AlipayReturnObj createDiv(@RequestHeader(name = "authorization",required = false) String authorization,
                     @RequestBody CreateDivVo param){
        settlementService.royaltyRelationBind(authorization,param);
        return new AlipayReturnObj(new CreateDivDto("SUCCESS"));
    }

    /**
     * 解除分账关系
     * https://opendocs.alipay.com/open-v3/3613f4e1_alipay.trade.royalty.relation.unbind?scene=common&pathHash=0aacfe4e
     */
    @PostMapping("/internal/v3/alipay/trade/royalty/relation/unbind")
    AlipayReturnObj cancelDiv(@RequestHeader(name = "authorization",required = true) String authorization,
                     @RequestBody CancelDivVo param){
        settlementService.royaltyRelationUnBind(param);
        return new AlipayReturnObj(new CancelDivDto("SUCCESS"));
    }



}

