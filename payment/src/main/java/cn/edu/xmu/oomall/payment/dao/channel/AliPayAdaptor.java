//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao.channel;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import cn.edu.xmu.oomall.payment.dao.bo.*;
import cn.edu.xmu.oomall.payment.dao.channel.vo.PostDivPayAdaptorVo;
import cn.edu.xmu.oomall.payment.dao.channel.vo.PostPayTransAdaptorVo;
import cn.edu.xmu.oomall.payment.dao.channel.vo.PostRefundAdaptorVo;
import cn.edu.xmu.oomall.payment.mapper.openfeign.AliPayMapper;
import cn.edu.xmu.oomall.payment.mapper.openfeign.alipay.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 支付宝支付适配器
 */
@Repository("aliPayChannel")
@Slf4j
@RequiredArgsConstructor
public class AliPayAdaptor implements PayAdaptor {

    private static final String SUCCESS_CODE = "10000";

    private static final String SUCCESS_SUB_CODE = "ACQ.TRADE_HAS_SUCCESS";

    private final AliPayMapper aliPayMapper;
    private final SnowFlakeIdWorker snowFlakeIdWorker;

    /**
     * 状态对应关系
     */
    public static final Map<String, Byte> StatusMap = new HashMap<>(){
        {
            put("WAIT_BUYER_PAY", PayTrans.NEW);
            put("TRADE_CLOSED", PayTrans.FAIL);
            put("TRADE_SUCCESS", PayTrans.SUCCESS);
            put("TRADE_FINISHED", PayTrans.SUCCESS);
        }
    };

