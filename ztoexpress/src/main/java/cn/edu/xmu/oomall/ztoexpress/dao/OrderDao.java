package cn.edu.xmu.oomall.ztoexpress.dao;

import cn.edu.xmu.oomall.ztoexpress.controller.vo.CancelPreOrderVo;
import cn.edu.xmu.oomall.ztoexpress.controller.vo.GetOrderInfoVo;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.OrderBo;
import cn.edu.xmu.oomall.ztoexpress.mapper.ExpressPoMapper;
import cn.edu.xmu.oomall.ztoexpress.mapper.OrderPoMapper;
import cn.edu.xmu.oomall.ztoexpress.mapper.PersoninfoPoMapper;
import cn.edu.xmu.oomall.ztoexpress.mapper.po.ExpressPo;
import cn.edu.xmu.oomall.ztoexpress.unit.ZTOException;
import cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static cn.edu.xmu.oomall.ztoexpress.unit.ZTOReturnNo.*;

@Repository
@RefreshScope
@Transactional
public class OrderDao {
    private OrderPoMapper orderPoMapper;
    private PersoninfoPoMapper personinfoPoMapper;
    private ExpressPoMapper expressPoMapper;
    @Autowired
    public OrderDao(OrderPoMapper orderPoMapper , PersoninfoPoMapper personinfoPoMapper,ExpressPoMapper expressPoMapper) {
        this.orderPoMapper = orderPoMapper;
        this.personinfoPoMapper = personinfoPoMapper;
        this.expressPoMapper = expressPoMapper;
    }
    //创建订单
    public ZTOReturnResult createOrder(OrderBo orderBo) {
        Logger logger = LoggerFactory.getLogger(OrderDao.class);
        //合作商订单号为空
        if (orderBo.getOrderPo().getPartnerOrderCode() == null) {
            logger.info("合作商订单号为空");
            throw new ZTOException(po001);
        }
        //订单已存在
        if(!orderPoMapper.findAllByPartnerOrderCode(orderBo.getOrderPo().getPartnerOrderCode()).isEmpty()) {
            logger.info("订单已经存在了");
            throw new ZTOException(po001);
        }
        //设置默认
        orderBo.getOrderPo().toBeDefault();
        //设置订单号
        orderBo.getOrderPo().setOrderCode(generateWaybillNumber());
        //插入联系人并且设置id
        orderBo.getOrderPo().setSenderId(personinfoPoMapper.save(orderBo.getSenderPo()).getId());
        orderBo.getOrderPo().setReceiverId(personinfoPoMapper.save(orderBo.getReceiverPo()).getId());
        //设置为已创建状态
        orderBo.getOrderPo().toBeDefault();
        saveOrder(orderBo);
        return new ZTOReturnResult(SYS000);
    }
    //存储订单
    public boolean saveOrder(OrderBo orderBo){
        orderPoMapper.save(orderBo.getOrderPo());
        return true;
    }

