package cn.edu.xmu.oomall.alipay.service.mq;

import cn.edu.xmu.oomall.alipay.openFeign.PaymentFeightService;
import cn.edu.xmu.oomall.alipay.service.bo.NotifyBody;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
@RocketMQMessageListener(topic = "alipay-notify-topic", consumeMode = ConsumeMode.CONCURRENTLY, consumerGroup = "alipay-notify-group")
public class NotifyService implements RocketMQListener<NotifyBody> {

    private PaymentFeightService paymentFeightService;
    @Autowired
    public NotifyService(PaymentFeightService paymentFeightService) {
        this.paymentFeightService = paymentFeightService;
    }



    @Override
    public void onMessage(NotifyBody notifyBody) {
        paymentFeightService.notify(notifyBody);
    }
}
