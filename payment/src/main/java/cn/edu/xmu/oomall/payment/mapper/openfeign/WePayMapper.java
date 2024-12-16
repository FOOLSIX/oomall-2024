//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.mapper.openfeign;

import cn.edu.xmu.oomall.payment.mapper.openfeign.wepay.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * 微信支付接口
 */
@FeignClient(name = "wepay-service")
public interface WePayMapper {

   /**
    * JSAPI下单
    * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_1.shtml
    * 测试数据规则：
    * 支付金额为9901分，回调支付失败
    * 支付金额为9902分，不回调
    * 其他回调成功
    * */
   @PostMapping("/internal/v3/pay/partner/transactions/jsapi")
   String pay(@RequestBody PostPayParam param);

    /**
     * 商户订单号查询
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_2.shtml#part-02
     * */
    @GetMapping("/internal/v3/pay/partner/transactions/out-trade-no/{out_trade_no}")
    String getOrderByOutNo(@PathVariable String out_trade_no, @RequestParam String sp_mchild, @RequestParam String sub_mchid);

    /**
     * 微信支付订单号查询
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_2.shtml#part-01
     * */
    @GetMapping("/internal/v3/pay/partner/transactions/id/{transaction_id}")
    String getOrderByTransId(@PathVariable String transaction_id, @RequestParam String sp_mchild, @RequestParam String sub_mchid);


    /**
     * 申请退款API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_9.shtml
     * 测试数据规则：
     * 退款金额为901分，退款失败
     * 其他退款成功
     * */
    @PostMapping("/internal/v3/refund/domestic/refunds")
    String refund(@RequestBody PostRefundParam params);

    /**
     * 查询退款信息
     * */
    @GetMapping("/internal/v3/refund/domestic/refunds/{out_refund_no}")
    String getRefund(@PathVariable String out_refund_no, @RequestParam String sub_mchid);

    /**
     * 关闭订单API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_3.shtml
     */
    @PostMapping("/internal/v3/pay/partner/transactions/out-trade-no/{out_trade_no}/close")
    String cancelOrder(@PathVariable String out_trade_no, @RequestBody CancelOrderParam param);

    /**
     * 请求分账API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml
     */
    @PostMapping("/internal/v3/profitsharing/orders")
    String postDivPay(@RequestBody PostDivPayParam param);


   /**
    * 请求分账回退API
    * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_3.shtml
    */
   @PostMapping("/internal/v3/profitsharing/return-orders")
   String postDivRefund(@RequestBody PostDivRefundParam param);

   /**
    * 添加分账方API
    * 2023-dgn1-006
    * @author huangzian
    * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_8.shtml
    */
   @PostMapping("/internal/v3/profitsharing/receivers/add")
   String createDiv(@RequestBody CreateDivParam createDivParam);

    /**
     * 删除分账接收方API
     * 2023-dgn1-006
     * @author huangzian
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_9.shtml
     */
    @PostMapping("/internal/v3/profitsharing/receivers/delete")
    String cancelDiv(@RequestBody CancelDivParam cancelDivParam);
}

