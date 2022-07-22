package com.huang.cloudbase.defaultserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangjunbiao_cdv
 */
@ConfigurationProperties(prefix = "cloudbase.server")
public class ServerProperties {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
