package cn.edu.xmu.oomall.product.mapper.rocketmq;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Rui Li
 * @task 2023-dgn2-007
 */
@Component
public class OrderMapper {

    private final static Logger logger = LoggerFactory.getLogger(OrderMapper.class);
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    @Lazy
    public OrderMapper(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 删除onsale
     * 在send-delete-onsale-topic发送消息看是否存在订单，如果不存在则在reply-del-onsale-topic收到回复去物理删除，如果未收到回复则不删除
     * @author Rui Li
     * @task 2023-dgn2-007
     */
    public void sendDelMessage(OnSale onSale, UserDto user)
    {
        String packStr = JacksonUtil.toJson(onSale);
        Message<String> msg = MessageBuilder.withPayload(packStr).setHeader("user", user).build();
        this.rocketMQTemplate.asyncSend("send-delete-onsale-topic", msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("delete: delete onsale id {} message send.", onSale.getId());
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("delete: delete onsale id {} fail to send message.", onSale.getId());
            }
        });
    }
}
