//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.config;

import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.javaee.core.util.SnowFlakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class PaymentConfig {

    private final static Logger logger = LoggerFactory.getLogger(PaymentConfig.class);

    @Value("${oomall.datacenter:0}")
    private Long dataCenterId;

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
        Long ipLong = Common.ipToLong(ipAddress);
        Long workerId = ipLong % SnowFlakeIdWorker.maxWorkerId;
        return new SnowFlakeIdWorker(workerId, this.dataCenterId);
    }
}
