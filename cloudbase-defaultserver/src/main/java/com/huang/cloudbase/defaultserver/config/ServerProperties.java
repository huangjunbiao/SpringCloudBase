package com.huang.cloudbase.defaultserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangjunbiao_cdv
 */
@ConfigurationProperties(prefix = "cloudbase.server")
public class ServerProperties {
    private String data = "111";

    /**
     * test
     */
    private String test = "aaa";

    /**
     * boolean
     */
    private boolean enable = true;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
