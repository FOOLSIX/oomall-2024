package cn.edu.xmu.oomall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "cn.edu.xmu.oomall.elasticsearch.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class ElasticsearchTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchTestApplication.class, args);
    }
}
