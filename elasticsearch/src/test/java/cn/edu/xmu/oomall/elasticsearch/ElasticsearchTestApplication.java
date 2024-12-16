package cn.edu.xmu.oomall.elasticsearch;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(basePackages = "cn.edu.xmu.oomall.elasticsearch.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class ElasticsearchTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchTestApplication.class, args);
    }
}
