package com.huang.cloudbase.defaultserver.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjunbiao
 * @date 2020/12/8 15:36
 */
@Configuration
@ComponentScan({"com.huang.cloudbase.defaultserver.test"})
@EnableConfigurationProperties(ServerProperties.class)
@RefreshScope
public class DefaultServerConfiguration {

}
