package cn.edu.xmu.oomall.sfexpress.service;

import cn.edu.xmu.oomall.sfexpress.controller.dto.*;
import cn.edu.xmu.oomall.sfexpress.controller.vo.*;
import cn.edu.xmu.oomall.sfexpress.dao.ExpressDao;
import cn.edu.xmu.oomall.sfexpress.dao.ExpressStatusEnum;
import cn.edu.xmu.oomall.sfexpress.dao.RouteDao;
import cn.edu.xmu.oomall.sfexpress.dao.bo.*;
import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import cn.edu.xmu.oomall.sfexpress.exception.SFException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@Service
@Transactional
public class SfexpressService {

    private final ExpressDao expressDao;
    private final RouteDao routeDao;

    @Autowired
    public SfexpressService(ExpressDao expressDao, RouteDao routeDao) {
        this.expressDao = expressDao;
        this.routeDao = routeDao;
    }

    public PostCreateOrderRetVo createOrder(PostCreateOrderDTO postCreateOrderDTO) {
        List<CargoDetailsDTO> cargoDetails = postCreateOrderDTO.getCargoDetails();
        List<ContactInfoListDTO> contactInfoList = postCreateOrderDTO.getContactInfoList();
        // 1.dto变bo
        Express express = new Express();
        BeanUtils.copyProperties(postCreateOrderDTO, express);
        List<CargoDetail> cargoDetailList = CargoDetailsDTO.toBo(cargoDetails);
        express.setCargoDetails(cargoDetailList);
        List<ContactInfo> contactInfos = ContactInfoListDTO.toBo(contactInfoList);
        express.setContactInfoList(contactInfos);
        express.setStatus(ExpressStatusEnum.UNCOMPLETED.getCode());
        express.generateWaybillNo();
        express.setWaybillType(1);
        express.setCreateTime(new Timestamp((new Date()).getTime()));

        // 2. 生成运单号
        express.generateWaybillNo();

        // 3. 创建运单
        expressDao.createExpress(express);

        // 4.构建返回参数
        PostCreateOrderRetVo createOrderRetVo = new PostCreateOrderRetVo();
        BeanUtils.copyProperties(express, createOrderRetVo);
        createOrderRetVo.setCargoDetails(cargoDetails);
        createOrderRetVo.setContactInfoList(contactInfoList);
        return createOrderRetVo;
    }

    public PostSearchOrderRetVo searchOrder(PostSearchOrderDTO postSearchOrderDTO) {
        String orderId = postSearchOrderDTO.getOrderId();
        Express express = expressDao.getExpressByOrderId(orderId);
        if (null == express) {
            throw new SFException(SFErrorCodeEnum.E8024);
        }
        List<ContactInfo> contactInfoList = express.getContactInfoList();

        PostSearchOrderRetVo postSearchOrderRetVo = new PostSearchOrderRetVo();
        for (ContactInfo contactInfo : contactInfoList) {
            if (contactInfo.getContactType() == 1) {
                postSearchOrderRetVo.setOrigincode(contactInfo.getCity());
            }
            if (contactInfo.getContactType() == 2) {
                postSearchOrderRetVo.setDestcode(contactInfo.getCity());
            }
        }
        postSearchOrderRetVo.setOrderId(orderId);

        PostSearchOrderRetVo.WaybillNoInfoListDTO waybillNoInfoListDTO = new PostSearchOrderRetVo.WaybillNoInfoListDTO();
        waybillNoInfoListDTO.setWaybillNo(express.getWaybillNo());
        waybillNoInfoListDTO.setWaybillType(express.getWaybillType());
        List<PostSearchOrderRetVo.WaybillNoInfoListDTO> waybillNoInfoListDTOList = new ArrayList<>();
        postSearchOrderRetVo.setWaybillNoInfoList(waybillNoInfoListDTOList);
        return postSearchOrderRetVo;
    }

