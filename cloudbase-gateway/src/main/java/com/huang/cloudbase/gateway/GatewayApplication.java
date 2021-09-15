package com.huang.cloudbase.gateway;

import com.huang.cloudbase.gateway.config.GatewayConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author huangjunbiao
 * @date 2020/12/3 11:58
 */
@SpringBootApplication
@EnableDiscoveryClient
@ImportAutoConfiguration({
    GatewayConfiguration.class
})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
