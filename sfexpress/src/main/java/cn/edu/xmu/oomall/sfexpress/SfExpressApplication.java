package cn.edu.xmu.oomall.sfexpress;

import cn.edu.xmu.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core", "cn.edu.xmu.oomall.sfexpress"})
@EnableJpaRepositories(value = "cn.edu.xmu.javaee.core.jpa", repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.xmu.oomall.sfexpress.mapper")
@EnableDiscoveryClient
public class SfExpressApplication {
    public static void main(String[] args) {
        SpringApplication.run(SfExpressApplication.class, args);
    }
}
