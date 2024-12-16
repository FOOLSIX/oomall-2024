package cn.edu.xmu.oomall.alipay.config;

import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

@Configuration
public class AlipayConfig {

    private final static Logger logger = LoggerFactory.getLogger(AlipayConfig.class);

    @Value("${rocketmq.name-server}")
    private String namesrv;

    @Value("${rocketmq.producer.group}")
    private String alipayGroup;

    @Value("${oomall.datacenter:0}")
    private Long dataCenterId;
    @Bean
    public RocketMQTemplate rocketMQTemplate(){
        DefaultMQProducer producer = new DefaultMQProducer();
        int random = new Random().nextInt(100);
        producer.setProducerGroup(String.format("%s-%d",this.alipayGroup,random));
        producer.setNamesrvAddr(this.namesrv);
        RocketMQTemplate template = new RocketMQTemplate();
        template.setProducer(producer);
        return template;
    }

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
        logger.debug("snowFlakeIdWorker: ip = {}",ipAddress);
        Long ipLong = this.ipToLong(ipAddress);
        Long workerId = ipLong % SnowFlakeIdWorker.maxWorkerId;
        return new SnowFlakeIdWorker(workerId, this.dataCenterId);
    }

    // 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
    private Long ipToLong(String strIp) {
        Long[] ip = new Long[4];
        // 先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }
}

