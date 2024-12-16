//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao.channel;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.payment.dao.bo.Channel;
import cn.edu.xmu.oomall.payment.dao.bo.Account;
import cn.edu.xmu.oomall.payment.dao.bo.*;
import cn.edu.xmu.oomall.payment.dao.channel.vo.*;
import cn.edu.xmu.oomall.payment.mapper.openfeign.wepay.*;
import cn.edu.xmu.oomall.payment.mapper.openfeign.WePayMapper;
import cn.edu.xmu.oomall.payment.mapper.openfeign.wepay.GetRefundRetObj;
import cn.edu.xmu.oomall.payment.mapper.openfeign.wepay.GetTransRetObj;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 微信支付适配器
 * @author Ming Qiu
 */
@Repository("wePayChannel")
@Slf4j
@RequiredArgsConstructor
public class WePayAdaptor implements PayAdaptor {

    private final WePayMapper wePayMapper;

    /**
     * 支付状态对应关系
     */
    public static final Map<String, Byte> PayStatusMap = new HashMap<>(){
        {
            put("NOTPAY", PayTrans.NEW);
            put("CLOSED", PayTrans.FAIL);
            put("SUCCESS", PayTrans.SUCCESS);
        }
    };

    /**
     * 退款状态对应关系
     */
    public static final Map<String, Byte> RefundStatusMap = new HashMap<>(){
        {
            put("PROCESSING", PayTrans.NEW);
            put("ABNORMAL", PayTrans.FAIL);
            put("SUCCESS", PayTrans.SUCCESS);
            put("CLOSED", PayTrans.FAIL);
        }
    };


