package com.huang.cloudbase.defaultserver;

import com.huang.cloudbase.defaultserver.config.DefaultServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

/**
 * @author huangjunbiao
 * @date 2020/12/2 18:55
 */
@ImportAutoConfiguration({DefaultServerConfiguration.class})
@EnableAutoConfiguration
public class DefaultServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DefaultServerApplication.class, args);
    }
}
