//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao.channel;

import cn.edu.xmu.oomall.payment.dao.bo.*;
import cn.edu.xmu.oomall.payment.dao.channel.vo.*;

/**
 * 支付渠道适配器接口
 * 适配器模式
 */
public interface PayAdaptor {

    /**
     * 创建支付交易单
     * @author Ming Qiu
     * <p>
     * date: 2022-11-01 19:26
     * @param payTrans 支付交易
     * @return
     */
    PostPayTransAdaptorVo createPayment(PayTrans payTrans);

    /**
     * 向第三方平台查询订单
     * */
    PayTrans returnOrderByTransId(Account account, String transId);

    /**
     * 向第三方平台查询订单(未收到下单返回值)
     * */
    PayTrans returnOrderByOutNo(Account account, String outNo);

    /**
     * 取消订单
     * */
    void cancelOrder(PayTrans payTrans);


    PostRefundAdaptorVo createRefund(RefundTrans refundTrans);

    /**
     * 查询退款单
     * */
    RefundTrans returnRefund(Account account, String outNo);

    /**
     * 分账交易
     * */
    PostDivPayAdaptorVo createDivPay(PayTrans payTrans, String outNo);


    /**
     * 退款分账交易
     * */
    PostRefundAdaptorVo createDivRefund(DivRefundTrans divRefundTrans);


    /**
     * 分账关系解绑
     * @param account
     */
    void cancelChannel(Account account);

    /**
     * 建立分账关系
     * @param account
     */
    void createChannel(Account account);
}
