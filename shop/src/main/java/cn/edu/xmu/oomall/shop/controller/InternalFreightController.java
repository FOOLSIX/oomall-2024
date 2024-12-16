//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.shop.controller.dto.ProductItemDto;
import cn.edu.xmu.oomall.shop.dao.bo.ProductItem;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.shop.service.FreightService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class InternalFreightController {
    private static final Logger logger = LoggerFactory.getLogger(InternalFreightController.class);

    private final FreightService freightService;
    /**
     * 计算一批商品的运费并根据策略分包
     *
     * @param tid
     * @param rid
     * @param voList
     * @return
     */
    @PostMapping("/internal/templates/{id}/regions/{rid}/freightprice")
    public ReturnObject getFreight(
            @PathVariable("id") Long tid,
            @PathVariable("rid") Long rid,
            @Validated @RequestBody List<ProductItemDto> voList
    ) {
        List<ProductItem> boList = voList.stream().map(vo -> CloneFactory.copy(new ProductItem(), vo)).collect(Collectors.toList());
        return new ReturnObject(this.freightService.cacuFreightPrice(boList, tid, rid));
    }
}
