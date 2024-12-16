package cn.edu.xmu.oomall.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ConfigurationProperties(prefix = "spring.elasticsearch")
@Data
public class ElasticsearchProperties {

    private List<URI> uris;

    @DurationUnit(ChronoUnit.MILLIS)
    private Duration connectionTimeout;

}