    public PostSearchRoutesRetVo searchRoutes(PostSearchRoutesDTO postSearchRoutesDTO) throws SFException {
        PostSearchRoutesRetVo searchRoutesRetVo = new PostSearchRoutesRetVo();
        int trackingType = postSearchRoutesDTO.getTrackingType();
        List<String> trackingNumber = postSearchRoutesDTO.getTrackingNumber();
        // 查询号类别:
        //1:根据顺丰运单号查询,trackingNumber将被当作顺丰运单号处理
        //2:根据客户订单号查询,trackingNumber将被当作客户订单号处理
        String mailNo = null;
        if (trackingType == 1) {
            mailNo = trackingNumber.get(0);
        }
        if (trackingType == 2) {
            String orderId = trackingNumber.get(0);
            Express express = expressDao.getExpressByOrderId(orderId);
            mailNo = express.getWaybillNo();
        }
        RouteResps routeResps = routeDao.getRouteRespsByMailNo(mailNo);
        List<RouteResps> routeRespsList = new ArrayList<>();
        routeRespsList.add(routeResps);
        searchRoutesRetVo.setRouteResps(routeRespsList);

        return searchRoutesRetVo;
    }

    public PostUpdateOrderRetVo updateOrder(PostUpdateOrderDTO postUpdateOrderDTO) {
        Integer dealType = postUpdateOrderDTO.getDealType();
        String orderId = postUpdateOrderDTO.getOrderId();
        Double totalHeight = postUpdateOrderDTO.getTotalHeight();
        Double totalLength = postUpdateOrderDTO.getTotalLength();
        Double totalVolume = postUpdateOrderDTO.getTotalVolume();
        Double totalWeight = postUpdateOrderDTO.getTotalWeight();
        Double totalWidth = postUpdateOrderDTO.getTotalWidth();
        Timestamp sendStartTm = postUpdateOrderDTO.getSendStartTm();
        Timestamp pickupAppointEndtime = postUpdateOrderDTO.getPickupAppointEndtime();

        List<PostUpdateOrderDTO.WaybillNoInfoListDTO> waybillNoInfoList = postUpdateOrderDTO.getWaybillNoInfoList();

        if (dealType == 1) {
            // 通知揽件
            Express express = expressDao.getExpressByOrderId(orderId);
            express.setTotalHeight(totalHeight);
            express.setTotalLength(totalLength);
            express.setTotalVolume(totalVolume);
            express.setTotalWeight(totalWeight);
            express.setTotalWidth(totalWidth);
            express.setSendStartTm(sendStartTm);
            express.setPickupAppointEndtime(pickupAppointEndtime);
            expressDao.updateExpress(express);

            // 异步生成路由列表
            CompletableFuture.runAsync(() -> {
                RouteResps routeResps = new RouteResps();
                routeResps.setMailNo(express.getWaybillNo());
                List<Route> routes = RouteResps.generateRandomRouteList(3);
                routeResps.setRoutes(routes);
                routeDao.saveRouteResps(routeResps);
            }).thenRun(() -> {
                // 这里可以添加异步生成路由列表后的其他操作，或者不添加任何操作
            });

        } else if (dealType == 2) {
            // 取消订单
            expressDao.cancelExpress(orderId);
        }
        Express express = expressDao.getExpressByOrderId(orderId);
        PostUpdateOrderRetVo updateOrderRetVo = new PostUpdateOrderRetVo();
        updateOrderRetVo.setOrderId(orderId);
        PostUpdateOrderDTO.WaybillNoInfoListDTO waybillNoInfoListDTO = new PostUpdateOrderDTO.WaybillNoInfoListDTO();
        waybillNoInfoListDTO.setWaybillNo(express.getWaybillNo());
        waybillNoInfoListDTO.setWaybillType(express.getWaybillType());
        List<PostUpdateOrderDTO.WaybillNoInfoListDTO> waybillNoInfoListDTOList = new ArrayList<>();
        updateOrderRetVo.setWaybillNoInfoList(waybillNoInfoListDTOList);
        updateOrderRetVo.setResStatus(express.getStatus());
        return updateOrderRetVo;
    }
}
