package com.huang.cloudbase.defaultserver.test.controller;

import com.huang.cloudbase.defaultserver.config.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangjunbiao
 * @date 2020/12/7 10:59
 */
@RestController
public class DefaultTestController {
    @Autowired
    private ServerProperties serverProperties;

    @GetMapping("/z/test")
    public String test() {
        return this.serverProperties.getData();
    }
}
