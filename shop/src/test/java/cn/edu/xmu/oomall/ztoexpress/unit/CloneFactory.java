package cn.edu.xmu.oomall.ztoexpress.unit;

import cn.edu.xmu.oomall.ztoexpress.controller.dto.CancelPreOrderDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.CreateOrderDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.GetOrderInfoDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.QueryTrackDto;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.ExpressBo;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.OrderBo;

public final class CloneFactory {

    /**
     * Copy all fields from source to target
     *
     *
     * @return the copied target object
     */
    public static OrderBo copy(OrderBo orderBo, CreateOrderDto createOrderDto){
        orderBo.getOrderPo().setPartnerType(createOrderDto.getPartnerType());
        orderBo.getOrderPo().setOrderType(createOrderDto.getOrderType());
        orderBo.getOrderPo().setPartnerOrderCode(createOrderDto.getPartnerOrderCode());
        orderBo.getSenderPo().setName(createOrderDto.getSenderInfo().getName());
        orderBo.getSenderPo().setPhone(createOrderDto.getSenderInfo().getPhone());
        orderBo.getSenderPo().setCity(createOrderDto.getSenderInfo().getCity());
        orderBo.getSenderPo().setDistrict(createOrderDto.getSenderInfo().getDistrict());
        orderBo.getSenderPo().setAddress(createOrderDto.getSenderInfo().getAddress());
        orderBo.getSenderPo().setProvince(createOrderDto.getSenderInfo().getProvince());
        orderBo.getReceiverPo().setName(createOrderDto.getReceiverInfo().getName());
        orderBo.getReceiverPo().setPhone(createOrderDto.getReceiverInfo().getPhone());
        orderBo.getReceiverPo().setCity(createOrderDto.getReceiverInfo().getCity());
        orderBo.getReceiverPo().setDistrict(createOrderDto.getReceiverInfo().getDistrict());
        orderBo.getReceiverPo().setAddress(createOrderDto.getReceiverInfo().getAddress());
        orderBo.getReceiverPo().setProvince(createOrderDto.getReceiverInfo().getProvince());
        return orderBo;
    }
    public static OrderBo copy(OrderBo orderBo, GetOrderInfoDto getOrderInfoDto){
        orderBo.getOrderPo().setPartnerType(getOrderInfoDto.getType());
        orderBo.getOrderPo().setOrderCode(getOrderInfoDto.getOrderCode());
        orderBo.getExpressPo().setBillcode(getOrderInfoDto.getBillCode());
        return orderBo;
    }
    public static OrderBo copy(OrderBo orderBo, CancelPreOrderDto cancelPreOrderDto){
        orderBo.setCancelType(cancelPreOrderDto.getCancelType());
        orderBo.getOrderPo().setOrderCode(cancelPreOrderDto.getOrderCode());
        orderBo.getExpressPo().setBillcode(cancelPreOrderDto.getBillCode());
        return orderBo;
    }
    public static ExpressBo copy(ExpressBo expressBo, QueryTrackDto queryTrackDto){
        expressBo.getExpressPo().setBillcode(queryTrackDto.getBillCode());
        return expressBo;
    }

}


