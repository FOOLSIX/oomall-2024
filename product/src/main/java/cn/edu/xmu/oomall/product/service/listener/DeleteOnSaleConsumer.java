package cn.edu.xmu.oomall.product.service.listener;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.product.service.OnsaleService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 用于删除商品销售
 */
@Service
@RocketMQMessageListener(consumerGroup = "product_del_onsale", topic = "reply-del-onsale-topic", selectorExpression = "1", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumeThreadNumber = 5)
@RequiredArgsConstructor
public class DeleteOnSaleConsumer implements RocketMQListener<Message>, RocketMQPushConsumerLifecycleListener {
    private static final Logger logger = LoggerFactory.getLogger(DeleteOnSaleConsumer.class);

    private final OnsaleService onsaleService;

    @Override
    public void onMessage(Message msg) {
        String body = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
        Long onSaleId = JacksonUtil.toObj(body, Long.class);
        UserDto user = (UserDto) msg.getHeaders().get("user");
        if(null == onSaleId || null == user){
            logger.error("onMessage: wrong encoding.... msg = {}", msg);
        } else{
            this.onsaleService.delete(onSaleId, user);
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
    }
}