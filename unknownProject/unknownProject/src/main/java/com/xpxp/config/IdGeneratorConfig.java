package com.xpxp.config;

import com.xpxp.util.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {

    private final SnowflakeProperties snowflakeProperties;

    public IdGeneratorConfig(SnowflakeProperties snowflakeProperties) {
        this.snowflakeProperties = snowflakeProperties;
    }

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator(
                snowflakeProperties.getWorkerId(),
                snowflakeProperties.getDatacenterId()
        );
    }
}