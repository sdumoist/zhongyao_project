package com.xpxp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "snowflake")
public class SnowflakeProperties {
    private long workerId;
    private long datacenterId;
}