    /**
     * 支付交易
     * https://opendocs.alipay.com/open-v3/09d1et?scene=21&pathHash=f2618d9f
     */
    @Override
    public PostPayTransAdaptorVo createPayment(PayTrans payTrans) {
        Account shop = payTrans.getAccount();
        assert Objects.nonNull(shop) : "AliPayAdaptor.createPayment: shop is null";
        Channel channel = shop.getChannel();
        assert Objects.nonNull(channel) : "AliPayAdaptor.createPayment: channel is null";

        /*set param*/
        PostPayParam p = PostPayParam.builder()
                .notify_url(channel.getNotifyUrl())
                .out_trade_no(payTrans.getOutNo()).subject(payTrans.getDescription()).build();
        p.setTotal_amount(payTrans.getAmount());

        String ret = this.aliPayMapper.pay(this.getAuthorization(channel.getSpAppid(),shop.getSubMchid()), p);
        PostPayRetObj retObj = JacksonUtil.toObj(ret, PostPayRetObj.class);
        if (Objects.isNull(retObj)){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createPayment: param = {}, code = {}, message = {}", p, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝支付接口"));
        } else {
            PostPayTransAdaptorVo dto = PostPayTransAdaptorVo.builder().
                    outTradeNo(retObj.getOut_trade_no()).
                    prepayId(retObj.getTrade_no()).
                    totalAmount(retObj.getTotal_amount()).
                    build();
            return dto;
        }
    }

    /**
     * 查询订单信息，用于下单后未收到支付宝返回信息
     * https://opendocs.alipay.com/open-v3/09d1ex?scene=common&pathHash=06def985
     */
    @Override
    public PayTrans returnOrderByOutNo(Account account, String outNo) {

        assert Objects.nonNull(account) : "AliPayAdaptor.returnOrderByOutNo: account is null";
        Channel channel = account.getChannel();
        assert Objects.nonNull(channel) : "AliPayAdaptor.returnOrderByOutNo: channel is null";
        String authorization  = this.getAuthorization(channel.getSpAppid(), account.getSubMchid());
        GetTransParam p = GetTransParam.builder()
                .out_trade_no(outNo)
                .build();
        return retrievePayTrans(authorization, p);
    }

    /**
     * 在支付渠道查询支付交易
     * @param p 查询参数
     * @return 支付交易
     */
    private PayTrans retrievePayTrans(String authorization, GetTransParam p) {
        log.debug("retrievePayTrans: param = {}", p);
        String ret = this.aliPayMapper.retrieveOrder(authorization, p);
        log.debug("retrievePayTrans: ret = {}", ret);
        GetTransRetObj retObj = JacksonUtil.toObj(ret, GetTransRetObj.class);
        log.debug("retrievePayTrans: retObj = {}", retObj);
        if (Objects.isNull(retObj)){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("retrievePayTrans: param = {}, code = {}, message = {}", p, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝查询支付"));
        }else {
            PayTrans trans = new PayTrans();
            trans.setOutNo(retObj.getOut_trade_no());
            trans.setTransNo(retObj.getTrade_no());
            trans.setSpOpenid(retObj.getBuyer_logon_id());
            trans.setStatus(StatusMap.get(retObj.getTrade_status()));
            trans.setAmount((long) (retObj.getTotal_amount() * 100));
            return trans;
        }
    }


    /**
     * 查询订单信息
     * https://opendocs.alipay.com/open-v3/09d1ex?scene=common&pathHash=06def985
     *
     */
    @Override
    public PayTrans returnOrderByTransId(Account account, String transId) {
        assert Objects.nonNull(account) : "AliPayAdaptor.returnOrderByOutNo: account is null";
        Channel channel = account.getChannel();
        assert Objects.nonNull(channel) : "AliPayAdaptor.returnOrderByOutNo: channel is null";

        GetTransParam p = GetTransParam.builder()
                .trade_no(transId)
                .build();
        String authorization  = this.getAuthorization(channel.getSpAppid(), account.getSubMchid());
        return retrievePayTrans(authorization, p);
    }

    /**
     * 取消支付
     * https://opendocs.alipay.com/open-v3/09d1ev?scene=common&pathHash=2124a438
     */
    @Override
    public void cancelOrder(PayTrans payTrans) {

        Account shop = payTrans.getAccount();
        assert Objects.nonNull(shop): "AliPayAdaptor.cancelOrder: shop is null";
        Channel channel = shop.getChannel();
        assert Objects.nonNull(channel): "AliPayAdaptor.cancelOrder: channel is null";
        /*set param*/
        CancelOrderParam p = CancelOrderParam.builder()
                .trade_no(payTrans.getTransNo())
                .build();

        String ret = this.aliPayMapper.cancelOrder(this.getAuthorization(channel.getSpAppid(),shop.getSubMchid()), p);
        CancelOrderRetObj retObj = JacksonUtil.toObj(ret, CancelOrderRetObj.class);
        if (null == retObj){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createRefund: param = {}, code = {}, message = {}", p, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝取消支付"));
        }
    }

    /**
     * 退款交易,退款和退分账分开进行
     * https://opendocs.alipay.com/open-v3/09d1eu?scene=common&pathHash=19d27b3b
     */
    @Override
    public PostRefundAdaptorVo createRefund(RefundTrans refundTrans) {

        PayTrans payTrans = refundTrans.getPayTrans();
        assert Objects.nonNull(payTrans): "AliPayAdaptor.createRefund：payTrans is null";
        Account shop = refundTrans.getAccount();
        assert Objects.nonNull(shop) : "AliPayAdaptor.createRefund：shop is null";
        Channel channel = refundTrans.getChannel();
        assert Objects.nonNull(channel): "AliPayAdaptor.createRefund：channel is null";

        //退款，不包括退分账 https://opendocs.alipay.com/support/01rfw9
        /*set param*/
        PostRefundParam p = PostRefundParam.builder()
                .trade_no(payTrans.getTransNo())
                .out_request_no(refundTrans.getOutNo()) // 退款单号
                .build();
        p.setRefund_amount(refundTrans.getAmount());
        String authorization = this.getAuthorization(channel.getSpAppid(),shop.getSubMchid());

        String ret = this.aliPayMapper.refund(authorization, p);
        PostRefundRetObj retObj = JacksonUtil.toObj(ret, PostRefundRetObj.class);
        if (Objects.isNull(retObj)){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createRefund: param = {}, code = {}, message = {}", p, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝退款"));
        } else {
            PostRefundAdaptorVo dto = PostRefundAdaptorVo.builder()
                    .transNo(retObj.getTrade_no())
                    .amount(Long.valueOf((long) (retObj.getRefund_fee() * 100)))
                    .successTime(LocalDateTime.now())
                    .userReceivedAccount(retObj.getBuyer_login_id())
                    .build();
            return dto;
        }
    }

    /**
     * 管理员查询退款信息
     */
    @Override
    public RefundTrans returnRefund(Account account, String outNo) {
        Channel channel = account.getChannel();
        assert Objects.nonNull(channel): "AliPayAdaptor.returnRefund:channel is null";
        GetRefundParam param = GetRefundParam.builder()
                .out_trade_no(outNo)
                .build();
        String ret = this.aliPayMapper.getRefund(this.getAuthorization(channel.getSpAppid(), account.getSubMchid()), param);
        GetRefundRetObj retObj = JacksonUtil.toObj(ret, GetRefundRetObj.class);
        if (Objects.isNull(retObj)){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createRefund: param = {}, code = {}, message = {}", param, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝查询退款"));
        } else {
            RefundTrans trans = new RefundTrans();
            trans.setTransNo(retObj.getTrade_no());
            trans.setOutNo(retObj.getOut_trade_no());
            trans.setAmount((long) (retObj.getRefund_amount() * 100));
            if (Objects.isNull(retObj.getRefund_status())) {
                trans.setStatus(RefundTrans.FAIL);
            }else if (retObj.getRefund_status().equals("REFUND_SUCCESS")){
                trans.setStatus(RefundTrans.SUCCESS);
            }
            trans.setDivAmount((long)(retObj.getRefund_royaltys().get(0).getRefund_amount() * 100));
            trans.setSuccessTime(retObj.getGmt_refund_pay());
            return trans;
        }
    }


    /**
     * 分账交易
     */
    @Override
    public PostDivPayAdaptorVo createDivPay(PayTrans payTrans, String outNo) {
        Account shop = payTrans.getAccount();
        assert Objects.nonNull(shop) : "AliPayAdaptor.createDivPay:shop is null";
        Channel channel = shop.getChannel();
        assert Objects.nonNull(channel): "AliPayAdaptor.createDivPay:channel is null";
        /*set param*/
        PostDivPayParam param = PostDivPayParam.builder()
                .out_request_no(outNo)
                .trade_no(payTrans.getTransNo())
                .royalty_parameters(new ArrayList<>() {
                    {
                        add(OpenApiRoyaltyDetailInfoPojo.builder()
                                .trans_out(shop.getSubMchid())
                                .trans_in(channel.getSpMchid())
                                .amount((double) payTrans.getDivAmount() / 100.0).build());
                    }
                }).build();

        String ret = this.aliPayMapper.postDivPay(this.getAuthorization(channel.getSpAppid(),shop.getSubMchid()), param);
        PostDivPayRetObj retObj = JacksonUtil.toObj(ret, PostDivPayRetObj.class);
        if (Objects.isNull(retObj)){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createRefund: param = {}, code = {}, message = {}", param, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝支付分账接口"));
        } else {
            PostDivPayAdaptorVo dto = PostDivPayAdaptorVo.builder()
                    .orderId(retObj.getSettle_no())
                    .transactionId(retObj.getTrade_no())
                    .build();
            /*set ret*/
            return dto;
        }
    }


    /**
     * 分账退款
     * https://opendocs.alipay.com/open-v3/09d1eu?scene=common&pathHash=19d27b3b
     * https://opendocs.alipay.com/support/01rg1b
     * 只退分账，不退款
     * refund_amount（退款金额）设置为0元。
     * refund_royalty_parameters 按照以上参数说明设置退分账信息和金额。
     */
    @Override
    public PostRefundAdaptorVo createDivRefund(DivRefundTrans divRefundTrans) {
        RefundTrans refundTrans = divRefundTrans.getRefundTrans();
        assert Objects.nonNull(refundTrans) : "AliPayAdaptor.createDivRefund: refundTrans is null";
        PayTrans payTrans = refundTrans.getPayTrans();
        assert Objects.nonNull(payTrans) : "AliPayAdaptor.createDivRefund:payTrans is null";
        Account shop = divRefundTrans.getAccount();
        assert Objects.nonNull(shop): "AliPayAdaptor.createDivRefund:shop is null";
        Channel channel = shop.getChannel();
        assert Objects.nonNull(channel): "AliPayAdaptor.createDivRefund:channel is null";

        String authorization = this.getAuthorization(channel.getSpAppid(),shop.getSubMchid());

        //仅仅退分账
        PostRefundParam p = PostRefundParam.builder()
                .trade_no(payTrans.getTransNo())
                .refund_amount(0.0)
                .refund_royalty_parameters(new ArrayList<>() {
                    {
                        add(RoyaltyDetailInfoPojo.builder().trans_out(channel.getSpMchid()).amount(divRefundTrans.getAmount() / 100.0).build());
                    }
                }).build();

        String ret = this.aliPayMapper.refund(authorization, p);
        PostRefundRetObj retObj = JacksonUtil.toObj(ret, PostRefundRetObj.class);
        if (Objects.isNull(retObj)){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createDivRefund: param = {}, code = {}, message = {}", p, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝分账退款"));
        } else {
            PostRefundAdaptorVo dto = PostRefundAdaptorVo.builder()
                    .transNo(retObj.getTrade_no())
                    .userReceivedAccount(retObj.getBuyer_login_id())
                    .build();
            return dto;
        }
    }
    /**
     * 2023-dgn1-006
     * @author huangzian
     */
    @Override
    public void cancelChannel(Account account) {
        Channel channel= account.getChannel();
        assert Objects.nonNull(channel) : "AliPayAdaptor.cancelChannel:channel is null";
        CancelDivParam param=CancelDivParam.builder()
                .out_request_no(account.getId().toString())
                .receiver_list(new ArrayList<>(){
                    {
                        add(RoyaltyEntity.builder().account(channel.getSpMchid()).build());
                    }
                })
                .build();
        String ret=this.aliPayMapper.cancelDiv(this.getAuthorization(channel.getSpAppid(), account.getSubMchid()), param);
        CancelDivRetObj retObj= JacksonUtil.toObj(ret, CancelDivRetObj.class);
        if (Objects.isNull(retObj)){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createChannel: param = {}, code = {}, message = {}", param, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝删除分账"));
        }
    }

    /**
     * 2023-dgn1-006
     * @author huangzian
     */
    @Override
    public void createChannel(Account account) {
        Channel channel= account.getChannel();
        assert Objects.nonNull(channel) : "AliPayAdaptor.createChannel:channel is null";
        CreateDivParam param= CreateDivParam.builder()
                .authorization(this.getAuthorization(channel.getSpAppid(), account.getSubMchid()))
                .out_request_no(account.getId().toString())
                .receiver_list(new ArrayList<>(){
                    {
                        add(RoyaltyEntity.builder().account(channel.getSpMchid()).build());
                    }
                })
                .build();
        String ret=this.aliPayMapper.createDiv(this.getAuthorization(channel.getSpAppid(), account.getSubMchid()),param);
        CreateDivRetObj retObj= JacksonUtil.toObj(ret, CreateDivRetObj.class);
        if (Objects.isNull(retObj)){
            ExceptionRetObj retObj1  = JacksonUtil.toObj(ret, ExceptionRetObj.class);
            log.error("createChannel: param = {}, code = {}, message = {}", param, retObj1.getCode(), retObj1.getMessage());
            throw new BusinessException(ReturnNo.PAY_INVOKEAPI_ERROR, String.format(ReturnNo.PAY_INVOKEAPI_ERROR.getMessage(), "支付宝创建分账"));
        }
    }

    /**
     * 产生authorization
     *
     * @param appId 开放平台颁发的应用id
     * @param certSn 收款商家账号
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    private String getAuthorization(String appId, String certSn) {
        //请求发起时间，使用Unix时间戳，精确到毫秒。支付宝会拒绝处理过期10分钟后的请求，请保持商家自身系统的时间准确性。
        String time = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        //随机字符串，每次请求需要保持唯一。支付宝使用字段值用于防重放，
        String nounce = snowFlakeIdWorker.nextId().toString();
        return String.format("app_id=${%s},app_cert_sn=${%s},nonce=${%s},timestamp=${%s}", appId,certSn, nounce, time);
    }
}
