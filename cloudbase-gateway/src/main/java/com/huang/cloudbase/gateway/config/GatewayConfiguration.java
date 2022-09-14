package com.huang.cloudbase.gateway.config;

import com.huang.cloudbase.gateway.filter.AuthGatewayFilterFactory;
import com.huang.cloudbase.gateway.router.CustomizeInMemoryRouteDefinitionRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjunbiao_cdv
 */

@Configuration(proxyBeanMethods = false)
@ComponentScan({
    "com.huang.cloudbase.gateway.filter",
    "com.huang.cloudbase.gateway.service"
})
@AutoConfigureBefore({GatewayAutoConfiguration.class})
public class GatewayConfiguration {

    @Bean
    public CustomizeInMemoryRouteDefinitionRepository routeDefinitionRepository() {
        return new CustomizeInMemoryRouteDefinitionRepository();
    }

    @Bean
    public AuthGatewayFilterFactory.Config config() {
        return new AuthGatewayFilterFactory.Config();
    }
}
