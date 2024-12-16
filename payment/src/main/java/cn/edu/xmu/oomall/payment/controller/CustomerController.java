//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.model.vo.StatusVo;
import cn.edu.xmu.oomall.payment.controller.dto.AlipayNotifyDto;
import cn.edu.xmu.oomall.payment.controller.dto.WepayNotifyDto;
import cn.edu.xmu.oomall.payment.controller.vo.FullAccountVo;
import cn.edu.xmu.oomall.payment.dao.bo.*;
import cn.edu.xmu.oomall.payment.dao.channel.WePayAdaptor;
import cn.edu.xmu.oomall.payment.service.ChannelService;
import cn.edu.xmu.oomall.payment.service.PaymentService;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.SYSTEM;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final static Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final ChannelService channelService;
    private final PaymentService paymentService;

    @GetMapping("/refund/states")
    public ReturnObject getRefundState() {
        return new ReturnObject(RefundTrans.STATUSNAMES.keySet().stream().map(key -> new StatusVo(key, RefundTrans.STATUSNAMES.get(key))).collect(Collectors.toList()));
    }

    @GetMapping("/payments/states")
    public ReturnObject getPaymentState() {
        return new ReturnObject(PayTrans.STATUSNAMES.keySet().stream().map(key -> new StatusVo(key, PayTrans.STATUSNAMES.get(key))).collect(Collectors.toList()));
    }

    @GetMapping("/divpay/states")
    public ReturnObject getDivpayState() {
        return new ReturnObject(DivPayTrans.STATUSNAMES.keySet().stream().map(key -> new StatusVo(key, PayTrans.STATUSNAMES.get(key))).collect(Collectors.toList()));
    }

    @GetMapping("/divrefund/states")
    public ReturnObject getDivrefundState() {
        return new ReturnObject(DivRefundTrans.STATUSNAMES.keySet().stream().map(key -> new StatusVo(key, PayTrans.STATUSNAMES.get(key))).collect(Collectors.toList()));
    }

    /**
     *获得商户有效的收款账号
     */
    @GetMapping("/accounts")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getChannels(@RequestParam Long shopId,
                                        @RequestParam(required = false,defaultValue = "1") Integer page,
                                        @RequestParam(required = false,defaultValue = "10") Integer pageSize) {
        List<Account> channels = this.channelService.retrieveValidAccount(shopId, page, pageSize);
        PageVo<FullAccountVo> pageDto = new PageVo<>(channels.stream().map(o -> CloneFactory.copy(new FullAccountVo(o), o)).collect(Collectors.toList()), page, pageSize);
        return new ReturnObject(pageDto);
    }

    /**
     * https://opendocs.alipay.com/open-v3/05w4ku?pathHash=af025e20
     * @param dto
     * @return
     */

    @PostMapping("/notify/payments/alipay")
    public ReturnObject alipayNotify(@Validated @RequestBody AlipayNotifyDto dto) {
        PayTrans payTrans = new PayTrans();
        String status = dto.getTradeStatus();
        if(status.equals("TRADE_SUCCESS")) {
            payTrans.setStatus(PayTrans.SUCCESS);
        } else if(status.equals("TRADE_CLOSED")) {
            payTrans.setStatus(PayTrans.FAIL);
        } else {
            return new ReturnObject();
        }
        payTrans.setOutNo(dto.getOutTradeNo());
        payTrans.setTransNo(dto.getTradeNo());
        payTrans.setSuccessTime(dto.getGmtPayment());
        payTrans.setAmount(dto.getReceiptAmount());

        this.paymentService.updatePaymentByOutNo(payTrans, SYSTEM);
        return new ReturnObject();

    }


    /**
     * https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_5.shtml
     * @param vo
     * @return
     * modified By ych
     * * task 2023-dgn1-004
     */
    @PostMapping("/notify/payments/wepay")
    public ReturnObject wepayNotify(@Validated @RequestBody WepayNotifyDto vo) {
        PayTrans payTrans = new PayTrans();
        Byte status = WePayAdaptor.PayStatusMap.get(vo.getResource().getTradeState());
        if (Objects.nonNull(status)) { //.equal改成!=
            payTrans.setStatus(status);
            payTrans.setOutNo(vo.getResource().getOutTradeNo());
            payTrans.setTransNo(vo.getResource().getTransactionId());
            payTrans.setSuccessTime(vo.getResource().getSuccessTime());
            payTrans.setAmount(vo.getResource().getAmount().getTotal());
            this.paymentService.updatePaymentByOutNo(payTrans, SYSTEM);
        }
        return new ReturnObject();
    }
}
