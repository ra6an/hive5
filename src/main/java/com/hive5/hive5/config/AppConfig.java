package com.hive5.hive5.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myapp")
@Getter
@Setter
public class AppConfig {
    private String apiPrefix;
    private String jwtSecretKey;
    private long jwtExpirationTime;
}
