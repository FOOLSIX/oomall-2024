package cn.edu.xmu.oomall.ztoexpress.controller;

import cn.edu.xmu.oomall.ztoexpress.controller.dto.CancelPreOrderDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.CreateOrderDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.GetOrderInfoDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.QueryTrackDto;
import cn.edu.xmu.oomall.ztoexpress.service.ZTOExpressService;
import cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author czy
 * 2023-dgn3-03
 */

@RestController
@RequestMapping(value = "/interal")
public class ZTOExpressController {
    private ZTOExpressService ztoExpressService;
    @Autowired
    ZTOExpressController(ZTOExpressService ztoExpressService){
        this.ztoExpressService = ztoExpressService;
    }
    //创建订单
    @PostMapping(value = "zto.open.createOrder" ,consumes ="application/json;charset=UTF-8" )
    public ZTOReturnResult createZtoOrder(@RequestHeader("x-appKey") String appKey, @RequestHeader("x-dataDigest") String dataDigest, @RequestBody CreateOrderDto createOrderDto){
        return ztoExpressService.createOrder(createOrderDto);
    }
    //获取订单信息
    @PostMapping(value = "zto.open.getOrderInfo" ,consumes ="application/json;charset=UTF-8" )
    public ZTOReturnResult getOrderInfo(@RequestHeader("x-appKey") String appKey, @RequestHeader("x-dataDigest") String dataDigest, @RequestBody GetOrderInfoDto getOrderInfoDto){
        return ztoExpressService.getOrderInfo(getOrderInfoDto);
}
//取消订单
    @PostMapping(value = "zto.open.cancelPreOrder" ,consumes ="application/json;charset=UTF-8" )
    public ZTOReturnResult cancelPreOrder(@RequestHeader("x-appKey") String appKey, @RequestHeader("x-dataDigest") String dataDigest, @RequestBody CancelPreOrderDto cancelPreOrderDto){
        return ztoExpressService.cancelPreOrder(cancelPreOrderDto);
    }
    //查询轨迹
    @PostMapping(value = "zto.merchant.waybill.track.query",consumes = "application/json;charset=UTF-8")
    public ZTOReturnResult queryTrack(@RequestHeader("x-appKey") String appKey, @RequestHeader("x-dataDigest") String dataDigest, @RequestBody QueryTrackDto queryTrackDto){
        return ztoExpressService.queryTrack(queryTrackDto);
    }
}
