package cn.edu.xmu.oomall.wechatpay.controller;

import cn.edu.xmu.oomall.wechatpay.controller.dto.*;
import cn.edu.xmu.oomall.wechatpay.controller.vo.*;
import cn.edu.xmu.oomall.wechatpay.dao.bo.*;
import cn.edu.xmu.oomall.wechatpay.service.*;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author maguoqi
 * @date 2023/12/6
 */
@RestController
@RefreshScope
@RequestMapping(value = "/internal/v3", produces = "application/json;charset=UTF-8")
public class WeChatPayController {

    private final PaymentService paymentService;
    private final RefundService refundService;
    private final DivPaymentService divPaymentService;
    private final DivRefundService divRefundService;


    @Autowired
    public WeChatPayController(PaymentService paymentService, RefundService refundService, DivPaymentService divPaymentService, DivRefundService divRefundService) {
        this.paymentService = paymentService;
        this.refundService = refundService;
        this.divPaymentService = divPaymentService;
        this.divRefundService = divRefundService;
    }

    /**
     * JSAPI下单API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_1.shtml
     */
    @PostMapping("/pay/partner/transactions/jsapi")
    public WeChatPayReturnObject pay(@Validated @RequestBody PayTransVo payTransVo) {
        PayTrans payTrans = paymentService.createPayTrans(new PayTrans(payTransVo));
        return new WeChatPayReturnObject(new PrepayDto(payTrans));
    }

    /**
     * 商户订单号查询
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_2.shtml#part-02
     */
    @GetMapping("/pay/partner/transactions/out-trade-no/{out_trade_no}")
    public WeChatPayReturnObject getPayTransByOutNo(@PathVariable("out_trade_no") String outTradeNo, @RequestParam("sp_mchid") String spMchid, @RequestParam("sub_mchid") String subMchid) {
        PayTrans payTrans = paymentService.getPayTransByOutNo(spMchid, subMchid, outTradeNo);
        return new WeChatPayReturnObject(new PayTransDto(payTrans));
    }

    /**
     * 微信支付订单号查询
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_2.shtml#part-01
     */
    @GetMapping("/pay/partner/transactions/id/{transaction_id}")
    public WeChatPayReturnObject getPayTransByTransId(@PathVariable("transaction_id") String transaction_id, @RequestParam("sp_mchid") String spMchid, @RequestParam("sub_mchid") String subMchid) {
        PayTrans payTrans = paymentService.getPayTransByTransId(spMchid, subMchid, transaction_id);
        return new WeChatPayReturnObject(new PayTransDto(payTrans));
    }

    /**
     * 关闭订单API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_3.shtml
     */
    @PostMapping("/pay/partner/transactions/out-trade-no/{out_trade_no}/close")
    public WeChatPayReturnObject cancelOrder(@PathVariable("out_trade_no") String outTradeNo, @Validated @RequestBody PayTransCancelVo payTransCancelVo) {
        paymentService.closePayTrans(payTransCancelVo.getSpMchid(), payTransCancelVo.getSubMchid(), outTradeNo);
        return new WeChatPayReturnObject();
    }

    /**
     * 申请退款API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_9.shtml
     */
    @PostMapping("/refund/domestic/refunds")
    public WeChatPayReturnObject refund(@Validated @RequestBody RefundTransVo refundTransVo) {
        RefundTrans refundTrans = refundService.createRefund(new RefundTrans(refundTransVo));
        return new WeChatPayReturnObject(new RefundTransDto(refundTrans));
    }

    /**
     * 查询退款信息
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_10.shtml
     */
    @GetMapping("/refund/domestic/refunds/{out_refund_no}")
    public WeChatPayReturnObject getRefund(@PathVariable("out_refund_no") String outRefundNo, @RequestParam("sub_mchid") String subMchid) {
        RefundTrans refundTrans = refundService.getRefund(subMchid, outRefundNo);
        return new WeChatPayReturnObject(new RefundTransDto(refundTrans));
    }

    /**
     * 请求分账API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml
     */
    @PostMapping("/profitsharing/orders")
    public WeChatPayReturnObject createDivPayTrans(@Validated @RequestBody DivPayTransVo divPayTransVo) {
        DivPayTrans divPayTrans = divPaymentService.createDivPayTrans(new DivPayTrans(divPayTransVo));
        return new WeChatPayReturnObject(new DivPayTransDto(divPayTrans));
    }

    /**
     * 请求分账回退API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_3.shtml
     */
    @PostMapping("/profitsharing/return-orders")
    public WeChatPayReturnObject createDivRefundTrans(@Validated @RequestBody DivRefundTransVo divRefundTransVo) {
        DivRefundTrans divRefundTrans = divRefundService.createDivRefund(new DivRefundTrans(divRefundTransVo));
        return new WeChatPayReturnObject(new DivRefundTransDto(divRefundTrans));
    }

}
