package cn.edu.xmu.oomall.product.service.listener;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.product.service.ProductService;
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

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

/**
 * 用于删除商品模板
 * @author wuzhicheng
 * @create 2022-12-15 17:53
 */
@Service
@RocketMQMessageListener(consumerGroup = "goods_rm_template", topic = "Del-Template", selectorExpression = "1", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumeThreadNumber = 5)
@RequiredArgsConstructor
public class DeleteTemplateConsumer implements RocketMQListener<Message>, RocketMQPushConsumerLifecycleListener {
    private static final Logger logger = LoggerFactory.getLogger(DeleteTemplateConsumer.class);

    private final ProductService productService;

    @Override
    public void onMessage(Message msg) {
        String body = new String((byte[]) msg.getPayload(), StandardCharsets.UTF_8);
        Long templateId = JacksonUtil.toObj(body, Long.class);
        UserDto user = (UserDto) msg.getHeaders().get("user");
        if(templateId==null || user==null){
            logger.error("TemplateConsumer: wrong encoding.... msg = {}", msg);
        } else{
            this.productService.removeTemplateId(PLATFORM, templateId, user);
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
    }
}