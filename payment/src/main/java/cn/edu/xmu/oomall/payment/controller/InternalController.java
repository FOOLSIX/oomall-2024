//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.controller.vo.RefundTransVo;
import cn.edu.xmu.oomall.payment.controller.dto.PayTransDto;
import cn.edu.xmu.oomall.payment.controller.dto.RefundTransDto;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import cn.edu.xmu.oomall.payment.dao.bo.RefundTrans;
import cn.edu.xmu.oomall.payment.dao.channel.vo.PostPayTransAdaptorVo;
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

/**
 * 内部的接口
 */
@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class InternalController {

    private final static Logger logger = LoggerFactory.getLogger(InternalController.class);

    private final PaymentService paymentService;
    private final RefundService refundService;

    /**
     * modified by ych
     * task 2023-dgn1-004
     * @param id
     * @param payTransDto
     * @param user
     * @return
     */
    @PostMapping("/accounts/{id}/payments")
    @Audit(departName = "shops")
    public ReturnObject createPayment(@PathVariable Long id, @Validated @RequestBody PayTransDto payTransDto, @LoginUser UserDto user){
        logger.debug("createPayment: orderPayVo = {}", payTransDto);
        if (payTransDto.getTimeExpire().isBefore(payTransDto.getTimeBegin())){
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "结束时间不能早于开始时间");
        }
        if ( payTransDto.getDivAmount() > payTransDto.getAmount()){
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "分账金额不能大于支付金额");
        }

        PayTrans payTrans = CloneFactory.copy(new PayTrans(), payTransDto);

        payTrans.setAccountId(id);
        PostPayTransAdaptorVo dto =  this.paymentService.createPayment(payTrans, user);
        return new ReturnObject(ReturnNo.CREATED, dto);
    }

    @PostMapping("/shops/{shopId}/payments/{id}/refunds")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject createRefund(@PathVariable Long shopId, @PathVariable Long id, @Validated @RequestBody(required = true) RefundTransDto vo, @LoginUser UserDto user){
        logger.debug("createRefund: shopId = {}, id = {}, vo = {}",shopId, id, vo);
        if (vo.getDivAmount() > vo.getAmount()){
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, "分账退回金额不能大于退款金额");
        }
        RefundTrans refundTrans = this.refundService.createRefund(shopId, id, vo.getAmount(), vo.getDivAmount(), user);
        RefundTransVo refundTransVo = new RefundTransVo(refundTrans);
        return new ReturnObject(ReturnNo.CREATED, refundTransVo);
    }

    @GetMapping("/shops/{shopId}/refunds/{id}")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject findRefundById(@PathVariable Long shopId, @PathVariable Long id) {
        RefundTrans refundTrans = this.refundService.findRefundById(shopId, id);
        return new ReturnObject(new RefundTransVo(refundTrans));
    }

    /**
     * modified by ych
     * task 2023-dgn1-004
     */
    @PutMapping("/channels/{id}/payments/div")
    public ReturnObject divPayment(@PathVariable Long id, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        //删去了 endTime为null的情况 上层会赋值
        this.paymentService.divPayment(id, endTime);
        return new ReturnObject();
    }



    @DeleteMapping("/shops/{shopId}/payments/{id}")
    @Audit(departName = "shops")
    public ReturnObject cancelPayment(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user) {
        this.paymentService.cancelPayment(shopId, id, user);
        return new ReturnObject();
    }
}
