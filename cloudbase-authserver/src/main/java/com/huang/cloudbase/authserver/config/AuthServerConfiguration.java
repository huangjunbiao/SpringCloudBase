package com.huang.cloudbase.authserver.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjunbiao_cdv
 */

@Configuration
@ComponentScan({
    "com.huang.cloudbase.authserver.controller"
})
public class AuthServerConfiguration {
}
