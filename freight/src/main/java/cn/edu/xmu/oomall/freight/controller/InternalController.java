package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.aop.UserLevel;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.controller.dto.ExpressDto;
import cn.edu.xmu.oomall.freight.controller.dto.SendPackageDto;
import cn.edu.xmu.oomall.freight.service.ExpressService;
import cn.edu.xmu.oomall.freight.controller.vo.ExpressVo;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.controller.vo.SimpleExpressVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 2024-dsg-111
 */
@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
@Slf4j
@RequiredArgsConstructor
public class InternalController {
    private final ExpressService expressService;

    /**
     * 2024-dsg-111
     * 创建运单
     */
    @PostMapping("/shops/{shopId}/packages")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject createPackage(@PathVariable Long shopId,
                                      @Validated @RequestBody ExpressDto expressDto,
                                      @LoginUser UserDto user, @UserLevel Integer userLevel) {
        Express express = Express.builder().sendRegionId(expressDto.getSender().getRegionId())
                .sendAddress(expressDto.getSender().getAddress())
                .sendMobile(expressDto.getSender().getMobile())
                .sendName(expressDto.getSender().getName())
                .receivRegionId(expressDto.getDelivery().getRegionId())
                .receivAddress(expressDto.getDelivery().getAddress())
                .receivMobile(expressDto.getDelivery().getMobile())
                .receivName(expressDto.getDelivery().getName())
                .contractId(expressDto.getShopLogisticId())
                .goodsType(expressDto.getGoodsType()).weight(expressDto.getWeight()).build();
        Express newExpress = this.expressService.createExpress(shopId, express, user);
        return new ReturnObject(ReturnNo.CREATED, CloneFactory.copy(new SimpleExpressVo(), newExpress));
    }


    /**
     * 2024-dsg-111
     * 商户取消运单
     */
    @PutMapping("/shops/{shopId}/packages/{id}/cancel")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject cancelPackage(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user, @UserLevel Integer userLevel) {
        this.expressService.cancelExpress(shopId, id, user);
        return new ReturnObject();
    }

    /**
     *  2024-dsg-111
     * 商户发出揽件
     */
    @PutMapping("/shops/{shopId}/packages/{id}/send")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject sendPackage(@PathVariable Long shopId, @PathVariable Long id,
                                    @LoginUser UserDto user, @UserLevel Integer userLevel, @RequestBody SendPackageDto vo) {
        this.expressService.sendExpress(shopId, id, user, vo.getStartTime(), vo.getEndTime());
        return new ReturnObject();
    }
}
