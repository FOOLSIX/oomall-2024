//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.payment.mapper.openfeign;

import cn.edu.xmu.oomall.payment.mapper.openfeign.alipay.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 向支付宝发送请求的接口
 * */
@FeignClient(name = "alipay-service")
//@RequestMapping("/internal/v3/alipay/trade")
public interface AliPayMapper {

    /**
     * 统一收单交易结算接口
     * https://opendocs.alipay.com/open-v3/c3b24498_alipay.trade.order.settle?scene=common&pathHash=e06e2d2b
     * @param param
     * @return
     */
    @PostMapping("/internal/v3/alipay/trade/order/settle")
    String postDivPay(@RequestHeader(name = "authorization",required = true) String authorization, @RequestBody PostDivPayParam param);

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
    String pay(@RequestHeader(name = "authorization",required = true) String authorization, @RequestBody PostPayParam param);

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
    String refund(@RequestHeader(name = "authorization",required = true) String authorization, @RequestBody PostRefundParam param);

    /**
     * 统一收单交易关闭接口
     * https://opendocs.alipay.com/open-v3/09d1ev?scene=common&pathHash=2124a438
     */
    @PostMapping("/internal/v3/alipay/trade/close")
    String cancelOrder(@RequestHeader(name = "authorization",required = true) String authorization, @RequestBody CancelOrderParam param);

    /**
     * 查询支付单
     * https://opendocs.alipay.com/open-v3/09d1ex?scene=common&pathHash=06def985
     * @return
     */
    @PostMapping("/internal/v3/alipay/trade/query")
    String retrieveOrder(@RequestHeader(name = "authorization",required = true) String authorization, @RequestBody GetTransParam param);

    /**
     * 查询退款单
     * https://opendocs.alipay.com/open-v3/09d1ew?scene=common&pathHash=feed2164
     */
    @PostMapping("/internal/v3/alipay/trade/fastpay/refund/query")
    String getRefund(@RequestHeader(name = "authorization",required = true) String authorization, @RequestBody GetRefundParam param);

    /**
     * 创建分账
     * 2023-dgn1-006
     * @author huangzian
     * https://opendocs.alipay.com/open-v3/c21931d6_alipay.trade.royalty.relation.bind?scene=common&pathHash=2c8d10e0
     */
    @PostMapping("/internal/v3/alipay/trade/royalty/relation/bind")
    String createDiv(@RequestHeader(name = "authorization",required = true) String authorization,@RequestBody CreateDivParam param);

    /**
     * 解除分账
     * 2023-dgn1-006
     * @author huangzian
     * https://opendocs.alipay.com/open-v3/3613f4e1_alipay.trade.royalty.relation.unbind?scene=common&pathHash=0aacfe4e
     */
    @PostMapping("/internal/v3/alipay/trade/royalty/relation/unbind")
    String cancelDiv(@RequestHeader(name = "authorization",required = true) String authorization, @RequestBody CancelDivParam param);
}

