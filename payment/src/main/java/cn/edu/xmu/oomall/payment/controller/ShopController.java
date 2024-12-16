//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.controller.dto.AccountDto;
import cn.edu.xmu.oomall.payment.controller.vo.*;
import cn.edu.xmu.oomall.payment.dao.bo.Account;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import cn.edu.xmu.oomall.payment.dao.bo.RefundTrans;
import cn.edu.xmu.oomall.payment.service.ChannelService;
import cn.edu.xmu.oomall.payment.service.PaymentService;
import cn.edu.xmu.oomall.payment.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 管理人员的接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
public class ShopController {

    private final static Logger logger = LoggerFactory.getLogger(ShopController.class);

    private final PaymentService paymentService;
    private final RefundService refundService;
    private final ChannelService channelService;

    /**
     *获得商铺所有的支付渠道
     */
    @Audit(departName = "shops")
    @GetMapping("/accounts")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject retrieveAccount(@PathVariable Long shopId,
                                        @RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false,defaultValue = "10") Integer pageSize) {
        List<Account> accountList = this.channelService.retrieveAccount(shopId, page, pageSize);
        PageVo<FullAccountVo> pageVo = new PageVo<>(accountList.stream().map(o -> CloneFactory.copy(new FullAccountVo(o), o)).collect(Collectors.toList()), page, pageSize);
        return new ReturnObject(pageVo);
    }

    /**
     *签约支付渠道
     */
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    @PostMapping("/channels/{id}/accounts")
    public ReturnObject createAccounts(@PathVariable("shopId") Long shopId,
                                       @PathVariable("id") Long id,
                                       @Validated @RequestBody AccountDto vo,
                                       @LoginUser UserDto user){
        Account newAccount = new Account();
        newAccount.setSubMchid(vo.getSubMchid());
        newAccount.setShopId(shopId);
        Account account = this.channelService.createAccount(id, newAccount, user);
        SimpleAccountVo dto = CloneFactory.copy(new SimpleAccountVo(), account);
        return new ReturnObject(ReturnNo.CREATED, dto);
    }

    /**
     * 获得商铺支付渠道
     */
    @GetMapping("/accounts/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findAccoutById(@PathVariable("shopId") Long shopId,
                                         @PathVariable("id") Long id){
         Account account = this.channelService.findAccountById(shopId, id);
        return new ReturnObject(new FullAccountVo(account));
    }

    /**
     *解约店铺的账户
     */
    @DeleteMapping("/accounts/{id}")
    @Audit(departName = "shops")
    public ReturnObject cancelAccount(@PathVariable("shopId") Long shopId,
                                       @PathVariable("id") Long id, @LoginUser UserDto user){
        this.channelService.cancelAccount(shopId, id, user);
        return new ReturnObject();
    }

    /**
     *修改收款账号为有效
     */
    @Audit(departName = "shops")
    @PutMapping("/accounts/{id}/valid")
    public ReturnObject validAccount(@PathVariable("shopId") Long shopId,
                                               @PathVariable("id") Long id,
                                               @LoginUser UserDto user) {
        this.channelService.validAccount(shopId, id, user);
        return new ReturnObject();
    }

    /**
     *修改支付渠道为无效
     */
    @Audit(departName = "shops")
    @PutMapping("/accounts/{id}/invalid")
    public ReturnObject invalidAccount(@PathVariable("shopId") Long shopId,
                                                 @PathVariable("id") Long id,
                                                 @LoginUser UserDto user) {
        this.channelService.invalidAccount(shopId, id, user);
        return new ReturnObject();
    }

    /**
     * 查询退款单
     */
    @GetMapping("/refunds/{id}")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findRefundById(@PathVariable Long shopId, @PathVariable Long id) {
        RefundTrans trans= this.refundService.findRefundById(shopId, id);
        return new ReturnObject(new RefundTransVo(trans));
    }

    /**
     * 调账
     * @author Rui Li
     * task 2023-dgn1-005
     */
    @PutMapping("/refunds/{id}")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject adjustRefund(@PathVariable Long shopId,
                                     @PathVariable Long id,
                                     @LoginUser UserDto user) {
        // 只有管理员可以调账
        if(!shopId.equals(PLATFORM)){
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "退款单", id, shopId));
        }
        this.refundService.adjustRefund(shopId, id, user);
        return new ReturnObject();
    }

    @GetMapping("/payments/{id}")
    @Audit(departName = "shops")
    public ReturnObject getPayment(@PathVariable Long shopId, @PathVariable Long id) {
        PayTrans payTrans = this.paymentService.findPayment(shopId, id);
        PayTransVo fullPaymentDto = new PayTransVo(payTrans);
        return new ReturnObject(fullPaymentDto);
    }

    /**
     * 查询退款信息
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    @GetMapping("/channels/{id}/refunds")
    @Audit(departName = "shops")
    public ReturnObject retrieveRefunds(@PathVariable Long shopId,
                                        @PathVariable Long id,
                                        @RequestParam(required = false) String transNo,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginTime,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                        @RequestParam(required = false) Integer status,
                                        @RequestParam(required = false,defaultValue = "1") Integer page,
                                        @RequestParam(required = false,defaultValue = "10") Integer pageSize){

        List<RefundTrans> trans = this.refundService.retrieveRefunds(shopId, id, transNo, beginTime, endTime, status, page, pageSize);
        PageVo<SimpleRefundVo> dto =new PageVo<>(trans.stream().map(o -> CloneFactory.copy(new SimpleRefundVo(), o)).collect(Collectors.toList()), page, pageSize);

        return new ReturnObject(dto);
    }

    /**
     * modified By ych
     * task 2023-dgn1-004
     */
    @GetMapping("/channels/{id}/payments")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject retrievePayments(@PathVariable Long shopId,
                                         @PathVariable Long id,
                                         @RequestParam(required = false) String transNo,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginTime,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                         @RequestParam(required = false) Integer status,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        //删去关于时间的判断 上层会赋值
        List<PayTrans> payTrans = this.paymentService.retrievePayments(shopId, id, transNo, beginTime, endTime, status, page, pageSize);
        PageVo<SimpleTransVo> dto = new PageVo<>(payTrans.stream().map(o ->new SimpleTransVo(o)).collect(Collectors.toList()), page, pageSize);
        return new ReturnObject(dto);
    }

    /**
     * 调账
     * @author ych
     * task 2023-dgn1-004
     */
    @PutMapping("/payments/{id}")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject adjustPayment(@PathVariable Long shopId,
                                      @PathVariable Long id,
                                      @LoginUser UserDto user) {
        // 只有管理员可以调账
        if(!shopId.equals(PLATFORM)){
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "支付交易", id, shopId));
        }
        this.paymentService.adjustPayment(shopId, id, user);
        return new ReturnObject();
    }
}
