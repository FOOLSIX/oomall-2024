package cn.edu.xmu.oomall.jtexpress.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.jtexpress.dao.bo.Order;
import cn.edu.xmu.oomall.jtexpress.dao.bo.PersonInfo;
import cn.edu.xmu.oomall.jtexpress.exception.JTException;
import cn.edu.xmu.oomall.jtexpress.exception.ReturnError;
import cn.edu.xmu.oomall.jtexpress.mapper.jpa.OrderPoMapper;
import cn.edu.xmu.oomall.jtexpress.mapper.jpa.PersonInfoPoMapper;
import cn.edu.xmu.oomall.jtexpress.mapper.po.OrderPo;
import cn.edu.xmu.oomall.jtexpress.mapper.po.PersonInfoPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Random;

@Repository
@RefreshScope
public class OrderDao {
    private final Logger logger = LoggerFactory.getLogger(OrderDao.class);

    @Autowired
    private OrderPoMapper orderPoMapper;


    @Autowired
    private PersonInfoPoMapper personInfoPoMapper;

    public Order saveOrder(Order order) throws RuntimeException {

        //保存寄件人收件人信息
        savePersonInfo(order);

        //如果运单号为空，生成运单号
        if (null == order.getBillCode()) order.setBillCode(generateWaybillNumber());

        //保存订单
        OrderPo orderPo = CloneFactory.copy(new OrderPo(), order);
        logger.debug("save orderPo : {}", orderPo);
        orderPo = orderPoMapper.save(orderPo);
        CloneFactory.copy(order, orderPo);

        return order;
    }

    public Order updateOrder(Order order) throws RuntimeException {
        //保存寄件人收件人信息
        savePersonInfo(order);

        //保存订单
        OrderPo orderPo = CloneFactory.copy(new OrderPo(), order);
        logger.debug("update orderPo : {}", orderPo);
        orderPo = orderPoMapper.save(orderPo);
        CloneFactory.copy(order, orderPo);
        return order;
    }

    public void savePersonInfo(Order order) throws RuntimeException {
        if (order.getSender().getAlter()) {
            //寄件人信息
            PersonInfoPo senderPo = CloneFactory.copy(new PersonInfoPo(), order.getSender());
            //保存寄件人信息
            logger.debug("save senderPo : {}", senderPo);
            PersonInfoPo personInfoPo=personInfoPoMapper.save(senderPo);
            order.getSender().setId(personInfoPo.getId());
            order.setSenderId(personInfoPo.getId());
        }
        if (order.getReceiver().getAlter()) {
            //收件人信息
            PersonInfoPo receiverPo = CloneFactory.copy(new PersonInfoPo(), order.getReceiver());
            //保存收件人信息
            logger.debug("save receivePo : {}", receiverPo);
            PersonInfoPo personInfoPo=personInfoPoMapper.save(receiverPo);
            order.getReceiver().setId(personInfoPo.getId());
            order.setReceiverId(personInfoPo.getId());
        }
    }

    public Order findOrderByTxLogisticId(String txLogisticId) {
        Optional<OrderPo> orderPo = orderPoMapper.findByTxLogisticId(txLogisticId);
        if (orderPo.isEmpty()) return null;
        Order order = CloneFactory.copy(new Order(), orderPo.get());

        //设置sender
        Optional<PersonInfoPo> senderPo = personInfoPoMapper.findById(order.getSenderId());
        if (senderPo.isEmpty()) throw new JTException(ReturnError.SYSTEM_ERROR);
        order.setSender(CloneFactory.copy(new PersonInfo(), senderPo.get()));

        //设置receiver
        Optional<PersonInfoPo> receiverPo = personInfoPoMapper.findById(order.getReceiverId());
        if (receiverPo.isEmpty()) throw new JTException(ReturnError.SYSTEM_ERROR);
        order.setReceiver(CloneFactory.copy(new PersonInfo(), receiverPo.get()));

        this.builder(order);
        return order;
    }

    public void builder(Order order) {
        order.setOrderDao(this);
    }

    private String generateWaybillNumber() {
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        // 生成随机偏移量
        long randomShift = new Random().nextInt(10) + 5;

        // 在时间戳上随机偏移
        long shiftedTimestamp = (timestamp << randomShift) | (timestamp >>> (64 - randomShift));

        // 使用时间戳和随机数生成运单号
        return String.format("JT%d%06d", shiftedTimestamp, new Random().nextInt(100000));
    }


}
