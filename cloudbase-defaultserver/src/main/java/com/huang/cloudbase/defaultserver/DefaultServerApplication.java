package com.huang.cloudbase.defaultserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author huangjunbiao
 * @date 2020/12/2 18:55
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DefaultServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DefaultServerApplication.class, args);
    }
}
