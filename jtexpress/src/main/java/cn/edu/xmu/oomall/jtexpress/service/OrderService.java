package cn.edu.xmu.oomall.jtexpress.service;

import cn.edu.xmu.oomall.jtexpress.dao.CustomerDao;
import cn.edu.xmu.oomall.jtexpress.dao.OrderDao;
import cn.edu.xmu.oomall.jtexpress.dao.TraceDetailDao;
import cn.edu.xmu.oomall.jtexpress.dao.bo.Order;
import cn.edu.xmu.oomall.jtexpress.exception.JTException;
import cn.edu.xmu.oomall.jtexpress.exception.ReturnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 徐森彬
 * 2023-dgn3-02
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private TraceDetailDao traceDetailDao;

    public Order addOrder(Order order) {
        logger.debug("顾客{}创建订单:{}", order.getCustomerCode(), order);
        //查找下运单的用户是否存在
        if (!customerDao.checkByCode(order.getCustomerCode())) throw new JTException(ReturnError.ILLEGAL_PARAMETER);

        //将要创建的orderbuild为满血模型
        orderDao.builder(order);

        Order newOrder = order.addOrder();

        //为了产生物流轨迹，创建一个任务，新增物流轨迹，以满足查询轨迹的需求
        createTrace(newOrder);

        return newOrder;

    }

    public Order cancelOrder(String customerCode,String txLogisticId) {
        logger.debug("顾客{}取消订单,顾客号为{}", customerCode, txLogisticId);
        //查找下运单的用户是否存在
        if (!customerDao.checkByCode(customerCode)) throw new JTException(ReturnError.ILLEGAL_PARAMETER);
        //判断订单是否已经存在
        Order oldOrder = orderDao.findOrderByTxLogisticId(txLogisticId);
        //如果不存在，抛出非法参数异常
        if (null == oldOrder) throw new JTException(ReturnError.ILLEGAL_PARAMETER);
        //如果该订单不属于该用户，抛出非法参数异常
        if (customerCode == null || !(customerCode.equals(oldOrder.getCustomerCode()))) {
            throw new JTException(ReturnError.ILLEGAL_PARAMETER);
        }
        return oldOrder.cancelOrder();
    }

    public void createTrace(Order order) {
        //不是订单完成状态不创建轨迹
        if (order.getOrderStatus() != Order.StatusEnum.COMPLETED.getCode()) {
            return;
        }
        try {
            traceDetailDao.createRandomTrace(order.getBillCode());
        } catch (RuntimeException e) {
            logger.debug("运单{}创建轨迹出错：{}", order.getBillCode(), e.getMessage());
        }
    }
}
