package cn.edu.xmu.oomall.shop.config;

import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Configuration
public class ShopConfig {

    @Value("${rocketmq.name-server}")
    private String namesrv;

    @Value("${rocketmq.producer.group}")
    private String producerGroup;

    @Value("${oomall.datacenter:0}")
    private Long dataCenterId;

    /**
     * 配置rocketMQTemplate
     * 避免ProducerGroup组别重复
     *
     * @return
     * @author ZhaoDong Wang
     * 2023-dgn1-009
     */
    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        DefaultMQProducer producer = new TransactionMQProducer();
        int random = new SecureRandom().nextInt(100);
        producer.setProducerGroup(String.format("%s-%d", this.producerGroup, random));
        producer.setNamesrvAddr(this.namesrv);
        RocketMQTemplate template = new RocketMQTemplate();
        template.setProducer(producer);
        return template;
    }

    /**
     * 配置雪花算法，用ip地址作为workerId
     * @author SongYuan Song
     * 2024-imp-002
     */
    @Bean
    public SnowFlakeIdWorker snowFlakeIdWorker(){
        if (this.dataCenterId > SnowFlakeIdWorker.maxDatacenterId){
            throw new IllegalArgumentException("oomall.datacenter大于最大值"+SnowFlakeIdWorker.maxDatacenterId);
        }

        InetAddress ip = null;
        try {
            ip = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String ipAddress = ip.getHostAddress();
        log.debug("snowFlakeIdWorker: ip = {}",ipAddress);
        Long ipLong = Common.ipToLong(ipAddress);
        Long workerId = ipLong % SnowFlakeIdWorker.maxWorkerId;
        return new SnowFlakeIdWorker(workerId, this.dataCenterId);
    }

}
