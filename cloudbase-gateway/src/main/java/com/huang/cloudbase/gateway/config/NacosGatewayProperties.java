package com.huang.cloudbase.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjunbiao
 * @date 2020/12/4 16:39
 */
@Configuration
public class NacosGatewayProperties {
    @Value("${cloudbase-gateway-test.data:}")
    private String configData;

    public void test() {
        System.out.println(configData);
    }
    
}
