package com.huang.cloudbase.defaultserver.config;

import com.huang.cloudbase.defaultserver.Model;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjunbiao
 * @date 2020/12/8 15:36
 */
@Configuration
@ComponentScan({
    "com.huang.cloudbase.defaultserver.controller"
})
@EnableConfigurationProperties({ServerProperties.class, TestConfig.class})
@RefreshScope
public class DefaultServerConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "cloudbase.server.test", value = "enabled", havingValue = "true")
    public Model model(TestConfig config) {
        Model model = new Model();
        model.setName(config.getMap());
        System.out.println("初始化：" );
        System.out.println(model.getName());
        return model;
    }

}