    /**
     * 支付交易
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_4.shtml
     * */
    @Override
    public PostPayTransAdaptorVo createPayment(PayTrans payTrans) {
        Account shop = payTrans.getAccount();
        Channel channel = shop.getChannel();
        /*Set param*/
        PostPayParam param = PostPayParam.builder().
                sp_appid(channel.getSpAppid()).
                sp_mchid(channel.getSpMchid()).
                sub_mchid(shop.getSubMchid()).
                description(payTrans.getDescription()).
                out_trade_no(payTrans.getOutNo()).
                time_expire(payTrans.getTimeExpire()).
                notify_url(channel.getNotifyUrl()).
                amount(PayAmount.builder().total(payTrans.getAmount()).build()).
                payer(Payer.builder().sp_openid(payTrans.getSpOpenid()).build()).
                build();

        String ret= this.wePayMapper.pay(param);
        PostPayRetObj retObj = JacksonUtil.toObj(ret, PostPayRetObj.class);
        if (Objects.isNull(retObj)) {
            ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createPayment：param = {}, code = {}, message = {}", param, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信支付"));
        } else {
            PostPayTransAdaptorVo dto = PostPayTransAdaptorVo.builder().prepayId(retObj.getPrepayId()).build();
            return dto;
        }
    }

    /**
     * 商户订单号查询
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_2.shtml#part-02
     * */
    @Override
    public PayTrans returnOrderByOutNo(Account account, String outNo) {
        Channel channel = account.getChannel();
        assert Objects.nonNull(channel) :  "returnOrderByOutNo: channel is null";
        String ret = this.wePayMapper.getOrderByOutNo(outNo, channel.getSpMchid(), account.getSubMchid());
        return this.retrievePayTrans(outNo, ret);
    }

    /**
     * 查询支付交易
     * @param query 查询的值
     * @param ret 查询渠道返回值
     * @return
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    private PayTrans retrievePayTrans(String query, String ret) {
        GetTransRetObj retObj = JacksonUtil.toObj(ret, GetTransRetObj.class);
        if (Objects.isNull(retObj)) {
            ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("retrievePayTrans：query = {}, code = {}, message = {}", query, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信支付查询"));
        }else {
            PayTrans trans = new PayTrans();
            trans.setOutNo(retObj.getOut_trade_no());
            trans.setTransNo(retObj.getTransaction_id());
            trans.setStatus(PayStatusMap.get(retObj.getTrade_state()));
            if(retObj.getTransRetPayer() != null)trans.setSpOpenid(retObj.getTransRetPayer().getSp_openid());
            if(retObj.getTransRetAmount() != null)trans.setAmount(retObj.getTransRetAmount().getTotal());
            return trans;
        }
    }


    /**
     * 商户订单号查询
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_2.shtml#part-02
     * */
    @Override
    public PayTrans returnOrderByTransId(Account account, String transId) {
        Channel channel = account.getChannel();
        assert Objects.nonNull(channel) :"returnOrderByTransId: channel is null";
        String ret = this.wePayMapper.getOrderByTransId(transId, channel.getSpMchid(), account.getSubMchid());
        return this.retrievePayTrans(transId, ret);
    }

    /**
     * 取消订单
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_3.shtml
     * */
    @Override
    public void cancelOrder(PayTrans payTrans) {
        Account shop = payTrans.getAccount();
        Channel channel = shop.getChannel();
        /*set param*/
        CancelOrderParam param = CancelOrderParam.builder()
                .sp_mchid(channel.getSpMchid())
                .sub_mchid(shop.getSubMchid())
                .build();

        String ret = this.wePayMapper.cancelOrder(payTrans.getTransNo(), param);
        ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
        if (Objects.nonNull(exceptionRetObj)) {
            log.error("createPayment：param = {}, code = {}, message = {}", param, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信退款"));
        }
    }

    /**
     * 退款交易
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_9.shtml
     * */
    @Override
    public PostRefundAdaptorVo createRefund(RefundTrans refundTrans) {
        Account account = refundTrans.getAccount();
        Channel channel = account.getChannel();
        PayTrans payTrans = refundTrans.getPayTrans();

        /*set param*/
        PostRefundParam param = PostRefundParam.builder()
                .sub_mchid(account.getSubMchid())
                .transaction_id(payTrans.getTransNo())
                .out_refund_no(refundTrans.getOutNo())
                .amount(PostRefundAmount.builder()
                                .total(payTrans.getAmount())
                                .refund(refundTrans.getAmount()).build())
                .build();

        String ret = this.wePayMapper.refund(param);
        PostRefundRetObj retObj = JacksonUtil.toObj(ret, PostRefundRetObj.class);
        if (Objects.isNull(retObj)) {
            ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createPayment：param = {}, code = {}, message = {}", param, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信退款"));
        } else {
            PostRefundAdaptorVo dto = PostRefundAdaptorVo.builder()
                    .transNo(retObj.getRefund_id())
                    .userReceivedAccount(retObj.getUser_received_account())
                    .successTime(retObj.getSuccess_time())
                    .amount(retObj.getAmount().getRefund())
                    .build();
            return dto;
        }
    }

    /**
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_10.shtml
     * 管理员查询退款信息
     * */
    @Override
    public RefundTrans returnRefund(Account account, String outNo) {
        String ret = this.wePayMapper.getRefund(outNo, account.getSubMchid());
        GetRefundRetObj retObj = JacksonUtil.toObj(ret, GetRefundRetObj.class);
        if (Objects.isNull(retObj)) {
            ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createPayment：outNo = {}, code = {}, message = {}", outNo, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信退款查询"));
        } else {
            RefundTrans trans = new RefundTrans();
            trans.setTransNo(retObj.getRefund_id());
            trans.setOutNo(retObj.getOut_refund_no());
            trans.setUserReceivedAccount(retObj.getUser_received_account());
            trans.setStatus(RefundStatusMap.get(retObj.getStatus()));
            trans.setSuccessTime(retObj.getSuccess_time());
            trans.setAmount(retObj.getRefundRetAmount().getPayer_refund());
            trans.setDivAmount(retObj.getRefundRetAmount().getRefundRetFrom().getAmount());
            return trans;
        }
    }


    /**
     * 分账交易
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_1.shtml
     * */
    @Override
    public PostDivPayAdaptorVo createDivPay(PayTrans payTrans, String outNo) {
        Account shop = payTrans.getAccount();
        Channel channel = shop.getChannel();

        /*set param*/
        PostDivPayParam param = PostDivPayParam.builder()
                .appid(channel.getSpAppid())
                .out_order_no(outNo)
                .transaction_id(payTrans.getTransNo())
                .receivers(new ArrayList<>(){
                    {
                        add(Receiver.builder()
                                .account(channel.getSpMchid())
                                .amount(payTrans.getDivAmount())
                                .build());
                    }
                })
                .build();

        String ret = wePayMapper.postDivPay(param);
        PostDivPayRetObj retObj = JacksonUtil.toObj(ret, PostDivPayRetObj.class);
        if (Objects.isNull(retObj)) {
            ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createPayment：param = {}, code = {}, message = {}", param, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信分账"));
        } else {
            PostDivPayAdaptorVo dto = PostDivPayAdaptorVo.builder()
                    .transactionId(retObj.getTransaction_id())
                    .orderId(retObj.getOrder_id())
                    .build();
            return dto;
        }
    }


    /**
     * 请求分账回退API
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter8_1_3.shtml
     * */
    @Override
    public PostRefundAdaptorVo createDivRefund(DivRefundTrans divRefundTrans) {
        DivPayTrans divPayTrans = divRefundTrans.getDivPayTrans();
        Account account = divRefundTrans.getAccount();
        Channel channel = account.getChannel();
        /*set param*/

        PostDivRefundParam param = PostDivRefundParam.builder()
                .sub_mchid(account.getSubMchid())
                .order_id(divPayTrans.getTransNo())
                .out_return_no(divRefundTrans.getOutNo())
                .return_mchid(channel.getSpMchid())
                .amount(divRefundTrans.getAmount())
                .build();

        String ret = this.wePayMapper.postDivRefund(param);
        PostDivRefundRetObj retObj = JacksonUtil.toObj(ret, PostDivRefundRetObj.class);
        if (Objects.isNull(retObj)) {
            ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createPayment：param = {}, code = {}, message = {}", param, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信分账退款"));
        } else {
            PostRefundAdaptorVo dto = PostRefundAdaptorVo.builder()
                    .transNo(retObj.getReturn_id())
                    .successTime(retObj.getFinish_time())
                    .userReceivedAccount(retObj.getUser_received_account())
                    .amount(retObj.getAmount())
                    .build();
            return dto;
        }
    }
    /**
     */
    @Override
    public void cancelChannel(Account account) {
        Channel channel= account.getChannel();
        assert Objects.nonNull(channel) :"cancelChannel: channel is null";
        CancelDivParam param= CancelDivParam.builder()
                .sub_mchid(account.getSubMchid())
                .appid(channel.getSpAppid())
                .account(channel.getSpMchid())
                .build();
        String ret=this.wePayMapper.cancelDiv(param);
        CancelDivRetObj retObj=JacksonUtil.toObj(ret,CancelDivRetObj.class);
        if (Objects.isNull(retObj)) {
            ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("cancelChannel：param = {}, code = {}, message = {}", param, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信删除分账"));
        }
    }
    /**
     */
    @Override
    public void createChannel(Account account) {
        Channel channel= account.getChannel();
        assert Objects.nonNull(channel) :"createChannel: channel is null";
        CreateDivParam param= CreateDivParam.builder()
                .sub_mchid(account.getSubMchid())
                .appid(channel.getSpAppid())
                .account(channel.getSpMchid())
                .build();
        String ret=this.wePayMapper.createDiv(param);
        CreateDivRetObj retObj = JacksonUtil.toObj(ret, CreateDivRetObj.class);
        if (Objects.isNull(retObj)) {
            ExceptionRetObj exceptionRetObj = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createChannel：param = {}, code = {}, message = {}", param, exceptionRetObj.getCode(), exceptionRetObj.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "微信创建分账"));
        }
    }
}
