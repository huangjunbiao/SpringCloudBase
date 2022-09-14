package com.huang.cloudbase.authserver;

import com.huang.cloudbase.authserver.config.AuthServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

/**
 * @author huangjunbiao_cdv
 */

@SpringBootConfiguration
@ImportAutoConfiguration({AuthServerConfiguration.class})
@EnableAutoConfiguration
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class);
    }
}
