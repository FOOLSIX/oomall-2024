package cn.edu.xmu.oomall.customer;

import cn.edu.xmu.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"cn.edu.xmu.javaee.core",
        "cn.edu.xmu.oomall.customer"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {RocketMQMessageListener.class, SpringBootApplication.class }),
        })
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(value = "cn.edu.xmu.javaee.core.jpa", repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.xmu.oomall.customer.mapper.jpa")
@EnableFeignClients
@EnableDiscoveryClient
public class CommentApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

}
