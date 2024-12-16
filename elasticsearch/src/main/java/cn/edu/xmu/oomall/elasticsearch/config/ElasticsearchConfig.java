package cn.edu.xmu.oomall.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfig {

    private final ElasticsearchProperties elasticsearchProperties;

    @Bean
    @Lazy
    public ElasticsearchClient client(@Autowired(required = false) ObjectMapper objectMapper) {
        // 解析URI配置信息
        HttpHost[] hosts = elasticsearchProperties.getUris().stream()
                .map(uri -> new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()))
                .toArray(HttpHost[]::new);

        // 创建RestClient
        RestClient restClient = RestClient.builder(hosts).build();

        // 创建Transport层，这里使用JacksonJsonpMapper来处理JSON
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // 创建并返回ElasticsearchClient实例
        return new ElasticsearchClient(transport);
    }

}