    //获取订单信息
    public ZTOReturnResult getOrderInfo(OrderBo orderBo) {
        Logger logger = LoggerFactory.getLogger(OrderDao.class);
        logger.debug(orderBo.toString()+"\n");
        //两者都为空
        if(orderBo.getOrderPo().getOrderCode()==null&&orderBo.getExpressPo().getBillcode()==null){
            logger.debug("两者都为空\n");
            throw new ZTOException(S208);
        }
        //订单号不空
        else if(orderBo.getOrderPo().getOrderCode()!=null){
            orderBo.setOrderPo(orderPoMapper.findByOrderCode(orderBo.getOrderPo().getOrderCode()));
            if(orderBo.getOrderPo()!=null){
                logger.debug("通过订单号找到订单\n");
                return new ZTOReturnResult(SYS000,new GetOrderInfoVo(orderBo));
            }
            else{
                logger.debug("找不到订单\n");
                throw new ZTOException(E404);
            }
        }
        //订单号为空但运单号不空
        else {
            orderBo.setExpressPo(expressPoMapper.findByBillcode(orderBo.getExpressPo().getBillcode()));
            if(orderBo.getExpressPo()==null){
                logger.debug("通过运单号找不到运单\n");
                throw new ZTOException(E404);
            }
            else{
                orderBo.setOrderPo(orderPoMapper.findByIdIs(orderBo.getExpressPo().getZtoOrderId()));
                if(orderBo.getOrderPo()!=null){
                    logger.debug("通过运单号找到订单\n");
                    return new ZTOReturnResult(SYS000,new GetOrderInfoVo(orderBo));
                }
                else {
                    logger.debug("通过运单号找不到订单\n");
                    throw new ZTOException(E404);
                }
            }

        }
    }
    //取消订单
    public ZTOReturnResult cancelPreOrder(OrderBo orderBo){
        Logger logger = LoggerFactory.getLogger(OrderDao.class);
        logger.debug(orderBo.toString()+"\n");
        //两者都为空
        if(orderBo.getOrderPo().getOrderCode()==null&&orderBo.getExpressPo().getBillcode()==null){
            logger.debug("两者都为空\n");
            throw new ZTOException(S208);
        }
        //订单号不空
        else if(orderBo.getOrderPo().getOrderCode()!=null){
            logger.debug("订单号非空\n");
            orderBo.setOrderPo(orderPoMapper.findByOrderCode(orderBo.getOrderPo().getOrderCode()));
            if(orderBo.getOrderPo()!=null){
                logger.debug("通过订单号找到订单\n");
                if(orderBo.getOrderPo().getOrderStatus()==0){
                    logger.debug("订单状态本身就是取消的\n");
                    throw new ZTOException(E403);
                }
                //修改订单状态并且更新运单状态
                setOrderStatusToCancel(orderBo);
                //更新订单
                saveOrder(orderBo);
                return new ZTOReturnResult(SYS000,new CancelPreOrderVo(orderBo.getOrderPo().getOrderStatus()));
            }
        }
        //订单号为空但运单号不空
        else {
            orderBo.setExpressPo(expressPoMapper.findByBillcode(orderBo.getExpressPo().getBillcode()));
            if(orderBo.getExpressPo()==null){
                logger.debug("通过运单号找不到运单\n");
                throw new ZTOException(E404);
            }
            else{
                orderBo.setOrderPo(orderPoMapper.findByIdIs(orderBo.getExpressPo().getZtoOrderId()));
                if(orderBo.getOrderPo()!=null){
                    logger.debug("通过运单号找到订单\n");
                    if(orderBo.getOrderPo().getOrderStatus()==0){
                        logger.debug("订单状态本身就是取消的\n");
                        throw new ZTOException(E403);
                    }
                    setOrderStatusToCancel(orderBo);
                    saveOrder(orderBo);
                    return new ZTOReturnResult(SYS000,new CancelPreOrderVo(orderBo.getOrderPo().getOrderStatus()));
                }
                else {
                    logger.debug("通过运单号找不到订单\n");
                    throw new ZTOException(E404);
                }
            }

        }
        throw new ZTOException(S208);
    }
    public void setOrderStatusToCancel(OrderBo orderBo){
        orderBo.getOrderPo().setOrderStatus(0);
        List<ExpressPo> expressPos=expressPoMapper.findAllByZtoOrderId(orderBo.getOrderPo().getId());
        for(ExpressPo expressPo :expressPos){
            expressPo.setExpressStatus(0);
            expressPoMapper.updateStatus(expressPo.getExpressStatus(),expressPo.getId());
        }
    }

    //雪花
    private String generateWaybillNumber() {
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        // 生成随机偏移量
        long randomShift = new Random().nextInt(10) + 5;

        // 在时间戳上随机偏移
        long shiftedTimestamp = (timestamp << randomShift) | (timestamp >>> (64 - randomShift));

        // 使用时间戳和随机数生成运单号
        return String.format("ZT%d%06d", shiftedTimestamp, new Random().nextInt(100000));
    }
}
