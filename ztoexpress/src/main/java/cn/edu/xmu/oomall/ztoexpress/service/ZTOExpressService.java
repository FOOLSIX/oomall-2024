package cn.edu.xmu.oomall.ztoexpress.service;

import cn.edu.xmu.oomall.ztoexpress.controller.dto.CancelPreOrderDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.CreateOrderDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.GetOrderInfoDto;
import cn.edu.xmu.oomall.ztoexpress.controller.dto.QueryTrackDto;
import cn.edu.xmu.oomall.ztoexpress.dao.ExpressDao;
import cn.edu.xmu.oomall.ztoexpress.dao.OrderDao;
import cn.edu.xmu.oomall.ztoexpress.dao.PersonDao;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.ExpressBo;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.OrderBo;

import cn.edu.xmu.oomall.ztoexpress.unit.CloneFactory;

import cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnResult;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ZTOExpressService {
    private OrderDao orderDao;
    private PersonDao personDao;
    private ExpressDao expressDao;
    @Autowired
    public ZTOExpressService(OrderDao orderDao, PersonDao personDao, ExpressDao expressDao) {
        this.orderDao = orderDao;
        this.personDao = personDao;
        this.expressDao = expressDao;
    }

    private Logger logger = LoggerFactory.getLogger(ZTOExpressService.class);
    @Transactional(rollbackFor = Exception.class)
    public ZTOReturnResult createOrder(CreateOrderDto createOrderDto) {
        OrderBo orderBo = CloneFactory.copy(new OrderBo(), createOrderDto);
        return orderDao.createOrder(orderBo);
    }
    @Transactional(rollbackFor = Exception.class)
    public ZTOReturnResult getOrderInfo(GetOrderInfoDto getOrderInfoDto) {
        OrderBo orderBo = new OrderBo();
        CloneFactory.copy(orderBo, getOrderInfoDto);
        return orderDao.getOrderInfo(orderBo);
    }
    @Transactional(rollbackFor = Exception.class)
    public ZTOReturnResult cancelPreOrder(CancelPreOrderDto cancelPreOrderDto) {
        OrderBo orderBo =new OrderBo();
        CloneFactory.copy(orderBo, cancelPreOrderDto);
        return orderDao.cancelPreOrder(orderBo);
    }
    @Transactional(rollbackFor = Exception.class)
    public ZTOReturnResult queryTrack(QueryTrackDto queryTrackDto) {
        ExpressBo expressBo=new ExpressBo();
        CloneFactory.copy(expressBo, queryTrackDto);
        return expressDao.queryTrack(expressBo);
    }
}
