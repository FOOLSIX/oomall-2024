package cn.edu.xmu.oomall.jtexpress;

import cn.edu.xmu.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author 徐森彬
 * 2023-dgn3-02
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(value = "cn.edu.xmu.javaee.core.jpa", repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.xmu.oomall.jtexpress.mapper.jpa")
public class jtexpressApplication {
    public static void main(String[] args) {
        SpringApplication.run(jtexpressApplication.class, args);
    }

}
